package TetrisControllers;

import Constants.TetrisConstants;

public class BoardController {
    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    
    // x and Y coordinates of the cells of the pentomino that's currently falling down 
    private int[] xCoords = new int[5];
    private int[] yCoords = new int[5];

    private boolean timeToSpawn = true;

    public BoardController() {
        boardValues = new int[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
    }

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }

    public void tick() {
        if (timeToSpawn) {
            spawnPiece();
            timeToSpawn = false;
        } else {
            boolean locked = moveDown();
            if (locked) {
                timeToSpawn = true;
            }
        }

    }

    // Returns true if the piece cannot move down anymore
    public boolean moveDown() {
        //...
        return false;
    } 

    public void spawnPiece() {
        //...
    }
    
}
