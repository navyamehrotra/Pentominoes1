package TetrisGUI;

import java.util.Arrays;

import javax.swing.*;

import TetrisControllers.BoardController;
import TetrisControllers.PlayerInput;
import TetrisControllers.ScoreController;
import TetrisControllers.SearchBot;

import java.awt.*;
import java.awt.event.*;


public class MainUIFrame {

    public boolean startRunning = false;

    private BoardUI boardUI;
    private ScoreUI scoreUI;

    private JFrame frame;
    private JPanel tetrisBoard;

    private final int RIGHT_PANEL_PREFERRED_WIDTH = 100;
    private final int RIGHT_PANEL_MARGINS = 7;

    private boolean markedForUpdate;
    private PlayerInput playerInput;
    private SearchBot searchBot;

    public MainUIFrame(BoardUI boardUI, ScoreUI scoreUI, BoardController boardController, ScoreController scoreController, PlayerInput playerInput, SearchBot searchBot) {
        this.boardUI = boardUI;
        this.scoreUI = scoreUI;
        this.playerInput = playerInput;
        this.searchBot = searchBot;
        playerInput.Init(this);

        // Setup the frame
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() 
        {  
            public void componentResized(ComponentEvent evt) {
                frameSizeChanged(true);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setSize(new Dimension(560, 570));

        // Create the buttons
        JButton reset = new JButton("Reset");

        JComboBox<String> playerChoice = new JComboBox<String>();
        playerChoice.addItem("Human");        
        playerChoice.addItem("Bot (main) - 1 depth");
        playerChoice.addItem("Bot - 2 depth");
        playerChoice.addItem("Bot - 3 depth");

        JButton play = new JButton("Play");
        JButton quit = new JButton("Quit");

        playerChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                playerSelectionChanged(playerChoice.getSelectedIndex());
            }
        });
        playerSelectionChanged(0);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the action you want when the button is clicked
                startRunning = true;
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardController.reset();
                scoreController.resetScore();
                updateAndDisplay();
                startRunning = true;
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // The left panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        frame.add(leftPanel);

        // The tetris board
        tetrisBoard = boardUI.update();
        frame.add(tetrisBoard);
       
        // The right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(RIGHT_PANEL_MARGINS ,RIGHT_PANEL_MARGINS ,RIGHT_PANEL_MARGINS ,RIGHT_PANEL_MARGINS);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;

        rightPanel.add(reset, gbc);    

        // Empty panel that fills space
        gbc.weighty = 1;
        rightPanel.add(new JPanel(), gbc);   
        gbc.weighty = 0;

        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        rightPanel.add(playerChoice, gbc);
        rightPanel.add(play, gbc);
        rightPanel.add(scoreUI.update(), gbc);
        rightPanel.add(quit, gbc);

        frame.add(rightPanel);

        // Resize elements of the right panel to the same width
        for (Component c : rightPanel.getComponents()) {
            c.setPreferredSize(new Dimension(RIGHT_PANEL_PREFERRED_WIDTH, c.getPreferredSize().height));
        }

        // 
        rightPanel.setPreferredSize(new Dimension(0, 0));
        leftPanel.setPreferredSize(new Dimension(0, 0));
        frame.setVisible(true);
        frameSizeChanged(true);
    }

    public void playerSelectionChanged(int i) {
        if (i == 0) {
            playerInput.setEnabled(true);
            searchBot.setEnabled(false);
        } else {
            playerInput.setEnabled(false);
            searchBot.setEnabled(true);
            searchBot.setSearchDepth(i - 1);
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void markForUpdate() {
        markedForUpdate = true;
    }

    public boolean isMarkedForUpdate() {
        return markedForUpdate;
    }

    public void updateAndDisplay() {

        // Replace the tetris board with an updated one
        int index = Arrays.asList(frame.getContentPane().getComponents()).indexOf((Component)tetrisBoard);
        frame.remove(tetrisBoard);
        tetrisBoard = boardUI.update();
        frame.add(tetrisBoard, index);

        frameSizeChanged(false);


        scoreUI.update();

        frame.revalidate();
        frame.repaint();

        markedForUpdate = false;
    }

    private void frameSizeChanged(boolean forceSize) {
        int heightToWidth = Constants.TetrisConstants.BOARD_HEIGHT / Constants.TetrisConstants.BOARD_WIDTH;

        if (tetrisBoard != null) {
            tetrisBoard.setPreferredSize(new Dimension(frame.getHeight() / heightToWidth, frame.getHeight()));    
            
            if (forceSize) {
                tetrisBoard.setSize(new Dimension(frame.getHeight() / heightToWidth, frame.getHeight()));                        
            }
        }
    }
}
