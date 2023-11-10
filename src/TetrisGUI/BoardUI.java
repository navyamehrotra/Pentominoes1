package TetrisGUI;

import javax.swing.*;
import java.awt.*;

public class BoardUI {
    private TetrisSurface tetrisSurface;

    public BoardUI(TetrisSurface tetrisSurface) {
        this.tetrisSurface = tetrisSurface;
        // ...
    }

    public JPanel update() {
        JPanel surface = new JPanel();
        surface.setLayout(new GridLayout(15, 5, 2, 2));

        for (int row = 0; row < 15; row++) {
            for (int column = 0; column < 5; column++) {
                surface.add(tetrisSurface.getCell(row, column));
            }
        }

        return surface;
    }

}
