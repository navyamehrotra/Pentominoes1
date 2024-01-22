
// Adaptation of Phase 1 code to work in 3D 
import java.util.Timer;

import javax.swing.JLabel;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Knapsacker1 {
	public final int EMPTY = -1; // How empty truck cells are defined.
	private final double skipScalar = 0.95;

	public boolean dead = false;

	private Scene3DGenerator generator;
	private Utility.ShapeSet shapeSet;
	private int[] scores;
	private int sizeX, sizeY, sizeZ;

	private int[] bestScoreForNumber;
	private int bestScore;
	private JLabel output;

	private char[] shapes;
	private long startTime;

	public Knapsacker1(Scene3DGenerator generator, JLabel output, Utility.ShapeSet shapeSet, int[] scores, int sizeX, int sizeY, int sizeZ) {
		startTime = System.currentTimeMillis();
		this.generator = generator;
		this.shapeSet = shapeSet;
		this.scores = scores;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.output = output;

		shapes = Utility.getShapeSet(shapeSet);

		bestScoreForNumber = new int[sizeX * sizeY * sizeZ];
		bestScore = 0;

		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				search();
			}
		});  
		t1.start();
	}

	public int[][][] search() {
		int[][][] truck = new int[sizeX][sizeY][sizeZ];

		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				for (int k = 0; k < sizeZ; k++) {
					truck[i][j][k] = EMPTY;
				}
			}
		}

		recursiveSearch(truck, 0, 0, 0, 0, 0);

		if (!dead) {
			output.setText("Best score found: " + bestScore + " - elapsed " + (System.currentTimeMillis() - startTime) + "ms");
		}

		return truck;
	}

	private void recursiveSearch(int[][][] truck,
		int wid, int len, int hgt, 
		int score, int numOfPieces) {
		if (dead) {
			return;
		}

		// If 'row' is equal to the truck height, the truck is filled successfully.
		// Note that this is because the program starts filling from top-left.
		if (wid == sizeX) {
			return;
		}

		// Jump one up by one once the entire slice is filled
		if (len == sizeY) {
			recursiveSearch(truck, wid + 1, 0, 0, score, numOfPieces);
			return;
		}

		// Jump to the next row once the current row is filled.
		if (hgt == sizeZ) {
			recursiveSearch(truck, wid, len + 1, 0, score, numOfPieces);
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
			if (!dead) {
				generator.updateGrid(truck);
				output.setText("Best score so far: " + bestScore + "(Still searching...)" + " - elapsed " + (System.currentTimeMillis() - startTime) + "ms");
			}
		}

		// If the current cell is not EMPTY, move to the next column.
		if (truck[wid][len][hgt] != EMPTY) {
			recursiveSearch(truck, wid, len, hgt + 1, score, numOfPieces);
			return;
		}

		// Loop through each possible shapes.
		for (int d = 0; d < 2; d++) {
			int c = 0;
			if (shapeSet == Utility.ShapeSet.PLT) {
				if (d == 0) c = 2;
				if (d == 1) c = 1;
				if (d == 2) c = 0;
			}
			else if (shapeSet == Utility.ShapeSet.ABC) {
				if (d == 0) c = 2;
				if (d == 1) c = 0;
				if (d == 2) c = 1;
			}

			int shapeID = Utility.characterToID(shapes[c]);
			// Loop through each possible mutation for that shape.
			for (int mutation = 0; mutation < DatabaseGenerator.getDatabase()[shapeID].length; mutation++) {
				if (dead) {
					return;
				}

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
				if (Utility.canAddPiece(truck, pieceToPlace, EMPTY, wid, len - yPadding, hgt - zPadding, sizeX, sizeY, sizeZ)) {
					Utility.addPiece(truck, pieceToPlace, shapeID, wid, len - yPadding, hgt - zPadding);

					// Recur with the next column.
					recursiveSearch(truck, wid, len, hgt + 1, score + scores[c], numOfPieces + 1);
					if (dead) {
						return;
					}
					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the truck state.
					Utility.addPiece(truck, pieceToPlace, EMPTY, wid, len - yPadding, hgt - zPadding);
				}
			}
		}

		recursiveSearch(truck, wid, len, hgt + 1, score, numOfPieces);
	}
}
