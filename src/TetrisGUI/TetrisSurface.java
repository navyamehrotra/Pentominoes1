package TetrisGUI;

import TetrisControllers.BoardController;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Phase1.PentominoBuilder;
import Phase1.PentominoDatabase;
import Phase1.UI;

public class TetrisSurface extends JPanel {
    private BoardController boardController;
    private UI ui = new UI(5, 15, 50);

    public TetrisSurface(BoardController boardController, UI ui) {
        this.boardController = boardController;
        this.ui = ui;
    }

    public Object getCell(int row, int column) {
        int id = boardController.getIDInCell(row, column);

        // ...

        return null;
    }

}
