package TetrisGUI;

import TetrisControllers.BoardController;
import javax.swing.*;
import java.awt.*;

public class TetrisSurface extends JPanel {
    private BoardController boardController;
    public TetrisSurface(BoardController boardController) {
        this.boardController = boardController;
    }

    public JPanel getCell(int row, int column) {
        int id = boardController.getIDInCell(row, column);
        JPanel cell = new JPanel();
        Color cur = new Color(0, 0, 0);

        if (id == 0) {
            cur = Color.BLACK;
        } else if (id == 1) {
            cur = Color.ORANGE;
        } else if (id == 2) {
            cur = Color.CYAN;
        } else if (id == 3) {
            cur = Color.GREEN;
        } else if (id == 4) {
            cur = Color.MAGENTA;
        } else if (id == 5) {
            cur = Color.PINK;
        } else if (id == 6) {
            cur = Color.RED;
        } else if (id == 7) {
            cur = Color.YELLOW;
        } else if (id == 8) {
            cur = Color.BLUE;
        } else if (id == 9) {
            cur = new Color(0, 0, 100);
        } else if (id == 10) {
            cur = new Color(100, 0, 0);
        } else {
            cur = Color.LIGHT_GRAY;
        }

        cell.setBackground(cur);

        return cell;
    }

}
