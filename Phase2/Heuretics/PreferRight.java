package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

public class PreferRight implements Heuretic {

    public double getScore(BoardController boardController) {
        //int minY = 15;

        int score = 0;

        for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
                if (boardController.getIDInCell(y, x) != 0) {
                    score += Math.pow(16, TetrisConstants.BOARD_WIDTH - x);
                }
            }
        }

        return score / (16 * Math.pow(16, TetrisConstants.BOARD_WIDTH));
    }
}
