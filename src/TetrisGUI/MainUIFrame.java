package TetrisGUI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainUIFrame {
 
    private BoardUI boardUI;
    private ScoreUI scoreUI;

    public MainUIFrame(BoardUI boardUI, ScoreUI scoreUI) {
        this.boardUI = boardUI;
        this.scoreUI = scoreUI;
        String[] colNames = {"name", "score"};
        String[][] data = {{"Aukje", "5000"}, {"Ata", "3500"}, {"Ty", "3000"}, {"Nathan", "2600"}, {"Anatoly", "2300"}};

        JFrame frame = new JFrame();
        JTable table = new JTable(data, colNames);

        JButton play = new JButton("Play");

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the action you want when the button is clicked
                System.out.println("Play button clicked");
            }
        });

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon image = new ImageIcon("C:\\Users\\V\\Desktop\\Project 1-1\\project-1-1\\codetest\\dalle3.png");
                Image img = image.getImage();
                //Image scaledImage = img.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
                g.drawImage(img, 0, 0, this);
            }
        };

        frame.setLayout(new BorderLayout());

        frame.setSize(500, 700);
        play.setSize(30, 10);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.add(play, BorderLayout.SOUTH);

        frame.add(imagePanel, BorderLayout.CENTER);
        frame.add(tablePanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateAndDisplay() {
        JPanel tetrisBoard = boardUI.update();
        JPanel highScore = scoreUI.update();
        //...
    }
}
