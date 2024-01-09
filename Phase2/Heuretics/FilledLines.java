package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculate the number of completely filled lines
* we want to maximize this value
*/ 
public class FilledLines implements Heuretic {

    public double getScore(BoardController boardController) {
        int count = 0;
        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            boolean emptySpace = false;
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                if (boardController.getIDInCell(y, x) == 0) {
                    emptySpace = true;
                }
            }
            if (emptySpace == false)
                count++;
        }
        return count;
    }
}
