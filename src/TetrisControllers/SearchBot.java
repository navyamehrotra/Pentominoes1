package TetrisControllers;

import java.util.ArrayList;

import Constants.TetrisConstants;
import Heuretics.Heuretic;

public class SearchBot {
    private BoardController testBoardController;
    private ArrayList<Heuretic> heuretics;
    private ArrayList<Double> heureticWeights;

    // The result of a search is saved here, to be translated to input later
    private int[] bestXCoords;
    private int[] bestYCoords;

    public SearchBot() {
        heuretics = new ArrayList<>();
        heureticWeights = new ArrayList<>();
    }

    public void clearHeuretics() {
        heuretics.clear();
        heureticWeights.clear();
    }

    public void addHeuretic(Heuretic heuretic, double weight) {
        heuretics.add(heuretic);
        heureticWeights.add(weight);
    }

    /**
     * Heuretics based search for the best move possible.
     * @param boardController - values used for the search. It's assumed that there's a piece currently falling down.
     * @param searchDepth - an integer >= 0. The larger, the more moves ahead are considered.
     */
    public void pickTheBestMove(BoardController boardController, int searchDepth) {
        testBoardController = new BoardController(boardController, new ScoreController());
        pickTheBestMove(searchDepth, true);
    }

    /**
     * Heuretics based search for the best move possible. 
     * It's assumed that testBoardController is already set and it has a piece falling down.
     * Keep in mind that after this method is executed, the piece that was falling down will have been removed.
     * @param searchDepth - an integer >= 0. The larger, the more moves ahead are considered.
     * @param baseCall - if true, the best move will be saved, for later use.
     * @return - the calculated score of the best move. The higher, the better.
     */
    private double pickTheBestMove(int searchDepth, boolean baseCall) {
        // Get all of the possible placement positions for the falling piece
        ArrayList<int[]> possiblePlacementsX = new ArrayList<>();        
        ArrayList<int[]> possiblePlacementsY = new ArrayList<>();
        PlacementGenerator.getAllPossiblePlacements(testBoardController, possiblePlacementsX, possiblePlacementsY);
        testBoardController.removeCurrentPiece();

        // Judge all of the possible placement positions
        Double bestScore = null;
        for (int i = 0; i < possiblePlacementsX.size(); i++) {
            testBoardController.spawnPiece(possiblePlacementsX.get(i), possiblePlacementsY.get(i));

            Double score = null;
            if (searchDepth == 0) {
                score = judgeABoard(testBoardController);  
            } else {
                score = worstScenarioScore(searchDepth);
            }

            if (score > bestScore) {
                bestScore = score;

                if (baseCall) {
                    bestXCoords = possiblePlacementsX.get(i);
                    bestYCoords = possiblePlacementsY.get(i);
                }
            }

            testBoardController.removeCurrentPiece();
        }

        return bestScore;
    }

    /**
     * Heuretics based search, that assumes the worst possible scenario (worst possible piece.) 
     * It's assumed that testBoardController is already set and there's no piece currently falling down.
     * 
     * It's crucial that even though the worst possible piece is assumed, 
     * we also assume that the best possible move with it will be made.
     * @param searchDepth - an integer >= 0. The larger, the more moves ahead are considered.
     * @return - the calculated score of the worst scenario. The higher, the better.
     */
    private double worstScenarioScore(int searchDepth) {
        Double worstScore = null;
        for (int i = 0; i < TetrisConstants.NUMBER_OF_PENTOMINOS; i++) {
            Double score = null;

            if (testBoardController.canSpawnPiece(i, 0)) {
                testBoardController.spawnPiece(i, 0);
                score = pickTheBestMove(searchDepth - 1, false);
            } else {
                score = 0.0;
            }

            if (worstScore == null || score < worstScore) {
                worstScore = score;
            }
        }

        return worstScore;
    }

    /**
     * Gets a score of a board, based on the bot's list of heuretics.
     * For now the score is a sum of linear expressions, which probably isn't ideal.
     * @param boardController - the boardController with the board to be judged.
     * @return - the calculated score. The higher, the better.
     */
    private double judgeABoard(BoardController boardController) {
        double score = 0;
        for (int i = 0; i < heuretics.size(); i++) {
            score += heuretics.get(i).getScore(boardController) * heureticWeights.get(i);
        }
        return score;
    }
    
}
