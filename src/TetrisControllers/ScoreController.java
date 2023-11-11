package TetrisControllers;

import Constants.TetrisConstants;

public class ScoreController {
    private int currentScore;
    private int[] highScores;
    
    // Nathaneal and Aukje please fill this class in (:
    public ScoreController() {
        highScores = new int[TetrisConstants.HIGH_SCORES_KEPT];
        // Fill high scores with values from the file
    }

    public int getHighestScore() {
        return highScores[0];
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void addPoint() {
        currentScore++;
        // (...)Update highScores if it qualifies
    }


}
