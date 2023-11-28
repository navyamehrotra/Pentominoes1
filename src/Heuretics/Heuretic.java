package Heuretics;

import TetrisControllers.BoardController;

public interface Heuretic {
    public double getScore(BoardController boardController);
}
