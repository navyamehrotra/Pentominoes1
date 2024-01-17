import Engine3DStuff.customdatatypes.CameraKeyframe;
import Engine3DStuff.customdatatypes.Vector3D;

public class MainRun {
    
    public static void main(String[] args) {
        Engine3D engine = new Engine3D(500, 300);

        Vector3D cameraPosition = new Vector3D(0, -12, -12);
        Vector3D cameraRotation = new Vector3D(0 * Math.PI / 180, -0 * Math.PI / 180, -40 * Math.PI / 180);
        CameraKeyframe cameraStart = new CameraKeyframe(cameraPosition, cameraRotation, 1);
    }

    public void update() {
        cameraRotation.x += MainUIFrame.getXCoord():
        cameraRotation.y += MainUIFrame.getYCoord();

        scene3DGenerator.rotateCamera(cameraRotation);


    }
}
