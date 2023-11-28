package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculate sum of depth of present wells
* we want to minimize this number
*/
public class WellDepth implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int sum = 0;
        int count = 0;
        for (int x = 1; x < TetrisConstants.BOARD_WIDTH - 1; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
                while ((boardController.getIDInCell(y, x) == 0) && (boardController.getIDInCell(y, x - 1) == 1) &&
                 (boardController.getIDInCell(y, x + 1) == 1) && (boardController.getIDInCell(y + 1, x) != 1)) {
                    count++;
                    y++;
                }
            }
            sum += count;

        }
        return sum;
    }
}
