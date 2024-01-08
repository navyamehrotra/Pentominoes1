package Engine3DStuff.customdatatypes;

import java.util.ArrayList;

public class Level {
    // Camera's state during this level
    public CameraKeyframe cameraStart;

    // Start ball position
    public Vector3D ballRespawnPoint;

    // When the ball is completionPointDistance the completionPoint, the level is won
    public Vector3D completionPoint;
    public double completionPointDistance; 
    
    // The camera will animate through these states when transitioning to the next level. 
    // If this is empty, it will just transition straight to the next level's state.
    public ArrayList<CameraKeyframe> cameraTransition;

    public Level(CameraKeyframe cameraStart,  
        Vector3D ballRespawnPoint, 
        Vector3D completionPoint, double completionPointDistance, 
        ArrayList<CameraKeyframe> cameraTransition) { 
        this.cameraStart = cameraStart;
        this.ballRespawnPoint = ballRespawnPoint;
        this.completionPoint = completionPoint;
        this.completionPointDistance = completionPointDistance;
        this.cameraTransition = cameraTransition;
    }
}
