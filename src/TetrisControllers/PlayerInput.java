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
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    boardController.rotatePentomino(1);
                    mainUIFrame.markForUpdate();

                    break;
                case KeyEvent.VK_LEFT:
                    boardController.move(-1, 0);
                    mainUIFrame.markForUpdate();

                    break;
                case KeyEvent.VK_RIGHT:
                    boardController.move(1, 0);
                    mainUIFrame.markForUpdate();

                    break;
                case KeyEvent.VK_DOWN:
                    boardController.movePentominoHardDown();
                    mainUIFrame.markForUpdate();
            }
        }
                
    }
}