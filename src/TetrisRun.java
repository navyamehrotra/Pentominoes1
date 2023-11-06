import Constants.TetrisConstants;
import Phase1.UI;
import TetrisControllers.BoardController;
import TetrisControllers.PlayerInput;
import TetrisControllers.ScoreController;
import TetrisGUI.BoardUI;
import TetrisGUI.MainUIFrame;
import TetrisGUI.ScoreUI;
import TetrisGUI.TetrisSurface;

public class TetrisRun {
    public static void main(String[] args) {
        // Initializing controllers
        BoardController boardController = new BoardController();
        ScoreController scoreController = new ScoreController();
        PlayerInput playerInput = new PlayerInput(boardController);
        UI ui = new UI(5, 15, 50);

        // Initializing UI components
        TetrisSurface tetrisSurface = new TetrisSurface(boardController, ui);
        BoardUI boardUI = new BoardUI(tetrisSurface);
        ScoreUI scoreUI = new ScoreUI(scoreController);
        MainUIFrame mainFrame = new MainUIFrame(boardUI, scoreUI);

        // Main game loop
        double previousTick = System.currentTimeMillis();
        boolean isRunning = true;
        // playerType 0 is for the human player, other values are for bots
        int playerType = 0;
        while(isRunning) {
            if (playerType == 0) {
                playerInput.readInput();
            } else {
                //...
            }

            if (System.currentTimeMillis() - previousTick >= TetrisConstants.TICK_DELTA) {
                boardController.tick();
                mainFrame.updateAndDisplay();
                previousTick += TetrisConstants.TICK_DELTA;
            }
        }
    }
    
}
