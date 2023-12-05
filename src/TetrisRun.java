import Constants.TetrisConstants;
import TetrisControllers.BoardController;
import TetrisControllers.PlayerInput;
import TetrisControllers.ScoreController;
import TetrisGUI.BoardUI;
import TetrisGUI.MainUIFrame;
import TetrisGUI.ScoreUI;
import TetrisGUI.TetrisSurface;

public class TetrisRun {
    public static void main(String[] args) {
        // playerType 0 is for the human player, other values are for bots
        int playerType = 0;

        // Initializing controllers
        ScoreController scoreController = new ScoreController();
        BoardController boardController = new BoardController(scoreController);

        
        
        // Initializing UI components
        TetrisSurface tetrisSurface = new TetrisSurface(boardController);
        BoardUI boardUI = new BoardUI(tetrisSurface);
        ScoreUI scoreUI = new ScoreUI(scoreController);
        MainUIFrame mainFrame = new MainUIFrame(boardUI, scoreUI);

        if (playerType == 0) {
            PlayerInput playerInput = new PlayerInput(boardController, mainFrame);
        } else {
            //...
        }

        // Main game loop
        double previousTick = System.currentTimeMillis();
        boolean isRunning = true;
        
        while(isRunning) {
            if (System.currentTimeMillis() - previousTick >= TetrisConstants.TICK_DELTA) {
                boardController.tick();
                mainFrame.updateAndDisplay();
                previousTick += TetrisConstants.TICK_DELTA;
            }
        }
    }
    
}
