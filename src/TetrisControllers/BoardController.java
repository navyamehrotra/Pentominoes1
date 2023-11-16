package TetrisControllers;

import Constants.TetrisConstants;
import Phase1.PentominoDatabase;

public class BoardController {
    // The values of the array correspond to ids at cells of the board
    private int[][] boardValues;
    
    // x and Y coordinates of the cells of the pentomino that's currently falling down 
    private int[] xCoords = new int[5];
    private int[] yCoords = new int[5];

    private boolean timeToSpawn = true;

    public BoardController() {
        boardValues = new int[TetrisConstants.BOARD_HEIGHT][TetrisConstants.BOARD_WIDTH];
        
    }

    public int getIDInCell(int row, int col) {
        return boardValues[row][col];
    }

    public void tick() {
        if (timeToSpawn) {
            spawnPiece();
            timeToSpawn = false;
        } else {
            boolean locked = moveDown();
            if (locked) {
                timeToSpawn = true;
            }
        }

    }

    // Returns true if the piece cannot move down anymore
    public boolean moveDown() {
        

        for (int i = 0; i < yCoords.length; i++) {
            yCoords[i]++; // Increment y coordinate to move down
        }
        boolean lock = canMoveDown();
        return lock;
    } 

    public boolean canMoveDown(){
        for (int i = 0; i < yCoords.length; i++) {
            if (yCoords[i] >= TetrisConstants.BOARD_HEIGHT - 1) {
                // The piece has reached the bottom
                return false;
            }
        }
         // Check collision with existing pieces in the boardValues array
    for (int i = 0; i < xCoords.length; i++) {
        int x = xCoords[i];
        int y = yCoords[i];

        // Check if the position below the current piece is occupied
        if (y < TetrisConstants.BOARD_HEIGHT - 1 && boardValues[y + 1][x] != 0) {
            // Collision detected
            return false;
        }
    }

        return true;
    }

   public static boolean checkLine(int[][] a){
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
   public static int [][] deleteline(int[][]a){
    
    if(checkLine(a)){
        for (int i = 0; i < a.length; i++) {
            a[i][a[0].length] = 0;
        }
    }
    
    return a;
   }

    public void spawnPiece() {
        int pentominoID = (int)(Math.random() * 12); //0-11
        int rotationID = (int)(Math.random() * PentominoDatabase.data[pentominoID].length); //0-x

        int[][] randomPentomino = PentominoDatabase.data[pentominoID][rotationID];
        // put randomPentomino into boardValues
        
        for (int i = 0; i < randomPentomino.length; i++) { // x value
            if(randomPentomino[i][0] == 1){
                
                boardValues[i] = randomPentomino[i];
            }
            for (int j = 0; j < randomPentomino[0].length; j++) { // y values
                if(randomPentomino[j][0] == 1){
                boardValues[j] = randomPentomino[j];
            }
            }
        }
        
  
        // put the coords into xCoords and yCoords
    
        int index = 0;
    for (int row = 0; row < randomPentomino.length; row++) {
        for (int col = 0; col < randomPentomino[row].length; col++) {
            if (randomPentomino[row][col] != 0) {
                xCoords[index] = col; // Assigning x coordinate
                yCoords[index] = row; // Assigning y coordinate
                index++;
            }
        }
    }
    }

    

    
    
}
