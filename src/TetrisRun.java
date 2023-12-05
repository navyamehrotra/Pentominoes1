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
        MainUIFrame mainFrame = new MainUIFrame(boardUI, scoreUI, boardController, scoreController);

        if (playerType == 0) {
            PlayerInput playerInput = new PlayerInput(boardController, mainFrame);
        } else {
            //...
        }


        // Main game loop
        boolean isRunning = false;
        double previousTick = System.currentTimeMillis();

        while (true) {
            if (!isRunning && mainFrame.startRunning) {
                previousTick = System.currentTimeMillis();
                isRunning = true;
                mainFrame.startRunning = false;
            }

            if(isRunning) {
                if (System.currentTimeMillis() - previousTick >= TetrisConstants.TICK_DELTA) {
                    isRunning = boardController.tick();
                    mainFrame.markForUpdate();
                    previousTick += TetrisConstants.TICK_DELTA;
                }

                if (mainFrame.isMarkedForUpdate()) {
                    mainFrame.updateAndDisplay();
                }
                mainFrame.startRunning = false;
            }
        }
        
    }
}
