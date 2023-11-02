package TetrisGUI;
import TetrisControllers.ScoreController;
import Constants.TetrisConstants;

import javax.swing.*;
import java.awt.*; 
import javax.swing.border.*;

public class ScoreUI {

    private ScoreController scoreController;
    private JFrame debugFrame;
    private JLabel currentScoreVal, highScoreVal;
    private JPanel panel;

    // Just for debugging purposes
    public static void main(String[] args) {
        ScoreController scoreController = new ScoreController();
        ScoreUI scoreUI = new ScoreUI(scoreController, true, 20);

        double previousTick = System.currentTimeMillis();

        while (true) {
            if (System.currentTimeMillis() - previousTick >= TetrisConstants.TICK_DELTA) {
                scoreController.addPoint();
                scoreUI.update();
                previousTick += TetrisConstants.TICK_DELTA;
            }
        }
    }

    // Constructors
    public ScoreUI(ScoreController scoreController) {
        this(scoreController, false, null);
    }

    public ScoreUI(ScoreController scoreController, Integer fontSize) {
        this(scoreController, false, fontSize);
    }

    public ScoreUI(ScoreController scoreController, boolean debugMode, Integer fontSize) {
        this.scoreController = scoreController;

        // Debug mode - lets you inspect the UI without the rest of the game
        if (debugMode) {
            debugFrame = new JFrame("debug frame");
            debugFrame.setSize(300, 300);
            debugFrame.setVisible(true);
        }
        
        // Create and stylize the panel
        Border border = new CompoundBorder(
            BorderFactory.createEtchedBorder(Color.red, Color.gray), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new GridLayout(0, 1));
        panel.setBackground(Color.black);

        // Add the text labels
        JLabel highScoreLabel = new JLabel("High score");
        highScoreVal = new JLabel("0");
        JLabel currentScoreLabel = new JLabel("Score");
        currentScoreVal = new JLabel("0");

        currentScoreLabel.setVerticalAlignment(JLabel.BOTTOM);
        currentScoreVal.setVerticalAlignment(JLabel.TOP);
        highScoreLabel.setVerticalAlignment(JLabel.BOTTOM);
        highScoreVal.setVerticalAlignment(JLabel.TOP);

        panel.add(highScoreLabel);
        panel.add(highScoreVal);
        panel.add(currentScoreLabel);        
        panel.add(currentScoreVal);
        
        // Change the font
        Font font = javax.swing.UIManager.getDefaults().getFont("Label.font");
        if (fontSize != null) {
            font = new Font(font.getName(), font.getStyle(), fontSize);
        }

        for(Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel)component;
                label.setFont(font);
                label.setForeground(Color.white);
            }
        }

        //
        if (debugMode) {
            debugFrame.add(panel);
        }
    }

    //
    public JPanel update() {
        int currentScore = scoreController.getCurrentScore();
        int highestScore = scoreController.getHighestScore(); 

        currentScoreVal.setText(String.valueOf(currentScore));
        highScoreVal.setText(String.valueOf(highestScore));     

        return panel;
    }
}
