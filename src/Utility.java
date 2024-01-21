public class Utility {
    public enum ShapeSet {
		ABC,
		PLT
	}

	public static char[] getShapeSet(Utility.ShapeSet shapeSet) {
		char[] chars = new char[3];

		if (shapeSet == Utility.ShapeSet.ABC) {
			chars[0] = 'A';
			chars[1] = 'B';
			chars[2] = 'C';
		} else if (shapeSet == Utility.ShapeSet.PLT) {
			chars[0] = 'P';
			chars[1] = 'L';
			chars[2] = 'T';
		} else {
			System.out.println("pls just pick 0 or 1 :'(");
			//start();
		}
		return chars;
	}

	public static int characterToID(char character) {
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

	public static boolean canAddPiece(int[][][] truck, int[][][] piece, int emptyInd, int x, int y, int z, int x_size, int y_size, int z_size) {
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
					if (piece[i][j][k] == 1 && truck[x + i][y + j][z + k] != emptyInd) {
						return false;
					}
				}
			}
		}
		return true;
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
}
