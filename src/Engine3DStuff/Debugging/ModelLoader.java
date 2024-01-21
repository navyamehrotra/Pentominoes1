package Engine3DStuff.Debugging;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.imageio.ImageIO;

import Engine3DStuff.customdatatypes.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import Engine3DStuff.Object3D;

public class ModelLoader {
    public static Vector<Object3D> loadOBJ(String fileName) {
        // Code written based on .obj file specification (https://en.wikipedia.org/wiki/Wavefront_.obj_file)
        //Path path = Paths.get("", "models\\" + fileName + ".obj");
        //File file = new File(path.toAbsolutePath().toString());

        Dictionary<String, Material> materialDictionary = new Hashtable<String, Material>();
        Material currentMaterial = new Material();
        
        Vector<Object3D> modelsRead = new Vector<Object3D>();

        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName + ".obj");
            Scanner scanner = new Scanner(stream);
            Object3D currentModel = null;
            Vector<Vector3D> currentVertices = new Vector<Vector3D>();
            Vector<Vector2D> currentUvs = new Vector<Vector2D>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("o ")) { // A new model
                    currentModel = new Object3D();

                    modelsRead.add(currentModel);
                }
                else if (line.startsWith("v ")) { // A new vertex
                    Vector<Double> numbers = getNumbers(line);

                    if (numbers.size() >= 3) {
                        currentVertices.add(new Vector3D(numbers.elementAt(0), numbers.elementAt(1), numbers.elementAt(2)));
                    }
                }
                else if (line.startsWith("vt ")) { // A new uv coordinate
                    Vector<Double> numbers = getNumbers(line);

                    if (numbers.size() >= 2) {
                        currentUvs.add(new Vector2D(numbers.elementAt(0), numbers.elementAt(1)));
                    }
                }
                else if (line.startsWith("f ")) { // A new face (we only accept triangles)
                    Vector<Double> numbers = getNumbers(line);
                    Vector3D[] thisFacesVertices = new Vector3D[3];                    
                    Vector2D[] thisFacesUvs = new Vector2D[3];
                    
                    for (int i = 0; i < 3; i++) {
                        thisFacesVertices[i] = currentVertices.elementAt(((int)numbers.elementAt(i * 3).doubleValue()) - 1);                        
                        thisFacesUvs[i] = currentUvs.elementAt(((int)numbers.elementAt(i * 3 + 1).doubleValue()) - 1);
                    } 

                    Triangle face = new Triangle(thisFacesVertices, thisFacesUvs, currentMaterial);
                    currentModel.triangles.add(face);
                }
                else if (line.startsWith("mtllib ")) { // A new mtl (material definition file)
                    String mtlFileName = line.substring(7);
                    loadMTL(mtlFileName, materialDictionary);
                }
                else if (line.startsWith("usemtl ")) { // Specify the material to be used from now on
                    String materialName = line.substring(7);
                    if (((Hashtable<String, Material>)materialDictionary).containsKey(materialName)) {
                        currentMaterial = materialDictionary.get(materialName);
                    }
                }
            }

            scanner.close();
        }
        catch (Exception e) {
            return null;
        }

        return modelsRead;
    }

    private static void loadMTL(String mtlFileName, Dictionary<String, Material> materialDictionary) {
        //Path path = Paths.get("", "models\\" + mtlFileName);
        //File file = new File(path.toAbsolutePath().toString());

        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(mtlFileName);
            Scanner scanner = new Scanner(stream);
            Material currenMaterial = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("newmtl ")) { // A new material
                    currenMaterial = new Material();
                    materialDictionary.put(line.substring(7), currenMaterial);
                }
                else if (line.startsWith("Kd ")) { // Diffuse color
                    Vector<Double> numbers = getNumbers(line);
                    currenMaterial.color = new Color(d2I(numbers.elementAt(0)), 
                        d2I(numbers.elementAt(1)), 
                        d2I(numbers.elementAt(2)));
                }
                else if (line.startsWith("map_Kd ")) { // Diffuse texture
                    String textureDefinition = line.substring(7);

                    if (textureDefinition.startsWith("-s ")) { // Texture with custom scale
                        Vector<Double> numbers = getNumbers(textureDefinition);
                        if (numbers.size() >= 3) {
                             currenMaterial.textureScale = new Vector3D(numbers.elementAt(0), numbers.elementAt(1), numbers.elementAt(2));
                        }

                        Vector<Integer> spaces = new Vector<Integer>();
                        for (int i = 0; i < textureDefinition.length(); i++) {
                            if (textureDefinition.charAt(i) == ' ') {
                                spaces.add(i);
                            }
                        }

                        currenMaterial.texture = loadTexture(textureDefinition.substring(spaces.elementAt(3) + 1, textureDefinition.length()));
                    }
                    else { // Default scale texture
                        currenMaterial.texture = loadTexture(textureDefinition);
                    }
                }
            }

            scanner.close();
        }
        catch (Exception e) {
            return;
        }
    }

    public static BufferedImage loadTexture(String textureFileName) {
        //Path path = Paths.get("", "models\\" + textureFileName);
        //File file = new File(path.toAbsolutePath().toString());

        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(textureFileName);
        if (stream == null) {
            return null;
        }
        
        try {
            return ImageIO.read(stream);
        }
        catch (IOException e) {
            return null;
        }
    }

    private static int d2I(double d) { // Converts from <0, 1> double to <0, 255> int
        return (int)Math.round(d * 255);
    }

    private static Vector<Double> getNumbers(String text) {
        Vector<Double> values = new Vector<Double>();

        for (String str : text.split("[ /]")) {
            try {
                double value = Double.parseDouble(str);
                values.add(value);
            }
            catch (NumberFormatException e) { }
        }

        return values;
    }
}
