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
    private int lastX;
    private int lastY;
    
    private boolean mouseDrag = false;

    private Camera camera;
    private Vector3D cameraRotation;
    private Vector3D cameraPosition;

    private double[] midpoint = {0, 0, 0};
    private double[] y = {5, 5, 5};
    private double radius = 9.12132;

    public CameraRotator(Camera camera, JFrame frame) {
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        this.camera = camera;

        // Temp start values:
        cameraPosition = new Vector3D(-10, 0, 0);
        cameraRotation = new Vector3D(0 * Math.PI / 180, 275 * Math.PI / 180, -180 * Math.PI / 180);

        camera.setPosition(cameraPosition);
        camera.setRotation(cameraRotation);
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

    private void rotateCamera(int deltaX, int deltaY) {
        double sensitivity = 0.0015;

        Vector4D rotationDelta = new Vector4D(deltaY * sensitivity, deltaX * sensitivity, 0, 0);

        //double maxVertAngle = Math.toRadians(359.0);
        //cameraRotation.x = Math.max(-maxVertAngle, Math.min(maxVertAngle, cameraRotation.x));

        Vector4D temp = new Vector4D(cameraRotation.x, cameraRotation.y, cameraRotation.z, 0);
        temp = Matrix4x4.getRotationMatrix(cameraRotation).mul(rotationDelta).add(temp);
        cameraRotation = new Vector3D(temp.x, temp.y, temp.z);

        Vector4D up = new Vector4D(0, 0, 1, 0);

        //Vector3D tempRotation = new Vector3D(cameraRotation.x, cameraRotation.y, cameraRotation.z);
        //Vector4D position4D = Matrix4x4.getRotationMatrix(tempRotation).mul(up);
        //Vector3D position3D = new Vector3D(position4D.x, position4D.y, position4D.z).mul(-10);
        //position3D = new Vector3D(0, 0, 10).add(position3D);
        //double[] x = findX(midpoint, y, radius);

        //cameraPosition.x = x[0];
        //cameraPosition.y = x[1];
        //cameraPosition.z = x[2];

        //System.out.println(position3D.x + ":" + position3D.y + ":" + position3D.z);

        camera.setRotation(cameraRotation);
        //camera.setPosition(position3D);
    }

    public static double[] findX(double[] midpoint, double[] y, double radius) {
        double[] vector = new double[3];
        
        for (int i = 0; i < 3; i++) {
            vector[i] = y[i] - midpoint[i];
        }

        double norm = calculateNorm(vector);

        double[] directionVector = new double[3];
        for (int i = 0; i < 3; i++) {
            directionVector[i] = vector[i] / norm;
        }

        double[] x = new double[3];
        for (int i = 0; i < 3; i++) {
            x[i] = midpoint[i] + radius * directionVector[i];
        }

        return x;
    }

    public static double calculateNorm(double[] vector) {
        double sumOfSquares = 0.0;

        for(double value : vector) {
            sumOfSquares += value * value;
        }

        return Math.sqrt(sumOfSquares);
    }
}




