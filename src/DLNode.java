public class DLNode {
    public DLNode up;
    public DLNode right;
    public DLNode down;
    public DLNode left;
    public DLHeader header;
    public int row;

    public DLNode(DLNode down, DLNode up, DLNode left, DLNode right, DLHeader header, int row) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;

        down.up = this;
        up.down = this;
        left.right = this;
        right.left = this;

        this.header = header;
        this.row = row;
    }

    public DLNode(DLNode down, DLNode up, DLHeader header, int row) {
        this.up = up;
        this.down = down;
        this.left = this;
        this.right = this;

        down.up = this;
        up.down = this;

        this.header = header;
        this.row = row;
    }

    public DLNode() {

    }
}
