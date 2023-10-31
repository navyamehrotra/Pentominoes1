package TetrisControllers;

import Constants.TetrisConstants;

public class BoardController {
    private int[][] boardValues;

    public BoardController() {
        boardValues = new int[TetrisConstants.BORD_HEIGHT][TetrisConstants.BOARD_WIDTH];
    }

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }
}
