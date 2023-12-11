package TetrisControllers;

import java.util.ArrayList;
import java.util.Arrays;

import Constants.TetrisConstants;
import Phase1.PentominoDatabase;

public class BoardController {

    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    private ScoreController scoreController;

    // x and Y coordinates of the cells of the pentomino that's currently falling
    // down
    private TetrisBlock[] blocks = new TetrisBlock[TetrisConstants.PIECE_SIZE];
    private TetrisBlock centerBlock;
    private SearchBot searchBot;

    public BoardController(BoardController template, ScoreController scoreController, SearchBot searchBot) {
        this(scoreController, searchBot);
        this.bestOrderInd = template.bestOrderInd;

        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                boardValues[y][x] = template.getIDInCell(y, x);
            }
        }

        for (int i = 0; i < 5; i++) {
            blocks[i] = new TetrisBlock(template.getXCoord(i), template.getYCoord(i));
        }
        centerBlock = new TetrisBlock(template.centerBlock.getX(), template.centerBlock.getY());
    }

    public BoardController(ScoreController scoreController, SearchBot searchBot) {
        this.scoreController = scoreController;
        this.searchBot = searchBot;
        if (searchBot != null) {
            searchBot.Init(this);
        }
        reset();
    }

    public void reset() {
        boardValues = new int[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            blocks[i] = new TetrisBlock(0, 0);
        }
        centerBlock = null;
    }

    public TetrisBlock getCenterBlock() {
        return centerBlock;
    }

    public int getXCoord(int i) {
        return blocks[i].getX();
    }

    public int getYCoord(int i) {
        return blocks[i].getY();
    }

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }

    public boolean tick() {
        boolean moved = move(0, 1);
        boolean working = true;
        if (!moved) {
            checkAndClearLines();
            working = spawnRandomPiece();
        }

        if (!working) {
            centerBlock = null;
        }

        return working;
    }

    public static boolean isLineFull(int[][] a) {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i][a[0].length] == 1) {
                count++;
            }
        }

        if (count == a.length) {
            return true;
        }

        return false;
    }

    public static int[][] clearLine(int[][] a) {
        if (isLineFull(a)) {
            for (int i = 0; i < a.length; i++) {
                a[i][a[0].length] = 0;
            }
        }

        return a;
    }

    public void startBestOrderMode(boolean start) {
        bestOrderMode = start;
        bestOrderInd = 0;
    }

    private boolean bestOrderMode = false;
    public int bestOrderInd = 0;
    private int[] bestOrderPentominoes = { 4, 8, 0, 7, 3, 9, 6, 11, 1, 10, 2, 5};
    private int[] bestOrderRotations = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public boolean spawnRandomPiece() {
        // Get a random pentomino with random rotation

        int pentominoID = (int) (Math.random() * PentominoDatabase.data.length);
        int rotationID = (int) (Math.random() * PentominoDatabase.data[pentominoID].length);

        if (bestOrderMode) {
            if (bestOrderInd < bestOrderPentominoes.length) {
                pentominoID = bestOrderPentominoes[bestOrderInd];
            }

            if (bestOrderInd < bestOrderRotations.length) {
                rotationID = bestOrderRotations[bestOrderInd];
            }

            bestOrderInd++;
        }

        boolean canSpawn = spawnPiece(pentominoID, rotationID);

        if (canSpawn && searchBot != null && searchBot.isEnabled()) {

            long time = System.currentTimeMillis();
            searchBot.pickTheBestMove(this);
            System.out.println(String.format("Calculation time %dms", System.currentTimeMillis() - time));
            // removeCurrentPiece();
            // spawnPiece(searchBot.bestXCoords, searchBot.bestYCoords, pentominoID);
        }

        return canSpawn;
    }

    private void initCenterPiece() {
        // Get center
        int centerX = getCenter(blocks, 0);
        int centerY = getCenter(blocks, 1);
        centerBlock = new TetrisBlock(centerX, centerY); // Change this
    }

    private int getCenter(TetrisBlock[] blocks, int variable) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            min = Math.min(variable == 0 ? blocks[i].getX() : blocks[i].getY(), min);
            max = Math.max(variable == 0 ? blocks[i].getX() : blocks[i].getY(), max);
        }

        return (int) Math.round((min + max) / 2.0);
    }

    public int[][] getCopyOfValues() {
        int[][] copy = new int[boardValues.length][boardValues[0].length];

        for (int i = 0; i < copy.length; i++) {
            copy[i] = Arrays.copyOf(boardValues[i], boardValues[i].length);
        }

        return copy;
    }

    public void setValues(int[][] values) {
        boardValues = values;
        blocks = new TetrisBlock[5];
        centerBlock = null;
    }

    public boolean rotatePentomino(int direction) {
        if (centerBlock == null) {
            return false;
        }

        int id = boardValues[blocks[0].getY()][blocks[0].getX()];

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[blocks[i].getY()][blocks[i].getX()] = 0;
        }

        TetrisBlock[] newBlocks = new TetrisBlock[TetrisConstants.PIECE_SIZE];
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int oldX = blocks[i].getX();
            int oldY = blocks[i].getY();

            // Rotating 90 degrees clockwise
            int newX = 0;
            int newY = 0;

            if (direction == -1) {
                newX = centerBlock.getX() + (oldY - centerBlock.getY());
                newY = centerBlock.getY() - (oldX - centerBlock.getX());
            } else {
                newX = centerBlock.getX() - (oldY - centerBlock.getY());
                newY = centerBlock.getY() + (oldX - centerBlock.getX());
            }

            newBlocks[i] = new TetrisBlock(newX, newY);

            if (newX < 0 || newX >= TetrisConstants.BOARD_WIDTH
                    || newY < 0 || newY >= TetrisConstants.BOARD_HEIGHT
                    || boardValues[newY][newX] != 0) {
                for (int j = 0; j < TetrisConstants.PIECE_SIZE; j++) {
                    boardValues[blocks[j].getY()][blocks[j].getX()] = id;
                }

                return false;
            }
        }

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            blocks[i] = newBlocks[i];
        }

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[blocks[i].getY()][blocks[i].getX()] = id;
        }

        return true;

        // Optional: Adjust the position if the new coordinates are out of bounds
        // This can be done by checking the new coordinates against the game board
        // boundaries
        // and shifting the piece accordingly.
    }

    public boolean move(int xDelta, int yDelta) {
        if (centerBlock == null) {
            return false;
        }

        boolean moved = move(xDelta, yDelta, blocks);
        if (moved) {
            centerBlock.moveTo(centerBlock.getX() + xDelta, centerBlock.getY() + yDelta);
        }
        return moved;
    }

    public boolean move(int xDelta, int yDelta, TetrisBlock[] tetrisBlock) {
        if (canMove(xDelta, yDelta, tetrisBlock)) {
            //int id = boardValues[tetrisBlock[0].getY()][tetrisBlock[0].getX()];
            // Clear current positions
            int[] ids = new int[tetrisBlock.length];
            for (int i = 0; i < tetrisBlock.length; i++) {
                ids[i] = boardValues[tetrisBlock[i].getY()][tetrisBlock[i].getX()];
                boardValues[tetrisBlock[i].getY()][tetrisBlock[i].getX()] = 0;
            }

            // Update coordinates
            for (int i = 0; i < tetrisBlock.length; i++) {
                tetrisBlock[i].moveTo(tetrisBlock[i].getX() + xDelta, tetrisBlock[i].getY() + yDelta);
            }

            // Place piece in new position
            for (int i = 0; i < tetrisBlock.length; i++) {
                boardValues[tetrisBlock[i].getY()][tetrisBlock[i].getX()] = ids[i];
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean canMove(int xDelta, int yDelta) {
        return canMove(xDelta, yDelta, blocks);
    }

    public boolean canMove(int xDelta, int yDelta, TetrisBlock[] tetrisBlock) {
        for (int i = 0; i < tetrisBlock.length; i++) {
            int newX = tetrisBlock[i].getX() + xDelta;
            int newY = tetrisBlock[i].getY() + yDelta;

            // Check board boundaries
            if (newX < 0 || newX >= TetrisConstants.BOARD_WIDTH || newY < 0 || newY >= TetrisConstants.BOARD_HEIGHT) {
                return false;
            }

            // Check collision with other pieces
            if (boardValues[newY][newX] != 0) {
                // Exclude collision detection with itself
                boolean occupiedByItself = false;
                for (int j = 0; j < tetrisBlock.length; j++) {
                    if (i != j && tetrisBlock[j].getX() == newX && tetrisBlock[j].getY() == newY) {
                        occupiedByItself = true;
                        break;
                    }
                }
                if (!occupiedByItself)
                    return false;
            }
        }
        return true;
    }

    public void movePentominoHardDown() {
        // Continue moving the piece down until it can no longer move
        while (move(0, 1)) {
        }
    }

    public boolean canSpawnPiece(int pentominoID, int rotationID) throws UnsupportedOperationException {
        int[][] randomPentomino = PentominoDatabase.data[pentominoID][rotationID];
        // Place the pentomino into the boardValues at the calculated position
        int startX = (TetrisConstants.BOARD_WIDTH - randomPentomino[0].length) / 2;
        int startY = 0;

        for (int i = 0; i < randomPentomino.length; i++) {
            for (int j = 0; j < randomPentomino[0].length; j++) {
                if (randomPentomino[i][j] != 0) {
                    int y = startY + i;
                    int x = startX + j;

                    if (boardValues[y][x] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean spawnPiece(int pentominoID, int rotationID) {
        if (!canSpawnPiece(pentominoID, rotationID)) {
            return false;
        }

        int[][] randomPentomino = PentominoDatabase.data[pentominoID][rotationID];

        // Place the pentomino into the boardValues at the calculated position
        int startX = (TetrisConstants.BOARD_WIDTH - randomPentomino[0].length) / 2;
        int startY = 0;

        int squareInd = 0;
        for (int i = 0; i < randomPentomino.length; i++) {
            for (int j = 0; j < randomPentomino[0].length; j++) {
                if (randomPentomino[i][j] != 0) {
                    int y = startY + i;
                    int x = startX + j;

                    boardValues[y][x] = pentominoID + 1;

                    // Put the coords into xCoords and yCoords
                    blocks[squareInd] = new TetrisBlock(startX + j, startY + i);
                    squareInd++;
                }
            }
        }

        initCenterPiece();
        return true;
    }

    public void spawnPiece(int[] xCoords, int[] yCoords, int id) {
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            blocks[i] = new TetrisBlock(xCoords[i], yCoords[i]);
            boardValues[blocks[i].getY()][blocks[i].getX()] = id;
        }

        initCenterPiece();
    }

    public void removeCurrentPiece() throws UnsupportedOperationException {
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[blocks[i].getY()][blocks[i].getX()] = 0;
            blocks[i] = null;
        }

        centerBlock = null;
    }

    public void removePiece(int[] xCoords, int[] yCoords) {
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[yCoords[i]][xCoords[i]] = 0;
            blocks[i] = null;
        }
        centerBlock = null;
    }

    public void checkAndClearLines() {
        for (int row = TetrisConstants.BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean isLineFull = true;
            for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
                if (boardValues[row][col] == 0) {
                    isLineFull = false;
                    break;
                }
            }

            if (isLineFull) {
                clearLine(row);
                row++;
            }
        }
    }

    // Clears a line by moving the shapes above it into the newly-emptied space
    // as well as making sure points are awarded for clearing lines
    private void clearLine(int row) {
        /*for (int r = row; r > 0; r--) {
            for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
                boardValues[r][col] = boardValues[r - 1][col];
            }
        }*/

        for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
            boardValues[row][col] = 0;
        }

        ArrayList<ArrayList<TetrisBlock>> groups = DFS();        
        ArrayList<TetrisBlock[]> groups2 = new ArrayList<>();

        for(ArrayList<TetrisBlock> group : groups) {
            TetrisBlock[] array = group.toArray(new TetrisBlock[0]);
            groups2.add(array);
        }

        boolean movedDown = false;
        do {
            movedDown = false;
            for (int i = 0; i < groups.size(); i++) {
                boolean moved = move(0, 1, groups2.get(i));
                if (moved) {
                    movedDown = true;
                }
            }
        } while(movedDown);
        
        if (scoreController != null) {
            scoreController.addPoint();
        }

        checkAndClearLines();
    }

    // Takes care of the advanced gravity by performing a depth-first search
    private ArrayList<ArrayList<TetrisBlock>> DFS() {
        boolean[][] checked = new boolean[TetrisConstants.BOARD_WIDTH][TetrisConstants.BOARD_HEIGHT];

        ArrayList<ArrayList<TetrisBlock>> groups = new ArrayList<>();
        for (int r = 0; r < TetrisConstants.BOARD_HEIGHT; r++) {
            for (int c = 0; c < TetrisConstants.BOARD_WIDTH; c++) {
                if (boardValues[r][c] != 0 && !checked[c][r]) {
                    ArrayList<TetrisBlock> group = new ArrayList<>();
                    explore(r, c, group, checked);
                    groups.add(group);
                }
            }
        }

        return groups;
    }

    // Locates all filled cells that together form a shape
    // that is meant to fall a specific amount of lines
    private void explore(int row, int col, ArrayList<TetrisBlock> group, boolean[][] checked) {
        group.add(new TetrisBlock(col, row));
        checked[col][row] = true;

        final int[] colDelta = { 0, 0, -1, 1 };
        final int[] rowDelta = { -1, 1, 0, 0 };

        for (int i = 0; i < colDelta.length; i++) {
            int newCol = col + colDelta[i];
            int newRow = row + rowDelta[i];

            if (newCol >= 0 && newCol < TetrisConstants.BOARD_WIDTH 
                && newRow >= 0 && newRow < TetrisConstants.BOARD_HEIGHT
                && boardValues[newRow][newCol] != 0 && !checked[newCol][newRow]) {
                explore(newRow, newCol, group, checked);
            }
        }
        
    }

}
