package TetrisControllers;

import java.util.ArrayList;

import Constants.TetrisConstants;
import Heuretics.Heuretic;

public class SearchBot {
    private BoardController testBoardController;
    private ArrayList<Heuretic> heuretics;
    private ArrayList<Double> heureticWeights;

    // The result of a search is saved here, to be translated to input later
    public int[] bestXCoords;
    public int[] bestYCoords;
    public Integer[] bestRecipe;
    public BoardController mainBoardController;


    private int indInRecipe = 0;
    private int searchDepth;
    private boolean enabled;

    public SearchBot(int searchDepth) {
        heuretics = new ArrayList<>();
        heureticWeights = new ArrayList<>();
        this.searchDepth = searchDepth;
    }

    public void Init(BoardController mainBoardController) {
        this.mainBoardController = mainBoardController;
    }

    public void clearHeuretics() {
        heuretics.clear();
        heureticWeights.clear();
    }

    public void addHeuretic(Heuretic heuretic, double weight) {
        heuretics.add(heuretic);
        heureticWeights.add(weight);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSearchDepth(int depth) {
        searchDepth = depth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void produceInput() {
        if (!enabled) {
            return;
        }

        while (bestRecipe != null && indInRecipe < bestRecipe.length && indInRecipe >= 0 && bestRecipe[indInRecipe] != 1) {
            switch (bestRecipe[indInRecipe]) {
                case 0:
                    mainBoardController.rotatePentomino(1);
                    break;
                case 2:
                    mainBoardController.move(1, 0);
                    break;
                case 3:
                    mainBoardController.move(-1, 0);
                    break;
            }

            indInRecipe++;
        }

        indInRecipe++;
    }

    /**
     * Heuretics based search for the best move possible.
     * @param boardController - values used for the search. It's assumed that there's a piece currently falling down.
     * @param searchDepth - an integer >= 0. The larger, the more moves ahead are considered.
     */
    public void pickTheBestMove(BoardController boardController) {
        if (enabled) {
            testBoardController = new BoardController(boardController, null, null);
            pickTheBestMove(searchDepth, true);
            indInRecipe = 0;
        }
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
        ArrayList<Integer[]> possiblePlacementsRecipes = new ArrayList<>();

        PlacementGenerator.getAllPossiblePlacements(testBoardController, possiblePlacementsX, possiblePlacementsY, possiblePlacementsRecipes);
        testBoardController.removeCurrentPiece();

        // Judge all of the possible placement positions
        Double bestScore = null;
        for (int i = 0; i < possiblePlacementsX.size(); i++) {
            int[][] copy = testBoardController.getCopyOfValues();
            testBoardController.spawnPiece(possiblePlacementsX.get(i), possiblePlacementsY.get(i), 1);
            testBoardController.checkAndClearLines();

            Double score = null;
            if (searchDepth == 0) {
                score = judgeABoard(testBoardController);  
            } else {
                score = worstScenarioScore(searchDepth);
            }

            if (bestScore == null || score > bestScore) {
                bestScore = score;

                if (baseCall) {
                    bestXCoords = possiblePlacementsX.get(i);
                    bestYCoords = possiblePlacementsY.get(i);
                    bestRecipe = possiblePlacementsRecipes.get(i);
                }
            }

            testBoardController.setValues(copy);
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
