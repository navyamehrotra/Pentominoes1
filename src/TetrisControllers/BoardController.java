package TetrisControllers;

import Constants.TetrisConstants;
import Phase1.PentominoDatabase;

public class BoardController {
    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    
    // x and Y coordinates of the cells of the pentomino that's currently falling down 
    private int[] xCoords = new int[TetrisConstants.PIECE_SIZE];
    private int[] yCoords = new int[TetrisConstants.PIECE_SIZE];
    

    public BoardController() {
        boardValues = new int[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
        spawnPiece();
    }

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }

    public void tick() {
        boolean locked = moveDown();
        if (locked) {
            spawnPiece();
        }
    }

    // Returns true if the piece cannot move down anymore
    public boolean moveDown() {
        boolean lock = canMoveDown();
        if (!lock) { 
            return true;
        }

        int id = boardValues[yCoords[0]][xCoords[0]];
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[yCoords[i]][xCoords[i]] = 0;
            yCoords[i]++;
        }

        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            boardValues[yCoords[i]][xCoords[i]] = id;
        }

        return false;
    } 

    public boolean canMoveDown() {
        // Check if the piece has reached the bottom
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            if (yCoords[i] >= TetrisConstants.BOARD_HEIGHT - 1) {
                return false;
            }
        }

        // Check collision with existing pieces in the boardValues array
        for (int i = 0; i < TetrisConstants.PIECE_SIZE; i++) {
            int x = xCoords[i];
            int y = yCoords[i];

            boolean occupiedByItself = false;
            for (int j = 0; j < TetrisConstants.PIECE_SIZE; j++) {
                if (xCoords[j] == x && yCoords[j] == y + 1) {
                    occupiedByItself = true;
                }
            }

            // Check if the position below the current piece is occupied
            if (!occupiedByItself && y < TetrisConstants.BOARD_HEIGHT - 1 && boardValues[y + 1][x] != 0) {
                return false;
            }
        }

        return true;
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

    private int getCenterX() {
        int min = 100;
        int max = -100;
        for (int i = 0; i < 5; i++) {
            min = Math.min(xCoords[i], min);            
            max = Math.max(xCoords[i], max); 
        }

        return min + max / 2;
    }

    /*public void rotatePentomino() {
        int centerX = getCenterX();
        int centerY = pentomino.getCenterY();

        for (int i = 0; i < 5; i++) {
            int newX = centerX + (yCoords[i] - centerY);
            int newY = centerY - (xCoords[i] - centerX);
            xCoords[i] = newX;
            yCoords[i] = newY;
        }
    }*/

    /*public void movePentominoDown() {
        if (isValidMove(pentomino, 0, 1)) {
            pentomino.setY(pentomino.getY() + 1);
        }
    }

    public void movePentominoLeft() {
        if (isValidMove(pentomino, -1, 0)) {
            pentomino.setX(pentomino.getX() - 1);
        }
    }

    public void movePentominoRight() {
        if (isValidMove(pentomino, 1, 0)) {
            pentomino.setX(pentomino.getX() + 1);
        }
    }

    public void movePentominoHardDown() {
        while (moveDown()) {
            pentomino.setY(pentomino.getY() + 1);
        }
    }*/
}
