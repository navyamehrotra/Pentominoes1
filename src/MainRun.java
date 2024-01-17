import Engine3DStuff.customdatatypes.Vector3D;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

public class MainRun {

    private static Scene3DGenerator scene3dGenerator;
    private static MainUIFrame mainUIFrame;
    private static Vector3D cameraRotation;

    public static void main(String[] args) {
        Vector3D cameraPosition = new Vector3D(-10, -0, -0);
        cameraRotation = new Vector3D(0 * Math.PI / 180, -0 * Math.PI / 180, -180 * Math.PI / 180);

        scene3dGenerator = new Scene3DGenerator(500, 300, cameraPosition, cameraRotation);
        mainUIFrame = new MainUIFrame(scene3dGenerator.render2ImageSample());

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                update();
            }
        };

        CameraRotator rotator = new CameraRotator(scene3dGenerator.getCamera(), mainUIFrame.getFrame());

        // Model generation
        int[][][] testGrid = new int[6][6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 6; k++) {
                    testGrid[i][j][k] = -1;
                }
            }
        } 
        testGrid[0] = new int[][]
        {
            {-1, -1, -1, -1, -1, -1},
            {-1, -1, 1, -1, 1, -1},
            {-1, -1, -1, -1, -1, -1},
            {-1, 1, -1, 1, -1, 1},
            {-1, -1, 1, -1, 1, -1},
            {-1, -1, -1, -1, -1, -1}
        };

        scene3dGenerator.updateGrid(testGrid);

        // Timer
        Timer timer = new Timer(1, listener);
        timer.start(); 
    }

    public static void update() {
        Dimension dimension = mainUIFrame.getSize();
        scene3dGenerator.resize((int)dimension.getWidth(), (int)dimension.getHeight());
        
        // Render
        mainUIFrame.update3DImage(scene3dGenerator.render2Image());
    }
}
