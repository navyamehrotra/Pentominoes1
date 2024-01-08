package customdatatypes;

public class Triangle {
    public Vector3D[] vertices;
    public Vector2D[] uvs;

    public Material material;

    public Triangle(Vector3D[] vertices, Vector2D[] uvs, Material material) {
        this.vertices = vertices;
        this.uvs = uvs;
        this.material = material;
    }

    public double distanceToPoint(Vector3D point) { // Based on
                                                    // (https://stackoverflow.com/questions/2924795/fastest-way-to-compute-point-to-triangle-distance-in-3d)
        return closestPointTo(point).distance(point);
    }

    public Vector3D closestPointTo(Vector3D point) { // Based on
                                                     // (https://stackoverflow.com/questions/2924795/fastest-way-to-compute-point-to-triangle-distance-in-3d)
        // Find the projection of the point onto the edge

        double uab = Project(vertices[0], vertices[1], point);
        double uca = Project(vertices[2], vertices[0], point);

        if (uca > 1 && uab < 0) {
            return vertices[0];
        }

        double ubc = Project(vertices[1], vertices[2], point);

        if (uab > 1 && ubc < 0) {
            return vertices[1];
        }

        if (ubc > 1 && uca < 0) {
            return vertices[2];
        }

        Vector3D abDelta = vertices[1].substract(vertices[0]);
        Vector3D bcDelta = vertices[2].substract(vertices[1]);
        Vector3D caDelta = vertices[0].substract(vertices[2]);

        Vector3D triNorm = vertices[0].substract(vertices[1]).cross(vertices[0].substract(vertices[2]));
        Plane planeAB = new Plane(vertices[0], triNorm.cross(abDelta));
        Plane planeBC = new Plane(vertices[1], triNorm.cross(bcDelta));
        Plane planeCA = new Plane(vertices[2], triNorm.cross(caDelta));

        if (uab >= 0 && uab <= 1 && !planeAB.isAbove(point)) {
            return vertices[0].add(abDelta.mul(uab));
        }

        if (ubc >= 0 && ubc <= 1 && !planeBC.isAbove(point)) {
            return vertices[1].add(bcDelta.mul(ubc));
        }

        if (uca >= 0 && uca <= 1 && !planeCA.isAbove(point)) {
            return vertices[2].add(caDelta.mul(uca));
        }

        Plane triPlane = new Plane(vertices[0], triNorm);

        // The closest point is in the triangle so
        // project to the plane to find it
        return triPlane.project(point);
    }

    public double Project(Vector3D a, Vector3D b, Vector3D point) {
        Vector3D delta = b.substract(a);
        return (point.substract(a)).dot(delta) / delta.lengthSquared();
    }
}