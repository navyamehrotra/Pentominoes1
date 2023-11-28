package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculate total number of blocks present
* we want to minimize this number
*/
public class TotalBlocks implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int count = 0;
        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                if (boardController.getIDInCell(y, x) == 1) {
                    count++;
                }
            }
        }
        return count;
    }
}
