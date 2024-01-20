import javax.swing.*;

import Engine3DStuff.*;
import Engine3DStuff.customdatatypes.*;
import java.awt.event.*;


public class CameraRotator extends MouseAdapter {
    private int lastX;
    private int lastY;
    
    private boolean mouseDrag = false;

    private Camera camera;
    private Vector3D cameraRotation;
    private Vector3D cameraPosition;
    private double radius;

    private final double startRadius = 25;
    private final double minRadius = 1;
    private final double maxRadius = 60;
    private final double maxVertAngle = Math.toRadians(89.9);
    private final double dragSensitivity = 0.0015;
    private final double scrollSensitivity = 1;
    private final Vector3D startRotation = new Vector3D(0 * Math.PI / 180, 0 * Math.PI / 180, 0 * Math.PI / 180);

    public CameraRotator(Camera camera, JFrame frame) {
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
        this.camera = camera;

        // Temp start values:
        radius = startRadius;

        setRotation(new Vector3D(startRotation.x, startRotation.y, startRotation.z));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDrag = true;

        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDrag = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDrag) {

            int xCoord = e.getX();;
            int yCoord = e.getY();

            int deltaX = xCoord - lastX;
            int deltaY = yCoord - lastY;

            lastX = xCoord;
            lastY = yCoord;
            
            rotateCamera(deltaX, deltaY);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();

        radius += scrollSensitivity * notches;
        radius = Math.max(radius, minRadius);
        radius = Math.min(radius, maxRadius);
        recalculatePosition();
    }

    public void resetView() {
        radius = startRadius;
        setRotation(new Vector3D(startRotation.x, startRotation.y, startRotation.z));
    }

    private void rotateCamera(int deltaX, int deltaY) {
        Vector4D rotationDelta = new Vector4D(0, -deltaX * dragSensitivity, -deltaY * dragSensitivity, 0);
        Vector4D temp = new Vector4D(cameraRotation.x, cameraRotation.y, cameraRotation.z, 0);
        temp = rotationDelta.add(temp);

        setRotation(new Vector3D(temp.x, temp.y, temp.z));
    }

    private void setRotation(Vector3D rotation) {
        cameraRotation = rotation;

        cameraRotation.z = Math.max(-maxVertAngle, Math.min(maxVertAngle, cameraRotation.z));

        recalculatePosition();
    }

    private void recalculatePosition() {
        Vector3D cameraRotationSpecial = new Vector3D(0, 0, -cameraRotation.z);
        Vector4D up = new Vector4D(0, 0, -1, 0);
        Vector4D position4D = Matrix4x4.getRotationMatrix(cameraRotationSpecial).mul(up);

        cameraRotationSpecial = new Vector3D(0, -cameraRotation.y, 0);
        position4D = Matrix4x4.getRotationMatrix(cameraRotationSpecial).mul(position4D);

        Vector3D position3D = new Vector3D(position4D.x, position4D.y, position4D.z).mul(radius);
        
        camera.setRotation(cameraRotation);
        camera.setPosition(position3D);
    }
}




