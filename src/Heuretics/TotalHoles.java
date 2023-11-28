package Heuretics;

import Constants.TetrisConstants;
import TetrisControllers.BoardController;

/*
* calculate the number of holes in the board
* a empty space is considered a hole when there is a filled cell above it 
* we want to mimimize this value
*/
public class TotalHoles implements Heuretic {
    
    public double getScore(BoardController boardController) {
        int count = 0;
        int XCord = 0;
        int YCord = 0;

        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                if (boardController.getIDInCell(y, x) == 0) {
                    XCord = x; 
                    YCord = y;
                    while ((boardController.getIDInCell(YCord + 1, XCord) != 0) && (YCord < TetrisConstants.BOARD_HEIGHT)) {
                        YCord++;
                        if ((boardController.getIDInCell(YCord + 1, XCord) == 0) && (YCord < TetrisConstants.BOARD_HEIGHT)) {
                            count++;
                        }
                    }
                    
                }
            }
        }
        return count;
    }
}
