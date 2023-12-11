import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Timer;

import Constants.TetrisConstants;
import Heuretics.MaxHeight;
import Heuretics.MeanColumnHeight;
import Heuretics.TotalBlocks;
import Phase1.Search;
import TetrisControllers.BoardController;
import TetrisControllers.PlayerInput;
import TetrisControllers.ScoreController;
import TetrisControllers.SearchBot;
import TetrisGUI.BoardUI;
import TetrisGUI.MainUIFrame;
import TetrisGUI.ScoreUI;
import TetrisGUI.TetrisSurface;

public class Experiments {
    public static void main(String[] args) {
        // playerType 0 is for the human player, other values are for bots
        // Heuretics definition
        searchBot = new SearchBot(2);
        //Ty_SimpleHeuretics(searchBot);
        Ty_BetterStrategy(searchBot);
        
        

        // Initializing controllers
        scoreController = new ScoreController();
        boardController = new BoardController(scoreController, searchBot);

        PlayerInput playerInput = new PlayerInput(boardController);

        // Initializing UI components
        TetrisSurface tetrisSurface = new TetrisSurface(boardController);
        BoardUI boardUI = new BoardUI(tetrisSurface);
        ScoreUI scoreUI = new ScoreUI(scoreController);
        mainFrame = new MainUIFrame(boardUI, scoreUI, boardController, scoreController, playerInput, searchBot, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                startRunning();
            }
        });

        mainFrame.playerSelectionChanged(2);

        isRunning = true;
        while(true) {
            loopIteration();
        }

        // Main game loop
        /*timer = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                loopIteration();
            }
        });

        startRunning();*/
    }

    private static Timer timer;
    private static boolean isRunning = false;
    private static MainUIFrame mainFrame;
    private static BoardController boardController;
    private static SearchBot searchBot;
    private static ScoreController scoreController;

    public static void startRunning() {
        if (!isRunning) {
            //isRunning = true;
            //timer.start();
        }        
    }

    /*public static void stopRunning() {
        if (isRunning) {
            isRunning = false;
            timer.stop();
        }
    }*/

    private static void loopIteration() {
        searchBot.produceInput();
        isRunning = boardController.tick();
        mainFrame.updateAndDisplay();

        if (!isRunning) {
            try (FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/experimentResults.txt", true)) {
		        fileWriter.write("" + scoreController.getCurrentScore() + '\n');
            }
            catch(IOException excep) {
            }
            mainFrame.resetGame();
        }
    }

    private static void Ty_SimpleStrategy(SearchBot searchBot) {
        searchBot.addHeuretic(new MaxHeight(), 1);
    }

    private static void Ty_BetterStrategy(SearchBot searchBot) {
        searchBot.addHeuretic(new TotalBlocks(), -100);
        searchBot.addHeuretic(new MaxHeight(), 1);
    }
}
