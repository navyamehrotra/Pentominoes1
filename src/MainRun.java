import Engine3DStuff.customdatatypes.Vector3D;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.lang.Character.UnicodeScript;
import java.util.Random;

public class MainRun {

    private static Scene3DGenerator scene3dGenerator;
    private static MainUIFrame mainUIFrame;

    public static void main(String[] args) {
        Vector3D cameraPosition = new Vector3D(-10, -0, -0);
        Vector3D cameraRotation = new Vector3D(0 * Math.PI / 180, -275 * Math.PI / 180, -180 * Math.PI / 180);

        scene3dGenerator = new Scene3DGenerator(500, 300, cameraPosition, cameraRotation);
        mainUIFrame = new MainUIFrame(scene3dGenerator.render2ImageSample());
       

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                update();
            }
        };

        CameraRotator rotator = new CameraRotator(scene3dGenerator.getCamera(), mainUIFrame.getFrame());
        mainUIFrame.addResetViewListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                rotator.resetView();
            }
        });
        mainUIFrame.addShowTruckListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scene3dGenerator.showTruck(true);
            }
        });
        mainUIFrame.addHideTruckListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                scene3dGenerator.showTruck(false);
            }
        });

        // Model generation
        Random rand = new Random();
   
        int[][][] testGrid = new int[33][8][5]; //33 8 5
        for (int i = 0; i < testGrid.length; i++) {
            for (int j = 0; j < testGrid[0].length; j++) {
                for (int k = 0; k < testGrid[0][0].length; k++) {

                    if (rand.nextInt(1000) < 500) {
                        testGrid[i][j][k] = rand.nextInt(6);
                    } 
                    else {
                        testGrid[i][j][k] = -1;
                    }

                }
            }
        } 

        Knapsacker1.tempGenerator = scene3dGenerator;

        Timer timer = new Timer(1, listener);
        timer.start(); 

        //int[][][] solution = DancingLinks.search(Utility.ShapeSet.PLT, 33, 8, 5);
        int[][][] solution = Knapsacker1.search(Utility.ShapeSet.PLT, new int[]{3, 4, 5}, 33, 8, 5);
        scene3dGenerator.updateGrid(solution);
		System.out.println("Done!");

        // Timer
        
    }

    public static void update() {
        Dimension dimension = mainUIFrame.getSize();
        scene3dGenerator.resize((int)dimension.getWidth(), (int)dimension.getHeight());
        
        // Render
        mainUIFrame.update3DImage(scene3dGenerator.render2Image());
    }
}
