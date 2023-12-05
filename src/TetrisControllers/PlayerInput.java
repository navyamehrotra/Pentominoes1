package TetrisControllers;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.*;

import TetrisGUI.MainUIFrame;

public class PlayerInput {
    private MainUIFrame mainUIFrame;
    private BoardController boardController;

    public PlayerInput(BoardController boardController, MainUIFrame mainUIFrame) {
        this.boardController = boardController;
        this.mainUIFrame = mainUIFrame;

        registerInputs();
    }

    public void registerInputs() {
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(e -> {
            keyPressed(e);
            return true;
        });

    }
    
    public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        boardController.rotatePentomino();
                        mainUIFrame.updateAndDisplay();

                        break;
                    case KeyEvent.VK_DOWN:
                        boardController.move(0, 1);
                        mainUIFrame.updateAndDisplay();

                        break;
                    case KeyEvent.VK_LEFT:
                        boardController.move(-1, 0);
                        mainUIFrame.updateAndDisplay();

                        break;
                    case KeyEvent.VK_RIGHT:
                        boardController.move(1, 0);
                        mainUIFrame.updateAndDisplay();

                        break;
                    case KeyEvent.VK_SPACE:
                        boardController.movePentominoHardDown();
                        mainUIFrame.updateAndDisplay();

                }
            }

    
}
