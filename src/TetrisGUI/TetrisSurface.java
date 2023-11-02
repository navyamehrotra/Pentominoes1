package TetrisGUI;

import TetrisControllers.BoardController;

public class TetrisSurface {
    private BoardController boardController;

    public TetrisSurface(BoardController boardController) {
        this.boardController = boardController;
    }

    public Object getCell(int row, int column) {
        int id = boardController.getIDInCell(row, column);

        //...

        return null;
    }

    
}
