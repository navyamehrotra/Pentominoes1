package TetrisGUI;

import javax.swing.*;

public class BoardUI {
    private TetrisSurface tetrisSurface;

    public BoardUI(TetrisSurface tetrisSurface) {
        this.tetrisSurface = tetrisSurface;
        //...
    }

    public JPanel update() {
        JTable table = null;


        // Make a call for every cell
        int row = 0;
        int column = 0;
        table.setValueAt(tetrisSurface.getCell(row, column), row, column);

        return null;
    }
    
}
