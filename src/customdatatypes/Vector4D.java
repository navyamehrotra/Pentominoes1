package customdatatypes;

public class Vector4D {
    public double x;
    public double y;
    public double z;
    public double w;

    public Vector4D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4D(Vector3D vector3d, double w) {
        this.x = vector3d.x;        
        this.y = vector3d.y;
        this.z = vector3d.z;
        this.w = w;
    }

    public Vector4D mul(double num) {
        return new Vector4D(x * num, y * num, z * num, w * num);
    }

    public Vector4D add(Vector4D vec) {
        return new Vector4D(x + vec.x, y + vec.y, z + vec.z, w + vec.w);
    }
}
