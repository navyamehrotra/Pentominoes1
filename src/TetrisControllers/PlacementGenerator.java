package TetrisControllers;

import java.util.ArrayList;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
}
