package Engine3DStuff;


import java.util.Vector;

import Engine3DStuff.customdatatypes.Constants;
import Engine3DStuff.customdatatypes.Matrix4x4;
import Engine3DStuff.customdatatypes.Triangle;
import Engine3DStuff.customdatatypes.Vector3D;

public class Object3D {
    public Vector<Triangle> triangles;

    private Vector3D position;
    private Matrix4x4 positionMatrix;

    private Vector3D rotation;
    private Matrix4x4 rotationMatrix;

    public Object3D() {
        triangles = new Vector<Triangle>();

        position = new Vector3D(0, 0, 0);
        positionMatrix = Matrix4x4.getIdentityMatrix();

        rotation = new Vector3D(0, 0, 0);
        rotationMatrix = Matrix4x4.getIdentityMatrix();
    }

    // Position
    public void setPosition(Vector3D position) {
        this.position = position;
        positionMatrix = Matrix4x4.getTranslationMatrix(position);
    }

    public Vector3D getPosition() {
        return position;
    }

    public Matrix4x4 getPositionMatrix() {
        return positionMatrix;
    }

    // Rotation
    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
        rotationMatrix = Matrix4x4.getRotationMatrix(rotation);
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public Matrix4x4 getRotationMatrix() {
        return rotationMatrix;
    }
}
