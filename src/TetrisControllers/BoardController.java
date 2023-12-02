package TetrisControllers;

import Constants.TetrisConstants;
import Phase1.PentominoDatabase;

public class BoardController {
    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    private ScoreController scoreController;
    
    // x and Y coordinates of the cells of the pentomino that's currently falling down 
    private int[] xCoords = new int[TetrisConstants.PIECE_SIZE];
    private int[] yCoords = new int[TetrisConstants.PIECE_SIZE];
    
    public BoardController(BoardController template, ScoreController scoreController) {
        this(scoreController);

        for (int y = 0; y < TetrisConstants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < TetrisConstants.BOARD_WIDTH; x++) {
                boardValues[y][x] = template.getIDInCell(y, x);
            } 
        }

        for (int i = 0; i < 5; i++) {
            xCoords[i] = template.getXCoord(i);
            yCoords[i] = template.getYCoord(i);
        }
    }

    public BoardController(ScoreController scoreController) {
        boardValues = new int[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
        this.scoreController = scoreController;
        spawnPiece();
    }

    public int getXCoord(int i) {
        return xCoords[i];
    }

    public int getYCoord(int i) {
        return yCoords[i];
    }    

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }

    public void tick() {
        boolean moved = move(0, 1);
        if (!moved) {
            checkAndClearLines();
            spawnPiece();
        }
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


    public void spawnPiece() {
        // Get a random pentomino with random rotation
        int pentominoID = (int)(Math.random() * PentominoDatabase.data.length);
        int rotationID = (int)(Math.random() * PentominoDatabase.data[pentominoID].length);
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
                    yCoords[squareInd] = startY + i;
                    xCoords[squareInd] = startX + j;
                    squareInd++;
                }
            }
        }
    }

    private int getCenter(int[] coords) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            min = Math.min(coords[i], min);
            max = Math.max(coords[i], max);
        }
        return (min + max) / 2; // Already correct
    }

    public void rotatePentomino() {
        int centerX = getCenter(xCoords);
        int centerY = getCenter(yCoords);
    
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int oldX = xCoords[i];
            int oldY = yCoords[i];
    
            // Rotating 90 degrees clockwise
            xCoords[i] = centerX + (oldY - centerY);
            yCoords[i] = centerY - (oldX - centerX);
        }
    
    }

    public boolean move(int xDelta, int yDelta) {
        if (canMove(xDelta, yDelta)) {
            int id = boardValues[yCoords[0]][xCoords[0]];
            // Clear current positions
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                boardValues[yCoords[i]][xCoords[i]] = 0;
            }
    
            // Update coordinates
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                xCoords[i] += xDelta;
                yCoords[i] += yDelta;
            }
    
            // Place piece in new position
            for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
                boardValues[yCoords[i]][xCoords[i]] = id;
            }
        }
        else {
            return false;
        }

        return true;
    }

    public boolean canMove(int xDelta, int yDelta) {
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int newX = xCoords[i] + xDelta;
            int newY = yCoords[i] + yDelta;
    
            // Check board boundaries
            if (newX < 0 || newX >= TetrisConstants.BOARD_WIDTH || newY < 0 || newY >= TetrisConstants.BOARD_HEIGHT) {
                return false;
            }
    
            // Check collision with other pieces
            if (boardValues[newY][newX] != 0) {
                // Exclude collision detection with itself
                boolean occupiedByItself = false;
                for (int j = 0; j < TetrisConstants.PIECE_SIZE; j++) {
                    if (i != j && xCoords[j] == newX && yCoords[j] == newY) {
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
    

    public boolean canSpawnPiece(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void spawnPiece(int id) throws UnsupportedOperationException {
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
                scoreController.addPoint();
            }
        }
    
        for (int col = 0; col < TetrisConstants.BOARD_WIDTH; col++) {
            boardValues[0][col] = 0;
        }
    }
}
