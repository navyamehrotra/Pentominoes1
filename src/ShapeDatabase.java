/**
 * @author Department of Data Science and Knowledge Engineering (DKE)
 * @version 2022.0
 */

 import java.io.FileNotFoundException;
//import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class contains all the methods that you may need to start developing your project together with the representation of the pentomino's pieces
 */
public class ShapeDatabase {

    //All basic pentominoes that will be rotated and flipped
    private static int[][][][] basicDatabase;// = new int[6][][][];

    //all pentominoes, inclusive their rotations
    private static int[][][][][] data = new int[6][][][][];


    public static int[][][][][] getDatabase() {
        makeDatabase();

        return data;
    }

    private static void fillBasicDataBase() {
        // ABCLPT - in this order
        int[][][] A = new int[2][2][4];
        fillWith1(A);

        int[][][] B = new int[2][3][4];
        fillWith1(B);

        int[][][] C = new int[3][3][3];
        fillWith1(C);

        int[][][] L = new int[0][][];
        L[0] = new int[][] {
            {1, 0},
            {1, 0},
            {1, 0},
            {1, 1}
        };

        int[][][] P = new int[0][][];
        P[0] = new int[][] {
            {1, 1},
            {1, 1},
            {1, 0}
        };

        int[][][] T = new int[0][][];
        T[0] = new int[][] {
            {1, 1, 1},
            {0, 1, 0},
            {0, 1, 0}
        };

        basicDatabase = new int[][][][] { A, B, C, L, P, T };
    }

