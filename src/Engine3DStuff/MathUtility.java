package Engine3DStuff;
import Engine3DStuff.customdatatypes.Vector2D;
import Engine3DStuff.customdatatypes.Vector3D;

public class MathUtility {
    public static double edgeFunction(Vector2D p1, Vector3D p2, Vector3D p3) { // (https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle)
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
    
}
