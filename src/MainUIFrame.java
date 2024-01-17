import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;



public class MainUIFrame {
    // Toly and nat

    private Engine3D engine3d;

    private int firstX;
    private int firstY;
    private int lastX;
    private int lastY;
    private static int XCoord;
    private static int YCoord;
    
    private boolean mouseDrag = false;

    public MainUIFrame(Engine3D engine3d) {
        this.engine3d = engine3d;

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseDrag = true;
                firstX = e.getX();
                firstY = e.getY();

                System.out.println("clicked");
                
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
                
                }
            }
        });


    }

    public void update() {
        //JPanel midPanel = engine3d.render2Panel();
        // do something with it
    }

    public static int getXCoord() {
        return XCoord;
    }

    public static int getYCoord() {
        return YCoord;
    }
}
