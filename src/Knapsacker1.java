
// Adaptation of Phase 1 code to work in 3D 
import java.awt.Shape;
import java.util.*;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Knapsacker1 {
	public static final Scanner scan = new Scanner(System.in);

	public static final int EMPTY = -1; // How empty truck cells are defined.
	public static Scene3DGenerator tempGenerator;

	// Sizes of truck as specified; we can make this flexible if we want to
	//public static final int X_SIZE = 5;// 165 / 5;
	//public static final int Y_SIZE = 8;//25 / 5;
	//public static final int Z_SIZE = 15;//40 / 5;

	

	public static int[][][] search(Utility.ShapeSet shapeSet, int[] scores, int sizeX, int sizeY, int sizeZ) {
		int[][][] truck = new int[sizeX][sizeY][sizeZ];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				for (int k = 0; k < sizeZ; k++) {
					truck[i][j][k] = EMPTY;
				}
			}
		}

		//addPiece(truck, DatabaseGenerator.getDatabase()[3][3], 4, 0, 0, 0);
		//return truck;
		bestScoreForNumber = new int[sizeX * sizeY * sizeZ];
		bestScore = 0;

		recursiveSearch(truck, Utility.getShapeSet(shapeSet), scores, 0, 0, 0, sizeX, sizeY, sizeZ, 0, 0);

		return truck;
	}


	//private static ArrayList<Integer> added1 = new ArrayList<>();
	//private static ArrayList<Integer> added2 = new ArrayList<>();
	private static int[] bestScoreForNumber;
	private static int bestScore;

	private static double skipScalar = 0.95;

	private static void recursiveSearch(int[][][] truck, char[] shapes, int[] scores,
		int wid, int len, int hgt, 
		int x_size, int y_size, int z_size, int score, int numOfPieces) {

		// If 'row' is equal to the truck height, the truck is filled successfully.
		// Note that this is because the program starts filling from top-left.
		if (wid == x_size) {
			return;
		}

		// Jump one up by one once the entire slice is filled
		if (len == y_size) {
			recursiveSearch(truck, shapes, scores, wid + 1, 0, 0, x_size, y_size, z_size, score, numOfPieces);
			return;
		}

		// Jump to the next row once the current row is filled.
		if (hgt == z_size) {
			recursiveSearch(truck, shapes, scores, wid, len + 1, 0, x_size, y_size, z_size, score, numOfPieces);
			return;
		}

		// Deal with scores
		if (score < bestScoreForNumber[numOfPieces] * skipScalar) {
			return;
		}

		if (score > bestScoreForNumber[numOfPieces]) {
			bestScoreForNumber[numOfPieces] = score;
		}

		if (score > bestScore) {
			bestScore = score;
			System.out.println("Best score so far: " + bestScore);
		}

		// If the current cell is not EMPTY, move to the next column.
		if (truck[wid][len][hgt] != EMPTY) {
			recursiveSearch(truck, shapes, scores, wid, len, hgt + 1, x_size, y_size, z_size, score, numOfPieces);
			return;
		}

		// Loop through each possible shapes.
		for (int d = 0; d < 2; d++) {
			int c = 0;
			if (shapes[0] == 'P') {
				if (d == 0) c = 2;
				if (d == 1) c = 1;
				if (d == 2) c = 0;
			}
			else if (shapes[0] == 'A') {
				if (d == 0) c = 2;
				if (d == 1) c = 0;
				if (d == 2) c = 1;
			}

			int shapeID = Utility.characterToID(shapes[c]);
			// Loop through each possible mutation for that shape.
			for (int mutation = 0; mutation < DatabaseGenerator.getDatabase()[shapeID].length; mutation++) {

				int[][][] pieceToPlace = DatabaseGenerator.getDatabase()[shapeID][mutation];

				int zPadding = 0;

				boolean found = false;
				for (int i = 0; i < pieceToPlace[0][0].length; i++) {
					if (pieceToPlace[0][0][i] == 0) { // ? no clue what to do with padding
						zPadding++;
					} else {
						found = true;
						break;
					}
				}

				int yPadding = 0;
				if (!found) {
					zPadding = 0;
					for (int i = 0; i < pieceToPlace[0].length; i++) {
						if (pieceToPlace[0][i][0] == 0) { // ? no clue what to do with padding
							yPadding++;
						} else {
							found = true;
							break;
						}
					}
				}

				//System.out.println(wid + ":" + len + ":" + hgt + ":" + c + ":" + mutation);
				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (Utility.canAddPiece(truck, pieceToPlace, EMPTY, wid, len - yPadding, hgt - zPadding, x_size, y_size, z_size)) {
					Utility.addPiece(truck, pieceToPlace, shapeID, wid, len - yPadding, hgt - zPadding);

					// Recur with the next column.
					recursiveSearch(truck, shapes, scores, wid, len, hgt + 1, x_size, y_size, z_size, score + scores[c], numOfPieces + 1);
					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the truck state.
					Utility.addPiece(truck, pieceToPlace, EMPTY, wid, len - yPadding, hgt - zPadding);
				}
			}
		}

		recursiveSearch(truck, shapes, scores, wid, len, hgt + 1, x_size, y_size, z_size, score, numOfPieces);
	}
}
