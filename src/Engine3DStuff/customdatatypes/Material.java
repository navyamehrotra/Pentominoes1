package Engine3DStuff.customdatatypes;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Material {
    public Color color;
    public BufferedImage texture;
    public Vector3D textureScale;
    
    public Material() {
        
    }

    public Material(Color color) { 
        this.color = color;
    }

    public Material(BufferedImage texture) {
        this.texture = texture;
    }

    public Color getcolor(Vector2D point) {
        if (texture != null) {
            return sampleTexture(point, texture);
        }
        else if (color != null) {
            return color;
        }
        
        return new Color(255, 255, 255);
    }

    private Color sampleTexture(Vector2D point, BufferedImage texture) { // Input point's coords are in <0, 1>
        Vector2D scaledPoint = point;
        if (textureScale != null) {
            scaledPoint = new Vector2D((point.x / textureScale.x) % 1, (point.y / textureScale.y) % 1);
        }
        
        int colorInt = texture.getRGB((int)Math.round(scaledPoint.x * (texture.getWidth() - 1)), 
            (int)Math.round(scaledPoint.y * (texture.getHeight() - 1)));

        return new Color(colorInt);
    }
    
}
