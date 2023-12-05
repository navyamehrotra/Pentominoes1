package TetrisControllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import Constants.TetrisConstants;

public class PlacementGenerator {

    /**
     * Calculates all possible places where the piece that's currently falling down can end up (be placed in,) 
     * after it has fallen all the way down.
     * This includes all of the possible rotations, and all of the placements that might need advanced maneuvering,
     * like going around obstacles.
     * @param boardController - the controller with the data.
     * @param resultXCoords - should be filled with x values of all of the possible placements.
     * @param resultYCoords - should be filled with y values of all of the possible placements.
     * @throws UnsupportedOperationException
     */    


    public static void getAllPossiblePlacements(
            BoardController boardController,
            ArrayList<int[]> resultXCoords, 
            ArrayList<int[]> resultYCoords) throws UnsupportedOperationException {

        int startRotation = 0;
        int startX = boardController.getCenterBlock().getX();        
        int startY = boardController.getCenterBlock().getY();

        boolean[][][] canReach = new boolean[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH][4];       
        boolean[][][] canEnd = new boolean[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH][4];

        explore(startX, startY, startRotation, canReach, canEnd);
    }

    public static void explore(int currentX, int currentY, int rotation, 
    boolean[][][] canReach, boolean[][][] canEnd) {




    }
    
}
