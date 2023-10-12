
/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */
import java.util.*;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Search {
	public static final Scanner scan = new Scanner(System.in);

	public static final int EMPTY = -1; // How empty grid cells are defined.

	public static final int horizontalGridSize = horizontalSize();
	public static final int verticalGridSize = verticalSize();

	// Asks and stores user choice for the height of the grid to be filled.
	public static int horizontalSize() {
		System.out.print("Please enter horizontal grid size: ");
		int horizontalGridSize = Integer.parseInt(scan.nextLine());
		return horizontalGridSize;
	}

	// Asks and stores user choice for the width of the grid to be filled.
	public static int verticalSize() {
		System.out.print("Please enter vertical grid size: ");
		int verticalGridSize = Integer.parseInt(scan.nextLine());
		return verticalGridSize;
	}

	public static final char[] inputsPossible = { 'X', 'I', 'Z', 'T', 'U', 'W', 'V', 'Y', 'L', 'P', 'N', 'F' };
	public static final char[] input = getPentominoInput();

	/**
	 * Asks and stores user choice of pentominoes to include in solution, as an
	 * array of chars.
	 * 
	 * Ignores invalid input as long as at least one valid pentomino
	 * shape has been entered.
	 */
	public static char[] getPentominoInput() {
		System.out.println("Input a list of Pentominoes to be used (all in one line, as capital letters): ");
		String line = scan.nextLine();

		ArrayList<Character> inputList = new ArrayList<Character>();
		for (char c : inputsPossible) {
			for (char d : line.toCharArray()) {
				if (c == d) {
					inputList.add(c);
					break;
				}
			}
		}

		char[] chars = new char[inputList.size()];
		for (int i = 0; i < inputList.size(); i++) {
			chars[i] = inputList.get(i);
		}

		return chars;
	}

	// Static UI class to display the board.
	public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

	/**
	 * Helper function which starts a basic search algorithm.
	 */
	public static void search() {
		Scanner reader = new Scanner(System.in);

		// Initialize an empty board.
		int[][] field = new int[horizontalGridSize][verticalGridSize];

		// -1 in the state matrix corresponds to empty square
		// Any positive number identifies the ID of the pentomino
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				field[i][j] = -1;
			}
		}

		// Start the search, asking for user input on which algorithm to use.
		System.out.println(
				"Please select which algorithm to use.\n" + "0 for basic, 1 for recursive.\n" + "Your choice: ");
		int choice = reader.nextInt();

		if (choice == 0) {
			basicSearch(field);
		} else if (choice == 1) {
			recursiveSearch(field, input);
		}
	}

	/**
	 * Get as input the character representation of a pentomino and translate it
	 * into its corresponding numerical value (ID).
	 * 
	 * @param character a character representating a pentomino.
	 * @return the corresponding ID (numerical value).
	 */
	private static int characterToID(char character) {
		int pentID = -1;
		if (character == 'X') {
			pentID = 0;
		} else if (character == 'I') {
			pentID = 1;
		} else if (character == 'Z') {
			pentID = 2;
		} else if (character == 'T') {
			pentID = 3;
		} else if (character == 'U') {
			pentID = 4;
		} else if (character == 'V') {
			pentID = 5;
		} else if (character == 'W') {
			pentID = 6;
		} else if (character == 'Y') {
			pentID = 7;
		} else if (character == 'L') {
			pentID = 8;
		} else if (character == 'P') {
			pentID = 9;
		} else if (character == 'N') {
			pentID = 10;
		} else if (character == 'F') {
			pentID = 11;
		}
		return pentID;
	}

	/**
	 * Basic implementation of a search algorithm. It is not a brute force
	 * algorithm (it does not check all the posssible combinations)
	 * but randomly takes possible combinations and positions to find a possible
	 * solution.
	 * The solution is not necessarily the most efficient one
	 * This algorithm can be very time-consuming.
	 * 
	 * @param field a matrix representing the board to be fulfilled with pentominoes
	 */
	private static void basicSearch(int[][] field) {
		Random random = new Random();
		boolean solutionFound = false;

		while (!solutionFound) {
			solutionFound = false;

			// Empty board again to find a solution
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					field[i][j] = -1;
				}
			}

			// Put all pentominoes with random rotation/flipping on a random position on the
			// board.
			for (int i = 0; i < input.length; i++) {

				// Choose a pentomino and randomly rotate/flip it
				int pentID = characterToID(input[i]);
				int mutationCount = PentominoDatabase.data[pentID].length;
				int mutation = random.nextInt(mutationCount);
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

				// Randomly generate a position to put the pentomino on the board.
				int x;
				int y;
				if (horizontalGridSize < pieceToPlace.length) {
					// this particular rotation of the piece is too long for the field
					x = -1;
				} else if (horizontalGridSize == pieceToPlace.length) {
					// this particular rotation of the piece fits perfectly into the width of the
					// field
					x = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					x = random.nextInt(horizontalGridSize - pieceToPlace.length + 1);
				}

				if (verticalGridSize < pieceToPlace[0].length) {
					// this particular rotation of the piece is too high for the field
					y = -1;
				} else if (verticalGridSize == pieceToPlace[0].length) {
					// this particular rotation of the piece fits perfectly into the height of the
					// field
					y = 0;
				} else {
					// there are multiple possibilities where to place the piece without leaving the
					// field
					y = random.nextInt(verticalGridSize - pieceToPlace[0].length + 1);
				}

				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (x >= 0 && y >= 0 && canAddPiece(field, pieceToPlace, x, y)) {
					addPiece(field, pieceToPlace, pentID, x, y);
				}
			}

			// Check whether complete field is filled
			// While boolean solutionFound is true ->
			// Iterate over each possible position in x and y axis - >
			// If position in the field is equal to -1, the space is empty
			// If the field has a empty space, solution is not found.
			// Loop repeats.
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					if (field[i][j] == -1) {
						solutionFound = false;
					}
				}
			}

			ui.setState(field);

			if (solutionFound) {
				// display the field
				ui.setState(field);
				System.out.println("Solution found");
				break;
			}
		}
	}

	/**
	 * Recursive function that is called the very first time,
	 * which calls its namesake (method overloading).
	 * 
	 * @param grid       first initial grid; empty by default.
	 * @param inputChars an array consisting of the pentominoes to be included.
	 * @return true if the grid can be filled, false otherwise.
	 */
	public static boolean recursiveSearch(int[][] grid, char[] inputChars) {
		return recursiveSearch(grid, 0, 0, inputChars);
	}

	/**
	 * Recursive function to try and fill the grid.
	 *
	 * @param grid        current grid state
	 * @param row         current row to attempt to place a piece
	 * @param col         current column to attempt to place a piece
	 * @param pentominoes an array consisting of the pentominoes to be included
	 * @return true if the grid can be filled, false otherwise.
	 */
	public static boolean recursiveSearch(int[][] grid, int row, int col, char[] pentominoes) {
		// If 'row' is equal to the grid height, the grid is filled successfully.
		// Note that this is because the program starts filling from top-left.
		if (row == verticalGridSize && col == 0) {
			return true;
		}

		// Jump to the next row once the current row is filled.
		if (col == horizontalGridSize) {
			return recursiveSearch(grid, row + 1, 0, pentominoes);
		}

		// If the current cell is not EMPTY, move to the next column.
		if (grid[col][row] != EMPTY) {
			return recursiveSearch(grid, row, col + 1, pentominoes);
		}

		// Loop through each possible shape.
		for (int c = 0; c < pentominoes.length; c++) {
			int pentID = characterToID(pentominoes[c]);
			// Loop through each possible mutation for that shape.
			for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
				// Get the shape from the database using pentimino ID and mutation as
				// identifiers.
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

				int emptyPadding = 0;
				for (int i = 0; i < pieceToPlace.length; i++) {
					if (pieceToPlace[i][0] == 0) {
						emptyPadding++;
					} else {
						break;
					}
				}

				// If there is a possibility to place the piece on the field, making sure it
				// does not overlap with any other pieces already place, place it.
				if (canAddPiece(grid, pieceToPlace, col - emptyPadding, row)) {
					addPiece(grid, pieceToPlace, pentID, col - emptyPadding, row);

					// Copies the array of pentominoes, excluding the one just used by the program
					// as to ensure each shape is only used once.
					char[] remainingPents = new char[pentominoes.length - 1];
					int destPos = 0;
					for (int i = 0; i < pentominoes.length; i++) {
						if (i != c) {
							remainingPents[destPos++] = pentominoes[i];
						}
					}

					ui.setState(grid);

					// Recur with the next column.
					if (recursiveSearch(grid, row, col + 1, remainingPents)) {
						return true;
					}

					// If placing the pentomino did not lead to a solution, remove it (backtrack)
					// and print the grid state.
					addPiece(grid, pieceToPlace, EMPTY, col - emptyPadding, row);
				}
			}
		}
		// If the shape cannot be placed, return false.
		return false;
	}

	/**
	 * Adds a pentomino to the position on the field (overriding current board at
	 * that position).
	 * 
	 * @param field   a matrix representing the board to be fulfilled with
	 *                pentominoes
	 * @param piece   a matrix representing the pentomino to be placed in the board
	 * @param pieceID ID of the relevant pentomino
	 * @param x       x position of the pentomino
	 * @param y       y position of the pentomino
	 */
	public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
		for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
		{
			for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
			{
				if (piece[i][j] == 1) {
					// Add the ID of the pentomino to the board if the pentomino occupies this
					// square.
					field[x + i][y + j] = pieceID;
				}
			}
		}
	}

	/**
	 * Checks whether a pentomino can be added by making sure there is no other
	 * pentomino already there, as well as checking whether it fits into the grid at
	 * the position the program tries to place it.
	 * 
	 * @param field a matrix representing the current grid
	 * @param piece a matrix representing the pentomino to be placed in the board
	 * @param x     x position of pentomino
	 * @param y     y position of pentomino
	 * @return true if the placement is valid, false if otherwise
	 */
	public static boolean canAddPiece(int[][] field, int[][] piece, int x, int y) {
		// Checks whether it fits the board
		if (x < 0 || x + piece.length > horizontalGridSize || y < 0 || y + piece[0].length > verticalGridSize) {
			return false;
		}

		// Loop over x position of pentomino
		for (int i = 0; i < piece.length; i++) {
			// Loop over y position of pentomino
			for (int j = 0; j < piece[i].length; j++) {
				if (piece[i][j] == 1 && field[x + i][y + j] != -1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 */
	public static void main(String[] args) {
		search();
	}
}
