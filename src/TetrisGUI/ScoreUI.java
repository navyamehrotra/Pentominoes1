package TetrisGUI;
import TetrisControllers.ScoreController;
import javax.swing.*;

public class ScoreUI {

    private ScoreController scoreController;

    public ScoreUI(ScoreController scoreController) {
        this.scoreController = scoreController;
        //...
    }

    public JPanel update() {
        //...
        int currentScore = scoreController.getCurrentScore();
        int highestScore = scoreController.getHighestScore(); 

        return null;
    }
    
}
