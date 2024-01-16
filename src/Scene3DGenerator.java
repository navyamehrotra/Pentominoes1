import javax.swing.*;

import Engine3DStuff.*;
import Engine3DStuff.customdatatypes.*;
import Engine3DStuff.Debugging.*;

import java.awt.image.BufferedImage;
import java.awt.Image;

import java.util.*;

import java.awt.*;

public class Scene3DGenerator {

    private Engine3D engine3d;
    private Camera camera;

    private ArrayList<Object3D> sampleModels;

    public Scene3DGenerator(int startWidth, int startHeight, Vector3D cameraPosition, Vector3D cameraRotation) {
        engine3d = new Engine3D(startWidth, startHeight);

        // Monkes :3
        sampleModels = new ArrayList<>();
        sampleModels.addAll(ModelLoader.loadOBJ("monke rainbow"));

        // Projection stuff
        camera = new Camera(cameraPosition, cameraRotation, startWidth, startHeight);
    }

    public void moveCamera(Vector3D cameraPosition) {
        camera.setPosition(cameraPosition);
    }

    public void rotateCamera(Vector3D cameraRotation) {
        camera.setRotation(cameraRotation);
    }

    public void resize(int width, int height) {
        engine3d = new Engine3D(width, height);   
        camera.setScale(width, height);
    }

    public Image render2ImageSample() {
        return engine3d.render2Image(camera.getPositionMatrix(), camera.getRotationMatrix(), camera.getProjectionMatrix(), sampleModels);
    }

    public Image render2Image(int[][][] grid) {
        ArrayList<Object3D> models = new ArrayList<>();

        int xSize = grid.length;
        int ySize = grid[0].length;
        int zSize = grid[0][0].length;

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    if (grid[x][y][z] != -1) {
                        Object3D cube = new Object3D();

                        Vector2D[] tempUVs = new Vector2D[] {new Vector2D(0, 0), new Vector2D(0, 1), new Vector2D(1, 1)};
                        Material tempMaterial = new Material(ModelLoader.loadTexture("tetris.png"));

                        Vector3D v0 = new Vector3D(0, 0, 0);
                        Vector3D v1 = new Vector3D(0, 0, 1);
                        Vector3D v2 = new Vector3D(0, 1, 0);
                        Vector3D v3 = new Vector3D(0, 1, 1);
                        Vector3D v4 = new Vector3D(1, 0, 0);
                        Vector3D v5 = new Vector3D(1, 0, 1);
                        Vector3D v6 = new Vector3D(1, 1, 0);
                        Vector3D v7 = new Vector3D(1, 1, 1);

                        cube.triangles.add(new Triangle(new Vector3D[]{v1, v3, v2}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v0, v1, v2}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v0, v4, v5}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v0, v5, v1}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v4, v6, v7}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v4, v7, v5}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v6, v2, v3}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v6, v3, v7}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v1, v5, v7}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v1, v7, v3}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v0, v6, v4}, tempUVs, tempMaterial));
                        cube.triangles.add(new Triangle(new Vector3D[]{v0, v2, v6}, tempUVs, tempMaterial));
                        

                        cube.setPosition(new Vector3D(x - (double)xSize / 2, y - (double)ySize / 2, z - (double)zSize / 2));
                        models.add(cube);
                    }
                }
            }
        }

        return engine3d.render2Image(camera.getPositionMatrix(), camera.getRotationMatrix(), camera.getProjectionMatrix(), models);
    }
}
