package Engine3DStuff.customdatatypes;

public class CameraKeyframe {
    public Vector3D cameraPosition;
    public Vector3D cameraRotation;
    public double afterTransitionTime;
    
    public CameraKeyframe(Vector3D cameraPosition, Vector3D cameraRotation, double afterTransitionTime) { 
        this.cameraPosition = cameraPosition;
        this.cameraRotation = cameraRotation;
        this.afterTransitionTime = afterTransitionTime;
    }
}
