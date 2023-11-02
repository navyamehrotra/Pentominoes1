package TetrisGUI;

import javax.swing.JPanel;

public class MainUIFrame {
 
    private BoardUI boardUI;
    private ScoreUI scoreUI;

    public MainUIFrame(BoardUI boardUI, ScoreUI scoreUI) {
        this.boardUI = boardUI;
        this.scoreUI = scoreUI;
        //...
    }

    public void updateAndDisplay() {
        JPanel tetrisBoard = boardUI.update();
        JPanel highScore = scoreUI.update();
        //...
    }
}
