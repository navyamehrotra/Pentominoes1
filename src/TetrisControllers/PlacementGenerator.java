package TetrisControllers;

import java.lang.reflect.Array;
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
            ArrayList<int[]> resultYCoords,
            ArrayList<Integer[]> resultMoveRecipes) throws UnsupportedOperationException {

        int startRotation = 0;
        int startX = boardController.getCenterBlock().getX();        
        int startY = boardController.getCenterBlock().getY();

        boolean[][][] canReach = new boolean[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH][4];       
        boolean[][][] canEnd = new boolean[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH][4];
        ArrayList<Integer> moves = new ArrayList<>();

        explore(startX, startY, startRotation, canReach, canEnd, boardController, resultXCoords, resultYCoords, resultMoveRecipes, moves);
        //System.out.println(resultXCoords.size());
    }

    public static void explore(int currentX, int currentY, int rotation, 
    boolean[][][] canReach, boolean[][][] canEnd,
    BoardController boardController, 
    ArrayList<int[]> resultXCoords, ArrayList<int[]> resultYCoords, ArrayList<Integer[]> resultMoveRecipes,
    ArrayList<Integer> moves) {

        canReach[currentY][currentX][rotation] = true;

        if (!boardController.canMove(0, 1)) {
            int[] xCoords = new int[5];
            int[] yCoords = new int[5];

            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                xCoords[i] = boardController.getXCoord(i);                
                yCoords[i] = boardController.getYCoord(i);
            }

            resultXCoords.add(xCoords);
            resultYCoords.add(yCoords);


            Integer[] recipeCopy = new Integer[moves.size()];
            for (int i = 0; i < moves.size(); i++) {
                recipeCopy[i] = moves.get(i);
            }
            resultMoveRecipes.add(recipeCopy);
        }

        // Movement all directions
        int[] xDeltas = {0, 1, -1};
        int[] yDeltas = {1, 0, 0};

        for (int i = 0; i < xDeltas.length; i++) {
            int xDelta = xDeltas[i];
            int yDelta = yDeltas[i];
            int newX = currentX + xDelta;
            int newY = currentY + yDelta;

            if (newX >= 0 && newX < TetrisConstants.BOARD_WIDTH 
            && newY >= 0 && newY < TetrisConstants.BOARD_HEIGHT) {
                if (!canReach[newY][newX][rotation]) {
                    if (boardController.move(xDelta, yDelta)) {
                        moves.add(i + 1);
                        explore(newX, newY, rotation, canReach, canEnd, boardController, resultXCoords, resultYCoords, resultMoveRecipes, moves);
                        boardController.move(-xDelta, -yDelta);
                        moves.remove(moves.size() - 1);
                    }
                }
            }
        }

        // Rotation clockwise
        if (!canReach[currentY][currentX][(rotation + 1) % 4]) {
            if (boardController.rotatePentomino(1)) {
                moves.add(0);
                explore(currentX, currentY, (rotation + 1) % 4, canReach, canEnd, boardController, resultXCoords, resultYCoords, resultMoveRecipes, moves);
                boardController.rotatePentomino(-1);
                moves.remove(moves.size() - 1);
            }
        }

    }
    
}
