package TetrisControllers;

import Constants.TetrisConstants;
import Phase1.PentominoDatabase;

public class BoardController {
    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    private ScoreController scoreController;
    
    // x and Y coordinates of the cells of the pentomino that's currently falling down 
    private TetrisBlock[] blocks = new TetrisBlock[TetrisConstants.PIECE_SIZE];
    private TetrisBlock centerBlock;
    
    public BoardController(BoardController template, ScoreController scoreController) {
        this(scoreController);

        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                boardValues[y][x] = template.getIDInCell(y, x);
            } 
        }

        for (int i = 0; i < 5; i++) {
            blocks[i] = new TetrisBlock(template.getXCoord(i), template.getYCoord(i));
        }
        centerBlock = new TetrisBlock(template.centerBlock.getX(), template.centerBlock.getY()) ;
    }

    public BoardController(ScoreController scoreController) {
        this.scoreController = scoreController;
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
            working = spawnPiece();
        }

        return working;
    }
    
    public static boolean isLineFull(int[][] a){
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if(a[i][a[0].length]==1){
                count++;
            }
        }

        if(count == a.length){
            return true;
        }
        
        return false;
    }

    public static int[][] clearLine(int[][]a){
        if(isLineFull(a)){
            for (int i = 0; i < a.length; i++) {
                a[i][a[0].length] = 0;
            }
        }
        
        return a;
    }


    public boolean spawnPiece() {
        // Get a random pentomino with random rotation
        int pentominoID = (int)(Math.random() * PentominoDatabase.data.length);
        int rotationID = (int)(Math.random() * PentominoDatabase.data[pentominoID].length);
        int[][] randomPentomino = PentominoDatabase.data[pentominoID][rotationID];

        if (!canSpawnPiece(pentominoID, rotationID)) {
            return false;
        }

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
                    blocks[squareInd].moveTo(startX + j, startY + i);
                    squareInd++;
                }
            }
        }

        // Get center
        int centerX = getCenter(blocks, 0);
        int centerY = getCenter(blocks, 1);

        centerBlock = new TetrisBlock(centerX, centerY); // Change this
        return true;
    }

    private int getCenter(TetrisBlock[] blocks, int variable) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            min = Math.min(variable == 0 ? blocks[i].getX() : blocks[i].getY(), min);
            max = Math.max(variable == 0 ? blocks[i].getX() : blocks[i].getY(), max);
        }

        return (int)Math.round((min + max) / 2.0);
    }

    
    public boolean rotatePentomino(int direction) {
        int id = boardValues[blocks[0].getY()][blocks[0].getX()];
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[blocks[i].getY()][blocks[i].getX()] = 0;
        }

        TetrisBlock[] newBlocks = new TetrisBlock[TetrisConstants.PIECE_SIZE];
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int oldX = blocks[i].getX();
            int oldY = blocks[i].getY();
    
            // Rotating 90 degrees clockwise
            int newX = centerBlock.getX() + (oldY - centerBlock.getY());
            int newY = centerBlock.getY() - (oldX - centerBlock.getX());

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
        // This can be done by checking the new coordinates against the game board boundaries
        // and shifting the piece accordingly.
    }


    public boolean move(int xDelta, int yDelta) {
        if (canMove(xDelta, yDelta)) {
            int id = boardValues[blocks[0].getY()][blocks[0].getX()];
            // Clear current positions
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                boardValues[blocks[i].getY()][blocks[i].getX()] = 0;
            }
    
            // Update coordinates
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                blocks[i].moveTo(blocks[i].getX() + xDelta, blocks[i].getY() + yDelta);
            }
            centerBlock.moveTo(centerBlock.getX() + xDelta, centerBlock.getY() + yDelta);
    
            // Place piece in new position
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[blocks[i].getY()][blocks[i].getX()] = id;
            }
        }
        else {
            return false;
        }

        return true;
    }

    public boolean canMove(int xDelta, int yDelta) {
        if (centerBlock == null) {
            return false;
        }

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int newX = blocks[i].getX() + xDelta;
            int newY = blocks[i].getY()+ yDelta;
    
            // Check board boundaries
            if (newX < 0 || newX >= TetrisConstants.BOARD_WIDTH || newY < 0 || newY >= TetrisConstants.BOARD_HEIGHT) {
                return false;
            }
    
            // Check collision with other pieces
            if (boardValues[newY][newX] != 0) {
                // Exclude collision detection with itself
                boolean occupiedByItself = false;
                for (int j = 0; j < TetrisConstants.PIECE_SIZE; j++) {
                    if (i != j && blocks[j].getX() == newX && blocks[j].getY() == newY) {
                        occupiedByItself = true;
                        break;
                    }
                }
                if (!occupiedByItself) return false;
            }
        }
        return true;
    }
    public void movePentominoHardDown() {
        // Continue moving the piece down until it can no longer move
        while (move(0,1)) {
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

    public void spawnPiece(int pentominoID, int rotationID) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void spawnPiece(int[] xCoords, int[] yCoords) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void removeCurrentPiece() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void checkAndClearLines() {
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

    // moves stuff down
    private void clearLine(int row) {
        for (int r = row; r > 0; r--) {
            for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
                boardValues[r][col] = boardValues[r - 1][col];
            }
        }
    
        for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
            boardValues[0][col] = 0;
        }

        scoreController.addPoint();
    }
}
