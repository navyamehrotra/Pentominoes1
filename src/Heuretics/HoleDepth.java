package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculates the mean depth of the holes present
* we want to maximize this value
*/
public class HoleDepth implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int sum = 0;
        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                if (boardController.getIDInCell(y, x) == 0) {
                    int XCord = x; 
                    int YCord = y;
                    if ((boardController.getIDInCell(YCord + 1, XCord) != 0) && (YCord < TetrisConstants.BOARD_HEIGHT)) {
                        YCord++;
                        if ((boardController.getIDInCell(YCord + 1, XCord) == 0) && (YCord < TetrisConstants.BOARD_HEIGHT)) {
                        sum += y;
                        }
                    }
                }
            }
        }
        return sum;
    }
}
