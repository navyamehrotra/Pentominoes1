package TetrisControllers;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.*;

public class PlayerInput {

    public PlayerInput() {
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
                        //boardController.rotatePentomino();
                        break;
                    /*case KeyEvent.VK_DOWN:
                        boardController.movePentominoDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        boardController.movePentominoLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        boardController.movePentominoRight();
                        break;
                    case Keyevent.VK_SPACE:
                        boardController.H*/
                }
            }

    
}
