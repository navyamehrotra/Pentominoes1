package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

public class MaxHeight implements Heuretic {

    public double getScore(BoardController boardController) {
        int minY = 15;

        for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
                if (boardController.getIDInCell(y, x) != 0) {
                    minY = Math.min(minY, y);
                }
            }
        }

        return minY;
    }
    
}
