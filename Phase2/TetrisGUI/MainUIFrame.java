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

    private BoardUI boardUI;
    private ScoreUI scoreUI;

    private JFrame frame;
    private JPanel tetrisBoard;

    private final int RIGHT_PANEL_PREFERRED_WIDTH = 120;
    private final int RIGHT_PANEL_MARGINS = 7;

    private PlayerInput playerInput;
    private SearchBot searchBot;
    private ActionListener startGame;
    private BoardController boardController;
    private ScoreController scoreController;

    public MainUIFrame(BoardUI boardUI, ScoreUI scoreUI, BoardController boardController, ScoreController scoreController, PlayerInput playerInput, SearchBot searchBot, ActionListener startGame) {
        this.boardUI = boardUI;
        this.scoreUI = scoreUI;
        this.playerInput = playerInput;
        this.searchBot = searchBot;
        this.startGame = startGame;
        this.boardController = boardController;
        this.scoreController = scoreController;
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
        frame.setSize(new Dimension(500, 570));


        // Create the buttons
        ImageIcon resetIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/reset.png"); 
        ImageIcon playIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/play.png");        
        ImageIcon quitIcon  = new ImageIcon(System.getProperty("user.dir") + "/src/assets/quit.png"); 
        ImageIcon tetrisIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/vertical_tetris.png");
        ImageIcon bestOrderIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/BEST ORDER MODE.png");

        double scale = (double)0.9 * RIGHT_PANEL_PREFERRED_WIDTH / bestOrderIcon.getIconWidth();
        int newWidth = (int)(bestOrderIcon.getIconWidth() * scale);
        int newHeight = (int)(scale * bestOrderIcon.getIconHeight());
        bestOrderIcon = new ImageIcon(bestOrderIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));

        tetrisIcon = new ImageIcon(tetrisIcon.getImage().getScaledInstance(80, 500 , Image.SCALE_DEFAULT));        

        JButton reset = new JButton(resetIcon);
        ImageIcon humanIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/human.png"); 
        ImageIcon botIcon = new ImageIcon(System.getProperty("user.dir") + "/src/assets/bot.png"); 
        JLabel tetris = new JLabel(tetrisIcon);

        JComboBox<String> playerChoice = new JComboBox<String>();
        playerChoice.addItem("Human");        
        playerChoice.addItem("Bot (main) - 1 depth");
        playerChoice.addItem("Bot - 2 depth");
        playerChoice.addItem("Bot - 3 depth");

        JButton play = new JButton(playIcon);
        JButton bestOrderMode = new JButton(bestOrderIcon);
        JButton quit = new JButton(quitIcon);
        JToggleButton onOffButton = new JToggleButton(humanIcon);
        onOffButton.setSelectedIcon(botIcon);

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
                startGame.actionPerformed(new ActionEvent(this, 0, null));
                boardController.startBestOrderMode(false);
            }
        });

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
                boardController.startBestOrderMode(false);
            }
        });

        bestOrderMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOffButton.setSelected(true);
                resetGame();
                playerInput.setEnabled(false);
                searchBot.setEnabled(true);
                boardController.startBestOrderMode(true);
            }
        });

        onOffButton.addActionListener(e -> {
            if (!onOffButton.isSelected()) {
                playerInput.setEnabled(true);
                searchBot.setEnabled(false);
            } else {
                playerInput.setEnabled(false);
                searchBot.setEnabled(true);
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // The left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        frame.add(leftPanel);
        leftPanel.add(tetris, BorderLayout.CENTER);

        // The tetris board
        tetrisBoard = boardUI.update();
        frame.add(tetrisBoard);
       
        // The right panel
        JPanel rightPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(RIGHT_PANEL_MARGINS, RIGHT_PANEL_MARGINS, RIGHT_PANEL_MARGINS, RIGHT_PANEL_MARGINS);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1;

        rightPanel.add(reset, gbc);    
        rightPanel.add(bestOrderMode, gbc);

        // Empty panel that fills space
        gbc.weighty = 1;
        rightPanel.add(new JPanel(), gbc);   
        gbc.weighty = 0;

        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        rightPanel.add(onOffButton, gbc);
        //rightPanel.add(playerChoice, gbc);
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

    public void resetGame() {
        boardController.reset();
        scoreController.resetScore();
        updateAndDisplay();
        startGame.actionPerformed(new ActionEvent(this, 0, null));
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

    private boolean updating = false;
    public void markForUpdate() {
        if (!updating) {
            updating = true;
            updateAndDisplay();
        }
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

        updating = false;
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
