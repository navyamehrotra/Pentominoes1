/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */
 import java.util.Scanner;
 import java.util.Random;
 import java.util.ArrayList;

/**
 * This class includes the methods to support the search of a solution.
 */
public class Search
{
	public static final Scanner scan = new Scanner(System.in);

	public static final int horizontalGridSize = horizontalSize();

	public static int horizontalSize() {
		System.out.print("Please enter horizontal grid size: ");
		int horizontalGridSize = Integer.parseInt(scan.nextLine());
		return horizontalGridSize;
	}
    
	public static final int verticalGridSize = verticalSize();

	public static int verticalSize() {
		System.out.print("Please enter vertical grid size: ");
		int verticalGridSize = Integer.parseInt(scan.nextLine());
		return verticalGridSize;
	}

    public static final char[] inputsPossible = { 'X', 'I', 'Z', 'T', 'U', 'W', 'V', 'Y', 'L', 'P', 'N', 'F' };
	public static final char[] input = getPentominoInput();

	public static char[] getPentominoInput() {
		System.out.println("Input a list of Pentominoes to be used (all in one line:)");
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
    
    //Static UI class to display the board
    public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

	/**
	 * Helper function which starts a basic search algorithm
	 */
    public static void search()
    {
        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];

        for(int i = 0; i < field.length; i++)
        {
            for(int j = 0; j < field[i].length; j++)
            {
                // -1 in the state matrix corresponds to empty square
                // Any positive number identifies the ID of the pentomino
            	field[i][j] = -1;
            }
        }
        //Start the basic search
        basicSearch(field);
    }
	
	/**
	 * Get as input the character representation of a pentomino and translate it into its corresponding numerical value (ID)
	 * @param character a character representating a pentomino
	 * @return	the corresponding ID (numerical value)
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
	 * Basic implementation of a search algorithm. It is not a bruto force algorithms (it does not check all the posssible combinations)
	 * but randomly takes possible combinations and positions to find a possible solution.
	 * The solution is not necessarily the most efficient one
	 * This algorithm can be very time-consuming
	 * @param field a matrix representing the board to be fulfilled with pentominoes
	 */
    private static void basicSearch(int[][] field){
    	Random random = new Random();
    	boolean solutionFound = false;
    	
    	while (!solutionFound) {
    		solutionFound = false;
    		
    		//Empty board again to find a solution
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					field[i][j] = -1;
				}
			}
    		
    		//Put all pentominoes with random rotation/flipping on a random position on the board
    		for (int i = 0; i < input.length; i++) {
    			
    			//Choose a pentomino and randomly rotate/flip it
    			int pentID = characterToID(input[i]);
    			int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
    			int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
    		
    			//Randomly generate a position to put the pentomino on the board
    			int x;
    			int y;
    			if (horizontalGridSize < pieceToPlace.length) {
    				//this particular rotation of the piece is too long for the field
    				x=-1;
    			} else if (horizontalGridSize == pieceToPlace.length) {
    				//this particular rotation of the piece fits perfectly into the width of the field
    				x = 0;
    			} else {
    				//there are multiple possibilities where to place the piece without leaving the field
    				x = random.nextInt(horizontalGridSize-pieceToPlace.length+1);
    			}

    			if (verticalGridSize < pieceToPlace[0].length) {
    				//this particular rotation of the piece is too high for the field
    				y=-1;
    			} else if (verticalGridSize == pieceToPlace[0].length) {
    				//this particular rotation of the piece fits perfectly into the height of the field
    				y = 0;
    			} else {
    				//there are multiple possibilities where to place the piece without leaving the field
    				y = random.nextInt(verticalGridSize-pieceToPlace[0].length+1);
    			}
    		
    			//If there is a possibility to place the piece on the field, do it
    			if (x >= 0 && y >= 0 && canAddPiece(field, pieceToPlace, pentID, x, y)) {
	    			addPiece(field, pieceToPlace, pentID, x, y);
	    		} 
    		}

