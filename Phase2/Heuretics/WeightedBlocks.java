package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* count the total number of blocks but weigh them according to the x axis
* we want to minimize this number
*/
public class WeightedBlocks implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int count = 0;
        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                if (boardController.getIDInCell(y, x) == 1) {
                    count += x;
                }
            }
        }
        return count;
    }
}
