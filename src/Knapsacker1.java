
// Adaptation of the Phase 1 knapsacker to 3D
import java.util.*;

public class Knapsacker1 {
	public static final Scanner scan = new Scanner(System.in);

	public static final int EMPTY = -1; // How empty grid cells are defined.

	public static final double xSize = 16.5;
	public static final double ySize = 2.5;
	public static final double zSize = 4.0;

	// 0 for parcels A, B, C; 1 for pentomino parcels
	public static int shapeSet;

	// 0 for possibility mode; 1 for maximum-value-seeking mode
	public static int mode;

	public void fit(Shape3D[] shapes, double xSize, double ySize, double zSize) {
		// Fit
	}

	public static MainUIFrame ui;

	public static boolean recursiveSearch(int[][][] grid) {
		return recursiveSearch(grid, 0, 0, 0);
	}

	public static int n = 11; // Has to be changed; this was for back when we had a set amount of shapes

	public static boolean recursiveSearch(int[][][] grid, int row, int col, int hgt) {
		if (row == ySize && col == 0 && hgt == zSize) {
			n--;
			return n <= 0; 
		}

		if (col == xSize && row != ySize) {
			return recursiveSearch(grid, row + 1, 0, hgt);
		}

		if (row == ySize && col == xSize) {
			return recursiveSearch(grid, 0, 0, hgt + 1);
		}

		if (grid[col][row][hgt] != EMPTY) {
			return recursiveSearch(grid, row, col + 1, hgt);
		}

	// THIS WHOLE SECTION NEEDS TO BE REWORKED!!
	// 	for (int c = 0; c < parcels.length; c++) {
	// 		int pentID = characterToID(parcels[c]);
	// 		for (int mutation = 0; mutation < parcelDatabase.data[pentID].length; mutation++) {

	// 			int[][][] pieceToPlace = parcelDatabase.data[pentID][mutation];

	// 			int emptyPadding = 0;
	// 			for (int i = 0; i < pieceToPlace.length; i++) {
	// 				if (pieceToPlace[i][0] == 0) {
	// 					emptyPadding++;
	// 				} else {
	// 					break;
	// 				}
	// 			}

	// 			// If there is a possibility to place the piece on the field, making sure it
	// 			// does not overlap with any other pieces already place, place it.
	// 			if (canAddPiece(grid, pieceToPlace, col - emptyPadding, row, hgt)) {
	// 				addPiece(grid, pieceToPlace, pentID, col - emptyPadding, row, hgt);

	// 				// Copies the array of parcels, excluding the one just used by the program
	// 				// as to ensure each shape is only used once.
	// 				char[] remainingPents = new char[parcels.length - 1];
	// 				int destPos = 0;
	// 				for (int i = 0; i < parcels.length; i++) {
	// 					if (i != c) {
	// 						remainingPents[destPos++] = parcels[i];
	// 					}
	// 				}

	// 				// Recur with the next column.
	// 				if (recursiveSearch(grid, row, col + 1, remainingPents)) {
	// 					return true;
	// 				}

	// 				// If placing the pentomino did not lead to a solution, remove it (backtrack)
	// 				// and print the grid state.
	// 				addPiece(grid, pieceToPlace, EMPTY, col - emptyPadding, row);
	// 			}
	// 		}
	// 	}
	// 	// If the shape cannot be placed, return false.
	// 	return false;
	// }

	public static void addPiece(int[][][] field, int[][][] piece, int pieceID, int x, int y, int z) {
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				for (int k = 0; k < piece[i][j].length; k++) {
					if (piece[i][j][k] == 1) {
						field[x + i][y + j][z + k] = pieceID;
					}
				}
			}
		}
	}

	public static boolean canAddPiece(int[][][] field, int[][][] piece, int x, int y, int z) {

		if (x < 0 || x + piece.length > xSize || y < 0 || y + piece[0].length > ySize || z < 0
				|| z + piece[0][0].length > zSize) {
			return false;
		}

	
		for (int i = 0; i < piece.length; i++) {
			for (int j = 0; j < piece[i].length; j++) {
				for (int k = 0; k < piece[i][j].length; k++) {
					if (piece[i][j][k] == 1 && field[x + i][y + j][z + k] != -1) {
						return false;
					}
				}
			}
		}
		return true;
	}
}