
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

	public enum ShapeSet {
		ABC,
		PLT
	}

	public static int[][][] search(ShapeSet shapeSet, int sizeX, int sizeY, int sizeZ) {
		int[][][] truck = new int[sizeX][sizeY][sizeZ];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				for (int k = 0; k < sizeZ; k++) {
					truck[i][j][k] = EMPTY;
				}
			}
		}

		addPiece(truck, ShapeDatabase.data[3][1], 4, 0, 0, 0);

		return truck;

		/*boolean found = recursiveSearch(truck, getShapeSet(shapeSet), 0, 0, 0, sizeX, sizeY, sizeZ);

		System.out.println("Found filling: " + found);

		return truck;*/
	}

	private static char[] getShapeSet(ShapeSet shapeSet) {
		char[] chars = new char[3];

		if (shapeSet == ShapeSet.ABC) {
			chars[0] = 'A';
			chars[1] = 'B';
			chars[2] = 'C';
		} else if (shapeSet == ShapeSet.PLT) {
			chars[0] = 'P';
			chars[1] = 'L';
			chars[2] = 'T';
		} else {
			System.out.println("pls just pick 0 or 1 :'(");
			//start();
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

	private static int i = 0;
	private static int incr = 10000000;

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


			tempGenerator.updateGrid(truck);
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
			for (int mutation = 0; mutation < ShapeDatabase.data[shapeID].length; mutation++) {

				int[][][] pieceToPlace = ShapeDatabase.data[shapeID][mutation];

				int emptyPadding = 0;
				for (int i = 0; i < pieceToPlace[0][0].length; i++) {
					if (pieceToPlace[0][0][i] == 0) { // ? no clue what to do with padding
						emptyPadding++;
					} else {
						break;
					}
				}

				//System.out.println(wid + ":" + len + ":" + hgt + ":" + c + ":" + mutation);
				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (canAddPiece(truck, pieceToPlace, wid, len, hgt - emptyPadding, x_size, y_size, z_size)) {
					addPiece(truck, pieceToPlace, shapeID, wid, len, hgt - emptyPadding);
					added1.add(shapeID);
					added2.add(mutation);

					// Recur with the next column.
					if (recursiveSearch(truck, shapes, wid, len, hgt + 1, x_size, y_size, z_size)) {
						return true;
					}

					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the truck state.
					addPiece(truck, pieceToPlace, EMPTY, wid, len, hgt - emptyPadding);
					added1.remove(added1.size() - 1);
					added2.remove(added2.size() - 1);
				}
			}
		}
		// If the shape cannot be placed, return false.
		return false;
	}

	private static void addPiece(int[][][] truck, int[][][] piece, int pieceID, int x, int y, int z) {
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

	private static boolean canAddPiece(int[][][] truck, int[][][] piece, int x, int y, int z, int x_size, int y_size, int z_size) {
		// Checks whether it fits the board
		if (x < 0 || x + piece.length > x_size || y < 0 || y + piece[0].length > y_size || z < 0
				|| z + piece[0][0].length > z_size) {
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
		//search(ShapeSet.ABC, X_SIZE, Y_SIZE, Z_SIZE);

		System.out.println("Done!");
		return;
	}

}
