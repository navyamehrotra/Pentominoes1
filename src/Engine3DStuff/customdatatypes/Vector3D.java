package Engine3DStuff.customdatatypes;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D mul(double num) {
        return new Vector3D(x * num, y * num, z * num);
    }

    public Vector3D add(Vector3D vec) {
        return new Vector3D(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vector3D substract(Vector3D vec) {
        return new Vector3D(x - vec.x, y - vec.y, z - vec.z);  
    }

    public double distance(Vector3D point) {
        return Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2) + Math.pow(point.z - z, 2));
    }

    public double lengthSquared() {
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double dot(Vector3D vector) { // Based on (https://chortle.ccsu.edu/vectorlessons/vch07/vch07_14.html)
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public Vector3D cross(Vector3D vector) { // Based on (http://www.java2s.com/example/java/java.lang/compute-the-cross-product-a-vector-of-two-vectors.html)
        return new Vector3D(y * vector.z - z * vector.y, 
        z * vector.x - x * vector.z, 
        x * vector.y - y * vector.x);
    }

    public Vector3D normalized() {
        double length = length();

        if (length == 0) return new Vector3D(0, 0, 0);

        return new Vector3D(x / length, y / length, z / length);
    }

    public static Vector3D lerp(Vector3D a, Vector3D b, double value) {
        return a.add(b.add(a.mul(-1)).mul(value));
    }
}