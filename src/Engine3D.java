import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Engine3DStuff.Camera;
import Engine3DStuff.MathUtility;
import Engine3DStuff.Object3D;
import Engine3DStuff.Debugging.ModelLoader;
import Engine3DStuff.customdatatypes.*;

import java.awt.image.BufferedImage;
import java.awt.Image;

import java.util.*;

import java.awt.Color;
import java.awt.Graphics2D;

public class Engine3D {
    public int width;
    public int height;
    public Color[][] imageData;


    public Engine3D(int width, int height) {
        this.width = width;
        this.height = height;

        imageData = new Color[width][height];
    }

    public static void main(String[] args) {
        
    }

    public static Image sampleImage() {
        Engine3D engine = new Engine3D(500, 400);

        // Setup camera data
        Matrix4x4 projectionMatrix = Matrix4x4.getProjectionMatrix();
        Vector3D cameraPosition = new Vector3D(0, -12, -12);
        Vector3D cameraRotation = new Vector3D(0 * Math.PI / 180, -0 * Math.PI / 180, -40 * Math.PI / 180);
        CameraKeyframe cameraStart = new CameraKeyframe(cameraPosition, cameraRotation, 1);
        Camera camera = new Camera();
        camera.appendKeyframes(cameraStart);
        camera.animate();

        // Load the test models
        ArrayList<Object3D> models = new ArrayList<>();
        models.addAll(ModelLoader.loadOBJ("monke rainbow"));

        //Setup the JFrame


        // Render and display the scene

        return engine.render2Image(camera.getPositionMatrix(), camera.getRotationMatrix(), projectionMatrix, models);

    }

