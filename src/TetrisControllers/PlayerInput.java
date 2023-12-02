package TetrisControllers;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.*;

public class PlayerInput {

    private BoardController boardController;

    public PlayerInput(BoardController boardController) {
        this.boardController = boardController;

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
                        break;
                    case KeyEvent.VK_DOWN:
                        boardController.move(0, 1);
                        break;
                    case KeyEvent.VK_LEFT:
                        boardController.move(-1, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        boardController.move(1, 0);
                        break;
                    case KeyEvent.VK_SPACE:
                        boardController.movePentominoHardDown();
                }
            }

    
}
