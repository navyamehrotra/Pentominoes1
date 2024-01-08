package customdatatypes;

public class Plane {
    public Vector3D point;
    public Vector3D direction;

    public Plane(Vector3D point, Vector3D direction) {
        this.point = point;
        this.direction = direction.normalized();
    }

    public boolean isAbove(Vector3D q) {
        return direction.dot(q.substract(point)) > 0;
    }

    public Vector3D project(Vector3D pointToProject) {         
        Vector3D v = pointToProject.substract(point);         
        double dist = v.dot(direction);         
        Vector3D projectedPoint = pointToProject.substract(direction.mul(dist));         
        return (projectedPoint);     
    }
    
}
