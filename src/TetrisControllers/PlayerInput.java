package TetrisControllers;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class PlayerInput {

    private BoardController boardController;

    public PlayerInput(BoardController boardController) {
        this.boardController = boardController;
    }

    public void readInput() {
        
    }
    public void registerInputs(JComponent component) {
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        boardController.rotatePentomino();
                        break;
                    case KeyEvent.VK_DOWN:
                        boardController.movePentominoDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        boardController.movePentominoLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        boardController.movePentominoRight();
                        break;
                    case Keyevent.VK_SPACE:
                        boardController.H
                }
            }
        });

    }

    
}
