import Constants.TetrisConstants;
import Heuretics.MaxHeight;
import Heuretics.MeanColumnHeight;
import Heuretics.TotalBlocks;
import TetrisControllers.BoardController;
import TetrisControllers.PlayerInput;
import TetrisControllers.ScoreController;
import TetrisControllers.SearchBot;
import TetrisGUI.BoardUI;
import TetrisGUI.MainUIFrame;
import TetrisGUI.ScoreUI;
import TetrisGUI.TetrisSurface;

public class TetrisRun {


    public static void main(String[] args) {
        // playerType 0 is for the human player, other values are for bots
        SearchBot searchBot = null;
        
        // Heuretics definition
        searchBot = new SearchBot(2);
        //Ty_SimpleHeuretics(searchBot);
        Ty_BetterStrategy(searchBot);


        // Initializing controllers
        ScoreController scoreController = new ScoreController();
        BoardController boardController = new BoardController(scoreController, searchBot);

        PlayerInput playerInput = new PlayerInput(boardController);

        // Initializing UI components
        TetrisSurface tetrisSurface = new TetrisSurface(boardController);
        BoardUI boardUI = new BoardUI(tetrisSurface);
        ScoreUI scoreUI = new ScoreUI(scoreController);
        MainUIFrame mainFrame = new MainUIFrame(boardUI, scoreUI, boardController, scoreController, playerInput, searchBot);


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
                    searchBot.produceInput();
                    isRunning = boardController.tick();

                    mainFrame.markForUpdate();
                    //previousTick += TetrisConstants.TICK_DELTA;
                    previousTick = System.currentTimeMillis();
                }

                if (mainFrame.isMarkedForUpdate()) {
                    mainFrame.updateAndDisplay();
                }
                mainFrame.startRunning = false;
            }
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
