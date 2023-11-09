package TetrisGUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainUIFrame {

    private BoardUI boardUI;
    private ScoreUI scoreUI;

    public MainUIFrame(BoardUI boardUI, ScoreUI scoreUI) {
        this.boardUI = boardUI;
        this.scoreUI = scoreUI;

        // Faked data for testing purposes
        String[] colNames = { "name", "score" };
        String[][] data = { { "Aukje", "5000" }, { "Ata", "3500" }, { "Ty", "3000" }, { "Nathan", "2600" },
                { "Anatoly", "2300" } };
        JTable table = new JTable(data, colNames);        

        JFrame frame = new JFrame();
        JPanel rightPanel = new JPanel(new BorderLayout());

        JButton reset = new JButton("Reset");
        JButton play = new JButton("Play");
        JButton quit = new JButton("Quit");

        // For testing purposes for play button functionality
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the action you want when the button is clicked
                System.out.println("Play button clicked");
            }
        });

        frame.setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(play, BorderLayout.NORTH);
        // table to be replaced by call to ScoreUI
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(quit, BorderLayout.SOUTH);
        
        rightPanel.add(reset, BorderLayout.NORTH);
        rightPanel.add(tablePanel, BorderLayout.SOUTH);

        frame.add(boardUI.update(), BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateAndDisplay() {
        JPanel tetrisBoard = boardUI.update();
        JPanel highScore = scoreUI.update();
        // ...
    }
}
