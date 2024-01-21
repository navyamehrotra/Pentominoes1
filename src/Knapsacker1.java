
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

	

	public static int[][][] search(Utility.ShapeSet shapeSet, int sizeX, int sizeY, int sizeZ) {
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

		boolean found = recursiveSearch(truck, getShapeSet(shapeSet), 0, 0, 0, sizeX, sizeY, sizeZ);
		System.out.println("Found filling: " + found);

		return truck;
	}

	private static int i = 0;
	private static int incr = 1000000;

	private static ArrayList<Integer> added1 = new ArrayList<>();
	private static ArrayList<Integer> added2 = new ArrayList<>();

	private static int a = 0;

	private static boolean recursiveSearch(int[][][] truck, char[] shapes,
		int wid, int len, int hgt, 
		int x_size, int y_size, int z_size) {
		i++;

		if (i % incr == 0) {
			for(Integer asd : added1) {
				System.out.print(asd);
			}
			a++;
			System.out.println(a);


			//tempGenerator.updateGrid(truck);
			System.out.println();
			//System.out.println(wid + ":" + len + ":" + hgt);
		}

		if (a == 5) {
			return false;
		}


		// If 'row' is equal to the truck height, the truck is filled successfully.
		// Note that this is because the program starts filling from top-left.
		if (wid == x_size) {
			return true;
		}

		// Jump one up by one once the entire slice is filled
		if (len == y_size) {
			return recursiveSearch(truck, shapes, wid + 1, 0, 0, x_size, y_size, z_size);
		}

		// Jump to the next row once the current row is filled.
		if (hgt == z_size) {
			return recursiveSearch(truck, shapes, wid, len + 1, 0, x_size, y_size, z_size);
		}

		// If the current cell is not EMPTY, move to the next column.
		if (truck[wid][len][hgt] != EMPTY) {
			return recursiveSearch(truck, shapes, wid, len, hgt + 1, x_size, y_size, z_size);
		}

		// Loop through each possible shapes.
		for (int c = 0; c < 3; c++) {
			int shapeID = characterToID(shapes[c]);
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

				if (!found || pieceToPlace[0][yPadding][zPadding] == 0) {
					System.out.println();
				}

				//System.out.println(wid + ":" + len + ":" + hgt + ":" + c + ":" + mutation);
				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (canAddPiece(truck, pieceToPlace, wid, len - yPadding, hgt - zPadding, x_size, y_size, z_size)) {
					addPiece(truck, pieceToPlace, shapeID, wid, len - yPadding, hgt - zPadding);
					tempGenerator.updateGrid(truck);
					added1.add(shapeID);
					added2.add(mutation);

					// Recur with the next column.
					if (recursiveSearch(truck, shapes, wid, len, hgt + 1, x_size, y_size, z_size)) {
						return true;
					}

					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the truck state.
					addPiece(truck, pieceToPlace, EMPTY, wid, len - yPadding, hgt - zPadding);
					tempGenerator.updateGrid(truck);
					added1.remove(added1.size() - 1);
					added2.remove(added2.size() - 1);
				}
			}
		}
		// If the shape cannot be placed, return false.
		return false;
	}

	

	

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 */
	public static void main(String[] args) {
		//search(ShapeSet.ABC, X_SIZE, Y_SIZE, Z_SIZE);

		System.out.println("Done!");
		return;
	}

}
