package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*  
* calculate the sum of the height of each column
* we want to minimize this value
* does not keep holes in mind
*/ 
public class MeanColumnHeight implements Heuretic {
    public double getScore(BoardController boardController) {
        double sum = 0.0;
        int[] columnHeight = new int[TetrisConstants.BOARD_WIDTH];
        for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
                if (boardController.getIDInCell(y, x) != 0) {
                    columnHeight[x]++;
                }
            }
        }
        for (int i = 0; i < columnHeight.length; i++) {
            sum += columnHeight[i];
        }

        double meanHeight = sum / TetrisConstants.BOARD_WIDTH;
        return meanHeight;
    }
}
