package Engine3DStuff;

import Engine3DStuff.customdatatypes.Matrix4x4;
import Engine3DStuff.customdatatypes.Vector3D;

public class Camera {
    private Matrix4x4 positionMatrix;
    private Matrix4x4 rotationMatrix;
    private Matrix4x4 projectionMatrix;

    public Camera(Vector3D position, Vector3D rotation, int imageWidth, int imageHeight) {
        setPosition(position);
        setRotation(rotation);
        setScale(imageWidth, imageHeight);
    }

    public void setScale(int imageWidth, int imageHeight) {
        projectionMatrix = Matrix4x4.getProjectionMatrix(imageWidth, imageHeight);
    }

    public void setPosition(Vector3D cameraPosition) {
        positionMatrix = Matrix4x4.getTranslationMatrix(cameraPosition);        
    }

    public void setRotation(Vector3D cameraRotation) {
        rotationMatrix = Matrix4x4.getRotationMatrix(cameraRotation);
    }

    public void setRotation(Matrix4x4 cameraRotation) {
        rotationMatrix = cameraRotation;
    }

    public Matrix4x4 getPositionMatrix() {
        return positionMatrix;
    }

    public Matrix4x4 getRotationMatrix() {
        return rotationMatrix;
    }

    public Matrix4x4 getProjectionMatrix() {
        return projectionMatrix;
    }
}
