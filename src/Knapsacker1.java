
// Adaptation of Phase 1 code to work in 3D 
import java.util.*;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Knapsacker1 {
	public static final Scanner scan = new Scanner(System.in);

	public static final int EMPTY = -1; // How empty truck cells are defined.

	// Sizes of truck as specified; we can make this flexible if we want to
	public static final int X_SIZE = 165 / 5;
	public static final int Y_SIZE = 25 / 5;
	public static final int Z_SIZE = 40 / 5;

	// I haven't touched this yet
	public void fit(Shape3D[] shapes, int xSize, int ySize, int zSize) {
		// Fit
	}

	public void start() {
		boolean testing = true;

		if (testing) {
			collectInput();
		}
	}

	public void collectInput() {
		System.out.print(
				"Please indicate whether to use parcel set 0 (parcels A, B, C), or parcel set 1 (pentomino parcels P, L, T): ");
		int shapeSet = scan.nextInt();

		int mode; // We can use this to toggle modes

		getShapeSet(shapeSet);
	}

	public char[] getShapeSet(int shapeSet) {
		char[] chars = new char[2];

		if (shapeSet == 0) {
			chars[0] = 'A';
			chars[1] = 'B';
			chars[2] = 'C';
		} else if (shapeSet == 0) {
			chars[0] = 'P';
			chars[1] = 'L';
			chars[2] = 'T';
		} else {
			System.out.println("pls just pick 0 or 1 :'(");
			start();
		}
		return chars;
	}

	private static int characterToID(char character) {
		int shapeID = -1;
		if (character == 'A') {
			shapeID = 0;
		} else if (character == 'B') {
			shapeID = 1;
		} else if (character == 'C') {
			shapeID = 2;
		} else if (character == 'P') {
			shapeID = 3;
		} else if (character == 'L') {
			shapeID = 4;
		} else if (character == 'T') {
			shapeID = 5;
		}
		return shapeID;
	}

	public static boolean recursiveSearch(int[][][] truck, char[] inputChars) {
		return recursiveSearch(truck, 0, 0, 0, inputChars);
	}

	private static int n = 3;

	public static boolean recursiveSearch(int[][][] truck, int wid, int len, int hgt, char[] shapes) {
		// If 'row' is equal to the truck height, the truck is filled successfully.
		// Note that this is because the program starts filling from top-left.
		if (hgt == Z_SIZE && wid == 0 && len == 0) {
			n--;
			return n <= 0;
		}

		// Jump one up by one once the entire slice is filled
		if (wid == X_SIZE && len == Y_SIZE) {
			return recursiveSearch(truck, 0, 0, hgt + 1, shapes);
		}

		// Jump to the next row once the current row is filled.
		if (wid == X_SIZE) {
			return recursiveSearch(truck, 0, len + 1, 0, shapes);
		}

		// If the current cell is not EMPTY, move to the next column.
		if (truck[wid][len][hgt] != EMPTY) {
			return recursiveSearch(truck, wid, len + 1, hgt, shapes);
		}

		// Loop through each possible shapes.
		for (int c = 0; c < 3; c++) {
			int shapeID = characterToID(shapes[c]);
			// Loop through each possible mutation for that shape.
			for (int mutation = 0; mutation < ShapeDatabase.data[shapeID].length; mutation++) {

				int[][][] pieceToPlace = ShapeDatabase.data[shapeID][mutation];

				int emptyPadding = 0;
				for (int i = 0; i < pieceToPlace.length; i++) {
					if (pieceToPlace[i][0] == 0) { // ? no clue what to do with padding
						emptyPadding++;
					} else {
						break;
					}
				}

				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (canAddPiece(truck, pieceToPlace, wid - emptyPadding, len, hgt)) {
					addPiece(truck, pieceToPlace, shapeID, wid - emptyPadding, len, hgt);


					// Recur with the next column.
					if (recursiveSearch(truck, len, wid + 1, hgt, shapes)) {
						return true;
					}

					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the truck state.
					addPiece(truck, pieceToPlace, EMPTY, wid - emptyPadding, len, hgt);
				}
			}
		}
		// If the shape cannot be placed, return false.
		return false;
	}

	public static void addPiece(int[][][] truck, int[][][] piece, int pieceID, int x, int y, int z) {
		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
			{
				for (int k = 0; k < piece[i][j].length; k++) {
					if (piece[i][j][k] == 1) {
						// Add the ID of the pentomino to the board if the pentomino occupies this
						// square.
						truck[x + i][y + j][z + k] = pieceID;
					}
				}
			}
		}
	}

	public static boolean canAddPiece(int[][][] truck, int[][][] piece, int x, int y, int z) {
		// Checks whether it fits the board
		if (x < 0 || x + piece.length > X_SIZE || y < 0 || y + piece[0].length > Y_SIZE || z < 0
				|| z + piece[0][0].length > Z_SIZE) {
			return false;
		}

		// Loop over x position of pentomino
		for (int i = 0; i < piece.length; i++) {
			// Loop over y position of pentomino
			for (int j = 0; j < piece[i].length; j++) {
				for (int k = 0; k < piece[i][j].length; k++) {
					if (piece[i][j][k] == 1 && truck[x + i][y + j][z + k] != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 */
	public static void main(String[] args) {
		start();

		System.out.println("Done!");
		return;
	}

}
