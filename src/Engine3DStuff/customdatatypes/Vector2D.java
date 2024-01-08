package Engine3DStuff.customdatatypes;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D mul(double num) {
        return new Vector2D(x * num, y * num);
    }

    public Vector2D add(Vector2D vec) {
        return new Vector2D(x + vec.x, y + vec.y);
    }
}