    		//Check whether complete field is filled
			// While boolean solutionFound is true ->
    		// Iterate over each possible position in x and y axis - >
    		// If position in the field is equal to -1, the space is empty
    		// If the field has a empty space, solution is not found. 
    		// Loop repeats.
    		//
			for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					if (field[i][j] == -1) {
						solutionFound = false;
					}
				}
			}

			ui.setState(field); 

    		if (solutionFound) {
    			//display the field
    			ui.setState(field); 
    			System.out.println("Solution found");
    			break;
    		}
    	}
    }

/**
	 * Recursive function to try and fill the grid.
	 *
	 * @param grid Current grid state.
	 * @param row  Current row to attempt to place a piece.
	 * @param col  Current column to attempt to place a piece.
	 * @return true if the grid can be filled, false otherwise.
	 */
	public static boolean fillGrid(int[][] grid, int row, int col, int[][] database) {
		// If 'row' is equal to GRID_SIZE, the grid is filled successfully
		if (row == horizontalGridSize && col == verticalGridSize) {
			return true;
		} 
		
		int[][] tempDatabase = new int[PentominoDatabase.data.length - 1][];
		System.arraycopy(PentominoDatabase.data, 1, database, 0, database.length - 1);

		if(row == horizontalGridSize){
			fillGrid(grid, row + 1, col,tempDatabase);
		}
		
		// If the current cell is not EMPTY, move to the next column
		if (grid[row][col] != EMPTY) {
			return fillGrid(grid, row, col + 1, tempDatabase);
		}

		// Loop through each possible shapes
		for (int j = 0; j <= 11; j++) {
			for (int i = 0; i <= 7; i++) {
				int pentID = characterToID(input[j]);
				int mutation = i;
				int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

				if (canPlacePentomino(grid, row, col, pieceToPlace)) {

					// If yes, place the L-shape and print the grid state
					placePentomino(grid, row, col, pieceToPlace, FILLED);
					System.out.println("Trying to place at: (" + row + ", " + col + ")");
					printGrid(grid);

					// Recur with the next column
					if (fillGrid(grid, row, col + 1, tempDatabase)) {
						return true;
					}

					// If placing the L-shape did not lead to a solution, remove it (backtrack) and
					// print the grid state
					placePentomino(grid, row, col, pieceToPlace, EMPTY);
					System.out.println("Backtracking from: (" + row + ", " + col + ")");
					printGrid(grid);
				}
			}
		}
		// If no L-shapes can be placed, return false
		return false;
	}

	// printing the possible outcomes since the UI doesnt wanna work??
	static void printGrid(int[][] grid) {
		// Loop through each row
		for (int[] row : grid) {
			// Loop through each column in the row
			for (int cell : row) {
				// Print "X" if FILLED, "." if EMPTY
				System.out.print((cell == EMPTY ? "." : "X") + " ");
			}
			// Move to the next line after printing all columns in a row
			System.out.println();
		}
		// Print an empty line to separate grid states
		System.out.println();
	}

    
	/**
	 * Adds a pentomino to the position on the field (overriding current board at that position)
	 * @param field a matrix representing the board to be fulfilled with pentominoes
	 * @param piece a matrix representing the pentomino to be placed in the board
	 * @param pieceID ID of the relevant pentomino
	 * @param x x position of the pentomino
	 * @param y y position of the pentomino
	 */
    public static void addPiece(int[][] field, int[][] piece, int pieceID, int x, int y)
    {
        for(int i = 0; i < piece.length; i++) // loop over x position of pentomino
        {
            for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
            {
                if (piece[i][j] == 1)
                {
                    // Add the ID of the pentomino to the board if the pentomino occupies this square
                    field[x + i][y + j] = pieceID;
                }
            }
        }
    }

public static boolean canAddPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {

	for (int i = 0; i < piece.length; i++) // loop over x position of pentomino
	{
		for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
		{
			if (piece[i][j] == 1 && field[x + i][y + j] != -1)
				return false;
		}
	}
	return true;
}

	/**
	 * Main function. Needs to be executed to start the basic search algorithm
	 */
    public static void main(String[] args)
    {
        search();
		
    }
}
