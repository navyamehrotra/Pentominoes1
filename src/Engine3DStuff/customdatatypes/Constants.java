package Engine3DStuff.customdatatypes;
import java.awt.Color;

public class Constants {
    // Input constants
    public static final double INPUT_DELAY = 1.0; // The time that Jerry needs to remain still for the player to get input
    public static final double POSITION_DELTA_THRESHOLD = 0.01; // Jerry is considered still, if he moves less than this distance per frame
    public static final double ROTATION_DELTA_THRESHOLD = 0.01; // Jerry is considered still, if he rotates less than this distance per frame
    public static final double MAX_FORCE = 15; // The maximum velocity increment (applied if the player inputs the highest force possible)

    // Render constants
    public static final int IMAGE_WIDTH = 150;    
    public static final int IMAGE_HEIGHT = 35;
    public static final Color SKYBOX_COLOR = new Color(255, 255, 255);//new Color(41, 184, 219); // Color rendered if nothing is visible on a certain pixel
    public static final double FOV = 60;
    public static final double NEAR_PLANE = 0.01; // Triangles' pixels need to be at least this far away to be rendered
    public static final double FAR_PLANE = 200; // Triangles' pixels need to be less than this far away to be rendered
    public static final double FRAMERATE_LOCK = 500;
    
    // Physics Constants. There is only one dynamic bodies, so they can all be set here
    public static final double PHYSICS_STEP_TIME = 10; // A single physics frame will take this many milliseconds
    public static final double BOUNCINESS = 0.9;
    public static final double DRAG = 0.5;
    public static final double FRICTION = 0.9;
    public static final double MASS = 0.9;    
    public static final double MOMENT_OF_INERTIA = 0.9;    
    public static final double RADIUS = 0.9;
    public static final double MAX_VELOCITY = 25;

    // Miscellaneous
    public static final double FPS_UPDATE = 500; // Update the FPS counter every FPS_UPDATE milliseconds
}