    public Image render2Image(Matrix4x4 translationMatrix, Matrix4x4 rotationMatrix, Matrix4x4 projectionMatrix,
            ArrayList<Object3D> models) {
        Color[][] colorData = renderFrame(translationMatrix, rotationMatrix, projectionMatrix, models);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, colorData[x][y].getRGB());
            }
        }

        JPanel panel = new JPanel();
        return image;
        //panel.add(new JLabel(new ImageIcon(image)));

        //return panel;
    }

    /**
     * Renders a single frame in 3D
     * The engine is mostly based on 
     * (https://www.scratchapixel.com/index.html)
     * @param translationMatrix the matrix every object gets moved by. It's related to the camera's position
     * @param rotationMatrix the matrix every object will get rotated by. It's related to the camera's orientation
     * @param projectionMatrix the projection matrix that converts the objects' positions from world space to clip space
     * @param models the models to be rendered
     * @return the value of the public member "imageData" - an array containing the color of each pixel
     */
    public Color[][] renderFrame(Matrix4x4 translationMatrix, Matrix4x4 rotationMatrix, Matrix4x4 projectionMatrix,
            ArrayList<Object3D> models) {
        // Transform all of the triangles from world space to clip space (see transformVertexToScreenSpace)
        ArrayList<Triangle> allTriangles = new ArrayList<Triangle>();
        ArrayList<Vector4D[]> transformedVertices = new ArrayList<Vector4D[]>();

        for (Object3D model : models) {
            Matrix4x4 modelRotationMatrix = model.getRotationMatrix();
            Matrix4x4 modelPositionMatrix = model.getPositionMatrix();

            for (Triangle triangle : model.triangles) {
                Vector4D[] verticesArray = new Vector4D[3];
                for (int i = 0; i < 3; i++) {
                    verticesArray[i] = new Vector4D(triangle.vertices[i].x, triangle.vertices[i].y, triangle.vertices[i].z, 1);
                    verticesArray[i] = transformVertexToClipSpace(verticesArray[i], modelRotationMatrix, modelPositionMatrix, translationMatrix, rotationMatrix, projectionMatrix);
                }
                transformedVertices.add(verticesArray);
                allTriangles.add(triangle);
            }
        }

        // Clip the triangles to the view space. (cut out the parts that are not visible.)
        // Some triangles will be completly removed, some will be cut into more triangles, some will just be left alone.
        // https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/projection-matrix-GPU-rendering-pipeline-clipping.html
        // https://en.wikipedia.org/wiki/Sutherland%E2%80%93Hodgman_algorithm
        ArrayList<Triangle> transformedTriangles = new ArrayList<Triangle>();
        ArrayList<Vector3D> transformedTriangleW = new ArrayList<Vector3D>();

        for (int i = 0; i < allTriangles.size(); i++) {
            Triangle triangle = allTriangles.get(i);
            Vector4D[] verticesArray = transformedVertices.get(i);

            clipTriangleToScreenSpace(verticesArray, triangle, transformedTriangles, transformedTriangleW);
        }

        // Rasterize the triangles (Mark which triangle is visible for every pixel on the screen.) and collect the baryCoords.
        // https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/projection-stage.html
        // https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        int[][] triangleIndexes = new int[width][height];
        double[][] depthBuffer = new double[width][height];
        Vector3D[][] baryCoordsOfPixels = new Vector3D[width][height];

        for (int i = 0; i < transformedTriangles.size(); i++) {
            Triangle triangle = transformedTriangles.get(i);
            Vector3D wValues = transformedTriangleW.get(i);
            
            rasterizeTriangle(triangle, wValues, i, triangleIndexes, depthBuffer, baryCoordsOfPixels);
        }

        // Based on the result of rasterization, calculate the actual color of every pixel
        // https://www.cs.ucr.edu/~craigs/courses/2018-fall-cs-130/lectures/perspective-correct-interpolation.pdf
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imageData[x][y] = getColorOfPixel(x, y, baryCoordsOfPixels, triangleIndexes, transformedTriangles);
            }
        }

        return imageData;
    }

    ///
    /// Section 1: World to screen space transformation
    /// 

    /**
     * Transfrom a single vertex from world space (here it is in the world) to clip space (where it is on the screen.)
     * Based on:
     * (https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/projection-matrix-GPU-rendering-pipeline-clipping.html)
     * @param vertex to be transformed
     * @param modelRotationMatrix ]
     * @param modelPositionMatrix
     * @param translatioMatrix
     * @param rotationMatrix
     * @param projectionMatrix
     * @return the transformed vertex, along with the w coordinate that's later used for clipping and interpolation.
     */
    public static Vector4D transformVertexToClipSpace(Vector4D vertex, Matrix4x4 modelRotationMatrix,
            Matrix4x4 modelPositionMatrix, 
            Matrix4x4 translatioMatrix, Matrix4x4 rotationMatrix, Matrix4x4 projectionMatrix) {
        vertex = modelRotationMatrix.mul(vertex);
        vertex = translatioMatrix.mul(vertex);
        vertex = modelPositionMatrix.mul(vertex);
        vertex = rotationMatrix.mul(vertex);
        vertex = projectionMatrix.mul(vertex);

        return vertex;
    }

    ///
    /// Section 2: Triangle clipping
    ///
    
    /**
     * Clip a triangle to the view space. (cut out the parts that are not visible.)
     * Some triangles will be completly removed, some will be cut into more triangles, some will just be left alone.
     * Based on:
     * (https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/projection-matrix-GPU-rendering-pipeline-clipping.html)
     * (https://en.wikipedia.org/wiki/Sutherland%E2%80%93Hodgman_algorithm)
     * @param transformedVertices the input vertices. In clip space
     * @param triangle the original triangle. We need it for the uv coordinates
     * @param trianglesOut the resulting triangles will be added to this list
     * @param triangleWOut the resulting triangles' w values will be added to this list
     */
    private void clipTriangleToScreenSpace (Vector4D[] transformedVertices, Triangle triangle, ArrayList<Triangle> trianglesOut, ArrayList<Vector3D> triangleWOut) {
        // Containers that will hold the polygon created by clipping    
        ArrayList<Vector4D> resultingPolygon = new ArrayList<Vector4D>();
        ArrayList<Vector2D> resultingUVs = new ArrayList<Vector2D>();

        // Start with just the input trianagle
        for (int i = 0; i < 3; i++) {
            resultingPolygon.add(transformedVertices[i]);
            resultingUVs.add(triangle.uvs[i]);
        }

        // Go through the boundries, if a segment of the polgyon is crossing the boundary, cut it up according to the
        // Sutherland-Hodgman algorithm's rules. The boundaries (stages) are in order: (x>=1, x<=1, y<=-1, y>=1, z<=-1, z>=1, w>0)
        for (int stage = 0; stage < 7; stage++) {
            ArrayList<Vector4D> newPolygon = new ArrayList<Vector4D>();
            ArrayList<Vector2D> newUVs = new ArrayList<Vector2D>();

            for (int i = 0; i < resultingPolygon.size(); i++) {
                Vector4D a = resultingPolygon.get(i);
                Vector4D b = resultingPolygon.get((i + 1) % resultingPolygon.size());

                Vector2D aUV = resultingUVs.get(i);
                Vector2D bUV = resultingUVs.get((i + 1) % resultingPolygon.size());

                boolean aInside = inClipSpace(a, stage);
                boolean bInside = inClipSpace(b, stage);

                if (aInside && bInside) {
                    newPolygon.add(b);
                    newUVs.add(bUV);
                } else if (!aInside && bInside) {
                    Vector2D newUV = new Vector2D(0, 0);
                    newPolygon.add(getClipIntersect(b, a, stage, bUV, aUV, newUV));
                    newUVs.add(newUV);

                    newPolygon.add(b);
                    newUVs.add(bUV);
                } else if (aInside && !bInside) {
                    Vector2D newUV = new Vector2D(0, 0);
                    newPolygon.add(getClipIntersect(a, b, stage, aUV, bUV, newUV));
                    newUVs.add(newUV);
                }
            }

            resultingPolygon = newPolygon;
            resultingUVs = newUVs;
        }

        // Now that the triangle is clipped, you can convert the vertices to NDC space
        for (Vector4D vec : resultingPolygon) {
            vec.x /= vec.w;
            vec.y /= vec.w;
            vec.z /= vec.w;
        }

        // Cut the polygon into triangles. Add each one of them to the result
        for (int i = 2; i < resultingPolygon.size(); i++) {
            Vector4D v1 = resultingPolygon.get(0);
            Vector4D v2 = resultingPolygon.get(i - 1);
            Vector4D v3 = resultingPolygon.get(i);

            Vector3D[] newVertices = { new Vector3D(v1.x, v1.y, v1.z),
                    new Vector3D(v2.x, v2.y, v2.z),
                    new Vector3D(v3.x, v3.y, v3.z) };

            Vector3D newWs = new Vector3D(v1.w, v2.w, v3.w);

            Vector2D uv1 = resultingUVs.get(0);
            Vector2D uv2 = resultingUVs.get(i - 1);
            Vector2D uv3 = resultingUVs.get(i);
            Vector2D[] uvsHere = { uv1, uv2, uv3 };

            trianglesOut.add(new Triangle(newVertices, uvsHere, triangle.material));
            triangleWOut.add(newWs);
        }
    }

    /**
     * Get the point cutting through 
     * @param a
     * @param b
     * @param stage
     * @param uvA
     * @param uvB
     * @param uvOut
     * @return
     */
    private Vector4D getClipIntersect(Vector4D a, Vector4D b, int stage, Vector2D uvA, Vector2D uvB, Vector2D uvOut) {
        double aDist = 1; // Distance from a to intersect * a.length

        double minBoundA = -a.w;
        double maxBoundA = a.w;
        double minBoundB = -b.w;
        double maxBoundB = b.w;

        if (stage == 0)
            aDist = Math.min(aDist, distToClip(a.x, b.x, minBoundA, minBoundB));
        if (stage == 1)
            aDist = Math.min(aDist, distToClip(a.x, b.x, maxBoundA, maxBoundB));

        if (stage == 2)
            aDist = Math.min(aDist, distToClip(a.y, b.y, minBoundA, minBoundB));
        if (stage == 3)
            aDist = Math.min(aDist, distToClip(a.y, b.y, maxBoundA, maxBoundB));

        if (stage == 4)
            aDist = Math.min(aDist, distToClip(a.z, b.z, minBoundA, minBoundB));
        if (stage == 5)
            aDist = Math.min(aDist, distToClip(a.z, b.z, maxBoundA, maxBoundB));

        if (stage == 6)
            aDist = Math.min(aDist, distToClip(a.w, b.w, 0, 0));

        Vector4D ans = a.add((b.add(a.mul(-1))).mul(aDist));
        Vector2D tempUVOut = uvA.add((uvB.add(uvA.mul(-1))).mul(aDist));
        uvOut.x = tempUVOut.x;
        uvOut.y = tempUVOut.y;
        return ans;
    }

    private boolean inClipSpace(Vector4D vec, int stage) {

        switch (stage) {
            case 0:
                return vec.x >= -vec.w;
            case 1:
                return vec.x <= vec.w;
            case 2:
                return vec.y >= -vec.w;
            case 3:
                return vec.y <= vec.w;
            case 4:
                return vec.z >= -vec.w;
            case 5:
                return vec.z <= vec.w;
            case 6:
                return vec.w > 0;
        }

        return false;
    }

    private static double distToClip(double a, double b, double boundA, double boundB) {
        double d1 = (a - boundA);
        double d2 = (b - boundB);
        return d1 / (d1 - d2);
    }

    ///
    /// Section 3: Triangle rasterization
    ///

    private void rasterizeTriangle(Triangle triangle, Vector3D wValues, int triangleIndex,
        int[][] triangleIndexes, double[][] depthBuffer, Vector3D[][] baryCoordsOfPixels) {
        Vector3D v1 = triangle.vertices[0];
        Vector3D v2 = triangle.vertices[1];
        Vector3D v3 = triangle.vertices[2];

        double w1 = wValues.x;
        double w2 = wValues.y;
        double w3 = wValues.z;

        // Transform the triangle to pixel coordinates, based on (https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/projection-stage.html)
        v1.x = (v1.x + 1) / 2 * width;
        v2.x = (v2.x + 1) / 2 * width;
        v3.x = (v3.x + 1) / 2 * width;

        v1.y = (1 - v1.y) / 2 * height;
        v2.y = (1 - v2.y) / 2 * height;
        v3.y = (1 - v3.y) / 2 * height;

        v1.z = -v1.z;
        v2.z = -v2.z;
        v3.z = -v3.z;

        // Reject if the triangle is back-facing
        if (MathUtility.edgeFunction(new Vector2D(v1.x, v1.y), v2, v3) > 0) {
            return;
        }

        // Find the bounding box of the triangle
        double xMin = Math.min(Math.min(v1.x, v2.x), v3.x);
        double xMax = Math.max(Math.max(v1.x, v2.x), v3.x);

        double yMin = Math.min(Math.min(v1.y, v2.y), v3.y);
        double yMax = Math.max(Math.max(v1.y, v2.y), v3.y);

        int xMinPixel = MathUtility.clamp((int) Math.floor(xMin), 0, width);
        int xMaxPixel = MathUtility.clamp((int) Math.ceil(xMax), 0, width);

        int yMinPixel = MathUtility.clamp((int) Math.floor(yMin), 0, height);
        int yMaxPixel = MathUtility.clamp((int) Math.ceil(yMax), 0, height);

        // Iterate over the bounding box, marking every pixel that's inside the triangle
        for (int x = xMinPixel; x < xMaxPixel; x++) {
            for (int y = yMinPixel; y < yMaxPixel; y++) {
                Vector2D point = new Vector2D(x, y);
                // Compute the barycentric coordinates of the point, relative to the vertices of the triangle
                Vector3D baryCoords = getBaryCoords(point, v1, v2, v3);

                // If the point is in the triangle, compute where exactly
                boolean pointInTriangle = (baryCoords.x >= 0) && (baryCoords.y >= 0) && (baryCoords.z >= 0);
                if (pointInTriangle) {

                    Vector3D newBaryCoords = new Vector3D(0, 0, 0);
                    newBaryCoords.x = (baryCoords.x / w1)
                            / (baryCoords.x / w1 + baryCoords.y / w2 + baryCoords.z / w3);
                    newBaryCoords.y = (baryCoords.y / w2)
                            / (baryCoords.x / w1 + baryCoords.y / w2 + baryCoords.z / w3);
                    newBaryCoords.z = (baryCoords.z / w3)
                            / (baryCoords.x / w1 + baryCoords.y / w2 + baryCoords.z / w3);

                    double oneOverZ = (1 / v1.z) * baryCoords.x + (1 / v2.z) * baryCoords.y
                            + (1 / v3.z) * baryCoords.z;
                    double z = 1 / oneOverZ;

                    // If this pixel is the closest one to the screen found so far, collect it
                    if (z < 1 && (z > depthBuffer[x][y] || depthBuffer[x][y]== 0)) {
                        depthBuffer[x][y] = z;
                        triangleIndexes[x][y] = triangleIndex + 1;
                        baryCoordsOfPixels[x][y] = newBaryCoords;
                    }
                }
            }
        }
    }

    private Vector3D getBaryCoords(Vector2D point, Vector3D v1, Vector3D v2, Vector3D v3) { // (https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle)
        double area = MathUtility.edgeFunction(new Vector2D(v1.x, v1.y), v2, v3);
        double d1 = MathUtility.edgeFunction(point, v2, v3) / area; // Closeness to v1
        double d2 = MathUtility.edgeFunction(point, v3, v1) / area; // Closeness to v2
        double d3 = MathUtility.edgeFunction(point, v1, v2) / area; // Closeness to v3

        return new Vector3D(d1, d2, d3);
    }

    ///
    /// Section 4: Shading
    ///

    private Color getColorOfPixel(int x, int y, 
    Vector3D[][] baryCoordsOfPixels, int[][] triangleIndexes, ArrayList<Triangle> transformedTriangles) {
        Color color = null;
        if (triangleIndexes[x][y] != 0) {

            Triangle triangle = transformedTriangles.get(triangleIndexes[x][y]- 1);
            Vector3D baryCoords = baryCoordsOfPixels[x][y];

            double someU = triangle.uvs[0].x * baryCoords.x + triangle.uvs[1].x * baryCoords.y
                    + triangle.uvs[2].x * baryCoords.z;
            double someV = triangle.uvs[0].y * baryCoords.x + triangle.uvs[1].y * baryCoords.y
                    + triangle.uvs[2].y * baryCoords.z;

            if (someU < 0 || someU > 1 || someV > 1 || someV < 0) {
                color = new Color(0, 0, 0);
            } else {
                color = triangle.material.getcolor(new Vector2D(someU, someV));
            }

        } else {
            color = Constants.SKYBOX_COLOR;
        }

        return color;
    }
}
