package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

public class WeightedHeight implements Heuretic {
    public double getScore(BoardController boardController) {
        //int minY = 15;

        int score = 0;

        for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
                if (boardController.getIDInCell(y, x) != 0) {
                    score += Math.pow(6, TetrisConstants.BOARD_HEIGHT - y);
                }
            }
        }

        return score / (6 * Math.pow(6, TetrisConstants.BOARD_HEIGHT));
    }
}
