package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculate the bumpiness by summing up 
* the absolute value of the difference between two adjacent columns
*/
public class Bumpiness implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int[] rowHeight = new int[TetrisConstants.BOARD_WIDTH];
        for (int i = 0; i < rowHeight.length; i++) {
            rowHeight[i] = 0;
        }
        for (int x = 0; x < rowHeight.length; x++) {
            for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++){
                if (boardController.getIDInCell(y, x) == 1) {
                    rowHeight[x]++;
                }
                else if ((x != 0) && (y != 0) && (x != TetrisConstants.BOARD_WIDTH - 1) && (y != TetrisConstants.BOARD_HEIGHT - 1)) {
                    rowHeight[x]++;
                }
            }

        }
        
        int sum = 0;
        for (int i = 0; i < rowHeight.length; i++) {
            sum += Math.abs(rowHeight[i] - rowHeight[i + 1]);
        }

        return sum;
    }
}
