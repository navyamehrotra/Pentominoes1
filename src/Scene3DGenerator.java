import javax.swing.*;

import Engine3DStuff.*;
import Engine3DStuff.customdatatypes.*;
import Engine3DStuff.Debugging.*;

import java.awt.image.BufferedImage;
import java.util.*;

import java.awt.*;

public class Scene3DGenerator {

    private Engine3D engine3d;
    private Camera camera;

    private ArrayList<Object3D> sampleModels;
    private ArrayList<Object3D> truckModel;
    private int[][][] grid;
    private ArrayList<Object3D> models = new ArrayList<>();
    private ArrayList<Material> materialSide = new ArrayList<>();
    private ArrayList<Material> materialTop = new ArrayList<>();
    private boolean doShowTruck = true;

    public Scene3DGenerator(int startWidth, int startHeight, Vector3D cameraPosition, Vector3D cameraRotation) {
        engine3d = new Engine3D(startWidth, startHeight);

        // Load Textures
        BufferedImage sideTexture = ModelLoader.loadTexture("Cube_Sidex2.png");
        BufferedImage topTexture = ModelLoader.loadTexture("Cube_Topx2.png");

        Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.YELLOW, Color.ORANGE, Color.BLACK, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.MAGENTA, Color.WHITE };
        for (int i = 0; i < colors.length; i++) {
            materialSide.add(new Material(repaintImage(sideTexture, colors[i])));
            materialTop.add(new Material(repaintImage(topTexture, colors[i])));
        }
        
        // Monkes :3
        sampleModels = new ArrayList<>();
        sampleModels.addAll(ModelLoader.loadOBJ("monke rainbow"));
        truckModel = new ArrayList<>();
        truckModel.addAll(ModelLoader.loadOBJ("truck2"));

        for (Object3D model : truckModel) {
            model.setPosition(new Vector3D(0, -7.84, 0));
            model.setRotation(new Vector3D(0, Math.toRadians(-90), 0));
        }