    private static void fillWith1(int[][][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j  < arr[0].length; j++) {
                for (int k = 0; k < arr[0][0].length; k++) {
                    arr[i][j][k] = 1;
                }
            }
        }
    }

    private static boolean databaseMade = false;

    /**
     * Make the database, created based on the pentomino's pieces defined in basicDatabase
     */
    public static void makeDatabase()
    {
        if (databaseMade) {
            return;
        }
        fillBasicDataBase();

        //do it for every piece of the basic database
        for(int i=0;i<basicDatabase.length;i++)
        {
            //make a piece with maximal number of mutations an space
            int[][][][] tempDatabase = new int[24][4][4][4];

            //take a piece of basic database, make it bigger so it fits in the 5*5, rotate it j times, move it to the left upper corner so duplicates will be the same
            for (int j = 0; j < 4; j++) {
                tempDatabase[j] = moveToAbove(rotate(makeBigger(basicDatabase[i], 4), j));
            }

            //same as above, but flipping it
            /*for (int j = 0; j < 4; j++) {
                tempDatabase[j] = moveToAbove(rotate(verticalFlip(makeBigger(basicDatabase[i], 5)), j));
            }*/

            //erase duplicates
            tempDatabase = eraseDuplicates(tempDatabase);

            //erase empty spaces in every piece
            for(int j=0;j<tempDatabase.length;j++)
            {
                tempDatabase[j]=eraseEmptySpace(tempDatabase[j]);
            }

            //add the found pieces of just one basic piece to the database
            data[i] = tempDatabase;
        }

        databaseMade = true;
    }
    
    /**
     * Rotate the matrix x times over 90 degrees 
     * Assume that the matrix is a square!
     * It does not make a copy, so the return matrix does not have to be used
     * @param data: a matrix
     * @param rotation: amount of rotation
     * @return the rotated matrix
     */
    public static int[][][] rotate(int[][][] data, int rotation)
    {
        int[][] tempData1 = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tempData1[i][j] = data[i][j];
            }
        }

        //do it for the amount of times it needs to be rotated
        for(int k=0;k<rotation;k++) {
            //make a matrix of the same size
            int[][] tempData2 = new int[tempData1.length][tempData1[0].length];
            //rotate it once and put it in tempData
            for (int i = 0; i < tempData1.length; i++) {
                for (int j = 0; j < tempData1[i].length; j++) {
                    tempData2[i][j] = tempData1[j][tempData1.length - i - 1];
                }
            }
            //put it back in the starting matrix so you can do it again
            for (int i = 0; i < tempData1.length; i++) {
                for (int j = 0; j < tempData1[i].length; j++) {
                    tempData1[i][j] = tempData2[i][j];
                }
            }
        }
        return tempData1;
    }

    /**
     * Flip the matrix vertically
     * It makes a copy, the input matrix stays unchanged
     * @param data: a matrix
     * @return the flipped matrix
     */
    /*public static int[][] verticalFlip(int[][] data)
    {
        //make a matrix of the same size
        int[][] returnData = new int[data.length][data[0].length];
        //flip the matrix to the return matrix
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                returnData[i][j]=data[i][data[i].length-j-1];
            }
        }
        return returnData;
    }*/

    /**
     * Flip the matrix horizontally
     * It makes a copy, the input matrix stays unchanged
     * @param data a matrix
     * @return the flipped matrix
     */
    /*public static int[][] horizontalFlip(int[][] data)
    {
        //make a matrix of the same size
        int[][] returnData = new int[data.length][data[0].length];
        //flip the matrix to the return matrix
        for(int i=0;i<data.length;i++)
        {
            for(int j=0;j<data[i].length;j++)
            {
                returnData[i][j]=data[data.length-i-1][j];
            }
        }
        return returnData;
    }*/

    /**
     * Expands a smaller than size*size matrix to a size*size matrix
     * It makes a copy, the input matrix stays unchanged
     * Assume that the input is smaller than size!!
     * @param data: a matrix
     * @param size: the square size of the new matrix
     * @return the size*size matrix
     */
    public static int[][][] makeBigger(int[][][] data, int size)
    {
        //make a matrix of size*size
        int[][][] returnData = new int[size][size][size];
        //copies the matrix in the new matrix
        for(int i=0;i<data.length;i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                for (int k = 0; k < data[i][j].length; k++) {
                    returnData[i][j][k] = data[i][j][k];
                }
            }
        }
        return returnData;
    }

    /**
     * Move matrix to the left above corner
     * Does not make a copy!
     * @param data: a matrix
     * @return the modified matrix
     */
    public static int[][][] moveToAbove(int[][][] data)
    {
        for (int dim = 0; dim < 3; dim ++) {
            int iMax = data.length;
            int jMax = data[0].length;
            int kMax = data[0][0].length;

            int iStart = 0;
            int jStart = 0;
            int kStart = 0;

            if (dim == 0) {
                iMax = 1;
                iStart = 1;
            }
            else if (dim == 1) {
                jMax = 1;
                jStart = 1;
            }
            else if (dim == 2) {
                kMax = 1;
                kStart = 1;
            }

            boolean empty = true;
            do {
                //check if the first row is empty
                for (int i = 0; i < iMax; i++) {
                    for(int j = 0; j < jMax; j++) {
                        for (int k = 0; k < kMax; k++) {
                            if(data[i][j][k] == 1){
                                empty = false;
                            }
                        }
                    }
                }
                
                //if empty move everything one up
                if(empty)
                {
                    for (int i = iStart; i < data.length; i++) {
                        for(int j = jStart; j < data[0].length; j++) {
                            for(int k = kStart; k < data[0][0].length; k++) {
                                
                                data[i - iStart][j - jStart][k - kStart] = data[i][j][k];
                                data[i][j][k] = 0;
                            }
                        }
                    }
                }
            } while (empty);
        }   
    }

    /**
     * Erase duplicates in a array of matrices
     * The input matrix stays unchanged
     * @param data an array of matrices
     * @return the array of matrices without duplicates
     */
    public static int[][][][] eraseDuplicates(int[][][][] data)
    {
        //make a counter that counts how many unique matrices there are
        int counter=0;
        //check all matrices of the input
        for(int i =0;i<data.length;i++)
        {
            //make an adder and set it to 1, if you find a duplicate, set it to 0
            int adder=1;
            //go from the start till the matrix that you are checking now
            for(int j=0;j<i;j++)
            {
                //check if equal
                if(isEqual(data[i],data[j]))
                {
                    adder=0;
                }
            }
            counter+=adder;
        }
        //make an array of matrices with size counter
        int[][][][] returnData = new int[counter][][][];
        //a counter that keeps how many matrices you already added to the new array of matrices
        counter=0;
        //check all matrices of the input
        for(int i =0;i<data.length;i++)
        {
            //go from the start till the matrix that you are checking now
            boolean alreadyExist=false;
            for(int j=0;j<i;j++)
            {
                if(isEqual(data[i],data[j]))
                {
                    alreadyExist=true;
                }
            }
            //if it's not already added, add it to the array
            if(alreadyExist==false) {
                returnData[counter] = data[i];
                //add one to counter, so next time you know where to add something
                counter++;
            }
        }
        return returnData;
    }

    /**
     * Check if two matrices are equal
     * Assume they have the same size
     * @param data1: the first matrix
     * @param data2: the second matrix
     * @return true if equal, false otherwise
     */
    public static boolean isEqual(int[][][] data1, int[][][] data2) {
        // Check if the dimensions match
        return Arrays.deepEquals(data1, data2);  // The matrices are equal
    }


    /**
     * Erase rows and columns that contain only zeros
     * @param data a matrix
     * @return the shrinken matrix
     */
    public static int[][][] eraseEmptySpace(int[][][] data)
    {
    }
}
