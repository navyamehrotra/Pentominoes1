package TetrisGUI;

import javax.swing.*;
import javax.swing.border.Border;

import Phase1.PentominoBuilder;

import java.awt.*;

public class BoardUI extends PentominoBuilder{
    private TetrisSurface tetrisSurface;

    public BoardUI(TetrisSurface tetrisSurface) {
        this.tetrisSurface = tetrisSurface;
        // ...
        getData();
    }

    public JPanel update() {
        int gridGap = 2;

        JPanel surface = new JPanel();

        Border border = BorderFactory.createEmptyBorder(1, 1, 1, 1);
        surface.setBorder(border);
        surface.setLayout(new GridLayout(Constants.TetrisConstants.BOARD_HEIGHT, Constants.TetrisConstants.BOARD_WIDTH, gridGap, gridGap));

        for (int row = 0; row < Constants.TetrisConstants.BOARD_HEIGHT; row++) {
            for (int column = 0; column < Constants.TetrisConstants.BOARD_WIDTH; column++) {
                surface.add(tetrisSurface.getCell(row, column));
            }
        }

        return surface;
    }
    

}
