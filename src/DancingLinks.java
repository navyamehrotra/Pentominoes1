import java.lang.Character.UnicodeScript;
import java.util.ArrayList;

public class DancingLinks {

    public static void main(String[] args) {
        search(Utility.ShapeSet.PLT, 11, 5, 8);
        System.out.println();
    }

    public static int[][][] search(Utility.ShapeSet shapeSet, int sizeX, int sizeY, int sizeZ) {
        // Get the strips
        boolean[][] strips = getStrips(shapeSet, sizeX, sizeY, sizeZ);

        /*strips = new boolean[][]
        {
            {false, true, false, true},
            {true, false, false, false},
            {true, true, false, false},
            {false, false, true, false},
            {false, false, true, true}
        };*/

        // Setup all the nodes
        DLNode[] newestNodeInRow = new DLNode[strips.length];
        DLHeader[] headers = new DLHeader[strips[0].length];

        for (int i = 0; i < strips[0].length; i++) {
            headers[i] = new DLHeader();
            headers[i].headerNode = new DLNode();
            headers[i].ind = i;
        }

        for (int i = 0; i < strips[0].length; i++) {
            // Build the header
            DLHeader header = headers[i];
            header.headerNode.header = header;
            header.headerNode.left = headers[(i - 1 + headers.length) % headers.length].headerNode;
            header.headerNode.right = headers[(i + 1) % headers.length].headerNode;
            header.headerNode.up = header.headerNode;
            header.headerNode.down = header.headerNode;

            // Add the nodes below
            for (int j = 0; j < strips.length; j++) {
                if (strips[j][i]) {
                    header.amount++;
                    DLNode newNode;

                    if (newestNodeInRow[j] == null) {
                        newNode = new DLNode(header.headerNode, header.headerNode.up, header, j);
                    }
                    else {
                        newNode = new DLNode(header.headerNode, header.headerNode.up, newestNodeInRow[j], newestNodeInRow[j].right, header, j);
                    }

                    newestNodeInRow[j] = newNode;
                }
            }

        }

        DLNode root = new DLNode();
        root.left = headers[headers.length - 1].headerNode;
        headers[headers.length - 1].headerNode.right = root;
        root.right = headers[0].headerNode;
        headers[0].headerNode.left = root;

        // Start work
        ArrayList<Integer> solution = new ArrayList<>();
        recursiveSolve(root, solution);

        int[][][] grid = new int[sizeX][sizeY][sizeZ];

        for (int i = 0; i < solution.size(); i++) {
            int ind = 0;
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    for (int z = 0; z < sizeZ; z++) {
                        if (strips[solution.get(i)][ind] == true ) {
                            grid[x][y][z] = i % 12;
                        }
                        ind++;
                    }
                }
            }
        }

        return grid;
    }

    private static void restore(DLHeader header) {
        for (DLNode rowNode = header.headerNode.up; rowNode != header.headerNode; rowNode = rowNode.up) {
            for (DLNode colNode = rowNode.left; colNode != rowNode; colNode = colNode.left) {
                colNode.header.amount++;
                colNode.down.up = colNode;
                colNode.up.down = colNode;
            }
        }

        header.headerNode.right.left = header.headerNode;
        header.headerNode.left.right = header.headerNode;
    }

    private static void remove(DLHeader header) {
        header.headerNode.right.left = header.headerNode.left;
        header.headerNode.left.right = header.headerNode.right;

        for (DLNode rowNode = header.headerNode.down; rowNode != header.headerNode; rowNode = rowNode.down) {
            for (DLNode colNode = rowNode.right; colNode != rowNode; colNode = colNode.right) {
                colNode.down.up = colNode.up;
                colNode.up.down = colNode.down;

                colNode.header.amount--;
            }
        }
    }

    private static DLHeader findBestHeader(DLNode root) {
        int min = Integer.MAX_VALUE;
        DLHeader best = null;

        for (DLNode header = root.right; header != root; header = header.right) {
            if (header.header.amount < min) {
                best = header.header;
                min = header.header.amount;
            }
        }

        return best;
    }


    private static int currentBest = 0;
    
    private static boolean recursiveSolve(DLNode root, ArrayList<Integer> currentSolution) {
        if (root.right == root) {
            System.out.println("Found a solution");
            return true;
        }

        DLHeader header = findBestHeader(root);
        remove(header);
        
        DLNode node = header.headerNode.down;
        //System.out.println("Picked header " + header.ind);

        while(node != header.headerNode) {
            currentSolution.add(node.row);

            if (currentSolution.size() > currentBest) {
                currentBest = currentSolution.size();
                System.out.println("New best! " + currentBest);
            }

            //System.out.println("Removed row" + node.row);
            for (DLNode iter = node.right; iter != node; iter = iter.right) {
               // System.out.println("Removed header " + iter.header.ind);
                remove(iter.header);
            }

            if (recursiveSolve(root, currentSolution)) {
                return true;
            }

            //System.out.println("Added row " + currentSolution.get(currentSolution.size() - 1));
            currentSolution.remove(currentSolution.size() - 1);
            for (DLNode iter = node.left; iter != node; iter = iter.left) {
                //System.out.println("Restored header " + iter.header.ind);
                restore(iter.header);
            }

            node = node.down;
        }

        restore(header);
        return false;
    }

    private static boolean[][] getStrips(Utility.ShapeSet shapeSet, int sizeX, int sizeY, int sizeZ) {
        ArrayList<boolean[]> fills = new ArrayList<>();

        int[][][][][] database = DatabaseGenerator.getDatabase();

        char[] chars = Utility.getShapeSet(shapeSet);

        for (int i = 0; i < chars.length; i++) {
            int[][][][] piece = database[Utility.characterToID(chars[i])];

            for (int mutation = 0; mutation < piece.length; mutation++) {
                int[][][] pieceRotated = piece[mutation];

                for (int x = 0; x < sizeX; x++) {
                    for (int y = 0; y < sizeY; y++) {
                        for (int z = 0; z < sizeZ; z++) {
                            int[][][] fillBoard = new int[sizeX][sizeY][sizeZ];

                            if (Utility.canAddPiece(fillBoard, pieceRotated, 0, x, y, z, sizeX, sizeY, sizeZ)) {
                                Utility.addPiece(fillBoard, pieceRotated, 1, x, y, z);

                                boolean[] unravelled = new boolean[sizeX * sizeY * sizeZ];

                                int ind = 0;
                                for (int j = 0; j < sizeX; j++) {
                                    for (int k = 0; k < sizeY; k++) {
                                        for (int l = 0; l < sizeZ; l++) {
                                            unravelled[ind] = (fillBoard[j][k][l] == 1);
                                            ind++;
                                        }
                                    }
                                }

                                if (ind == 0) {
                                    System.out.println();
                                }

                                fills.add(unravelled);
                            }
                        }
                    }
                }
            }
        }

        boolean[][] strips = new boolean[fills.size()][];

        for (int i = 0; i < fills.size(); i++) {
            strips[i] = fills.get(i);
        }

        return strips;
    }
}
