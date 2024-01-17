import javax.swing.*;

import Engine3DStuff.*;
import Engine3DStuff.customdatatypes.*;
import Engine3DStuff.Debugging.*;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.event.*;

import java.util.*;

import java.awt.*;

public class CameraRotator extends MouseAdapter {
    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    private int XCoord;
    private int YCoord;
    
    private boolean mouseDrag = false;

    private Camera camera;
    private Vector3D cameraRotation;
    private Vector3D cameraPosition;

    public CameraRotator(Camera camera, JFrame frame) {
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        this.camera = camera;

        // Temp start values:
        cameraPosition = new Vector3D(-10, -0, -0);
        cameraRotation = new Vector3D(0 * Math.PI / 180, 275 * Math.PI / 180, -180 * Math.PI / 180);

        camera.setPosition(cameraPosition);
        camera.setRotation(cameraRotation);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDrag = true;
        firstX = e.getX();
        firstY = e.getY();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // 
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDrag = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseDrag) {
            lastX = e.getX();
            lastY = e.getY();

            int deltaX = lastX - firstX;
            int deltaY = lastY - firstY;
            XCoord = firstX + deltaX;
            YCoord = firstY + deltaY;

            rotateCamera(deltaX, deltaY);
        }
    }

    private void rotateCamera(int deltaX, int deltaY) {
        cameraRotation.x += deltaX / 1000.0;
        cameraRotation.y += deltaY / 1000.0;

        camera.setRotation(cameraRotation);
        camera.setPosition(cameraPosition);
    }
}