        // Projection stuff
        camera = new Camera(cameraPosition, cameraRotation, startWidth, startHeight);
    }

    private BufferedImage repaintImage(BufferedImage image, Color color) {
        BufferedImage repainted = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                int r = pixelColor.getRed() * color.getRed() / 255;
                int g = pixelColor.getGreen() * color.getGreen() / 255;
                int b = pixelColor.getBlue() * color.getBlue() / 255;
                repainted.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return repainted;
    }

    public Camera getCamera() {
        return camera;
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

    public void updateGrid(int[][][] grid) {


        this.grid = grid;

        ArrayList<Object3D> models2 = new ArrayList<>();
        if (doShowTruck) { 
            models2.addAll(truckModel);
        }

        int xSize = grid.length;
        int ySize = grid[0].length;
        int zSize = grid[0][0].length;

        // Truck offset
        double xOffset = xSize > 33 ? (33 - xSize) / 2.0 : 0;
        Vector3D offset = new Vector3D(-xOffset, (8 - ySize) / 2.0, 0);
        for (Object3D model : truckModel) {
            model.setPosition(new Vector3D(0, -7.84, 0).add(offset));
            model.setRotation(new Vector3D(0, Math.toRadians(-90), 0));
        }

        // Fill cubes
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    if (grid[x][y][z] != -1) {
                        Vector3D cubeCenter = new Vector3D(x - (double)xSize / 2 + 0.5, y - (double)ySize / 2 + 0.5, z - (double)zSize / 2 + 0.5);
                        Vector3D cubeExtents = new Vector3D(0.5, 0.5, 0.5);
                        models2.add(getCube(cubeCenter, cubeExtents, null, grid[x][y][z]));
                    }
                }
            }
        }

        addBorder(models2, xSize, ySize, zSize);
        models = models2;
    }

    public Image render2Image() {
       
        return engine3d.render2Image(camera.getPositionMatrix(), camera.getRotationMatrix(), camera.getProjectionMatrix(), models);
    }

    private void addBorder(ArrayList<Object3D> models, double xSize, double ySize, double zSize) {
        // The line border
        double lineWidth = 0.01;

        // Z direction lines
        Vector3D cubeCenter = new Vector3D(xSize / 2, ySize / 2, 0);
        Vector3D cubeExtents = new Vector3D(lineWidth, lineWidth, zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(-xSize / 2, ySize / 2, 0);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(xSize / 2, -ySize / 2, 0);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(-xSize / 2, -ySize / 2, 0);
        models.add(getCube(cubeCenter, cubeExtents));

        // Y direction lines
        cubeCenter = new Vector3D(xSize / 2, 0, zSize / 2);
        cubeExtents = new Vector3D(lineWidth, ySize / 2, lineWidth);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(-xSize / 2, 0, zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(xSize / 2, 0, -zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(-xSize / 2, 0, -zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        // X direction lines
        cubeCenter = new Vector3D(0, ySize / 2, zSize / 2);
        cubeExtents = new Vector3D(xSize / 2, lineWidth, lineWidth);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(0, -ySize / 2, zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(0, ySize / 2, -zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));

        cubeCenter = new Vector3D(0, -ySize / 2, -zSize / 2);
        models.add(getCube(cubeCenter, cubeExtents));
    }

    private Object3D getCube(Vector3D center, Vector3D extents) {
        Material material = new Material(Color.black);
        return getCube(center, extents, material, 0);
    }

    public void showTruck(boolean show) {
        models.removeAll(truckModel);

        if (show) {
            models.addAll(truckModel);

        }
    }

    private Object3D getCube(Vector3D center, Vector3D extents, Material material, int ind) {
        Material side = materialSide.get(ind);
        Material top = materialTop.get(ind);

        if (material != null) {
            side = material;
            top = material;
        }

        Object3D cube = new Object3D();

        Vector2D[] none = new Vector2D[] {new Vector2D(0, 0), new Vector2D(0, 0), new Vector2D(0, 0)};


        Vector2D[] uv1 = new Vector2D[] {new Vector2D(0, 0), new Vector2D(0, 1), new Vector2D(1, 1)};
        Vector2D[] uv2 = new Vector2D[] {new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(1, 0)};
        Vector2D[] uv3 = new Vector2D[] {new Vector2D(1, 1), new Vector2D(0, 1), new Vector2D(0, 0)};
        Vector2D[] uv4 = new Vector2D[] {new Vector2D(1, 1), new Vector2D(1, 0), new Vector2D(0, 0)};
        Vector2D[] uv5 = new Vector2D[] {new Vector2D(0, 1), new Vector2D(1, 1), new Vector2D(0, 0)};
        Vector2D[] uv6 = new Vector2D[] {new Vector2D(1, 1), new Vector2D(0, 0), new Vector2D(0, 1)};
        Vector2D[] uv7 = new Vector2D[] {new Vector2D(0, 1), new Vector2D(1, 1), new Vector2D(1, 0)};
        Vector2D[] uv8 = new Vector2D[] {new Vector2D(0, 1), new Vector2D(1, 0), new Vector2D(0, 0)};
        Vector2D[] uv9 = new Vector2D[] {new Vector2D(1, 0), new Vector2D(0, 0), new Vector2D(0, 1)};
        Vector2D[] uv10 = new Vector2D[] {new Vector2D(1, 0), new Vector2D(0, 1), new Vector2D(1, 1)};

        Vector3D v0 = new Vector3D(-extents.x, -extents.y, -extents.z);
        Vector3D v1 = new Vector3D(-extents.x, -extents.y, extents.z);
        Vector3D v2 = new Vector3D(-extents.x, extents.y, -extents.z);
        Vector3D v3 = new Vector3D(-extents.x, extents.y, extents.z);
        Vector3D v4 = new Vector3D(extents.x, -extents.y, -extents.z);
        Vector3D v5 = new Vector3D(extents.x, -extents.y, extents.z);
        Vector3D v6 = new Vector3D(extents.x, extents.y, -extents.z);
        Vector3D v7 = new Vector3D(extents.x, extents.y, extents.z);

        cube.triangles.add(new Triangle(new Vector3D[]{v1, v3, v2}, uv4, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v0, v1, v2}, uv5, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v0, v4, v5}, uv7, top));
        cube.triangles.add(new Triangle(new Vector3D[]{v0, v5, v1}, uv8, top));
        cube.triangles.add(new Triangle(new Vector3D[]{v4, v6, v7}, uv4, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v4, v7, v5}, uv6, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v6, v2, v3}, uv9, top));
        cube.triangles.add(new Triangle(new Vector3D[]{v6, v3, v7}, uv10, top));
        cube.triangles.add(new Triangle(new Vector3D[]{v1, v5, v7}, uv7, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v1, v7, v3}, uv8, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v0, v6, v4}, uv6, side));
        cube.triangles.add(new Triangle(new Vector3D[]{v0, v2, v6}, uv4, side));

        cube.setPosition(center);

        return cube;
    }   
}
