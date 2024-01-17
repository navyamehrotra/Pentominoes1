package Engine3DStuff;

import java.util.ArrayList;

import Engine3DStuff.customdatatypes.CameraKeyframe;
import Engine3DStuff.customdatatypes.Matrix4x4;
import Engine3DStuff.customdatatypes.Vector3D;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Camera extends JPanel{
    private Vector3D position;
    private Vector3D rotation;

    private Matrix4x4 positionMatrix;
    private Matrix4x4 rotationMatrix;

    private ArrayList<CameraKeyframe> keyframes = new ArrayList<>();

    private int currentKeyframe = 0;
    private double animationStart = 0;

    private boolean atRest = false;

    private int firstX;
    private int firstY;
    private boolean mouseDrag = false;

    public Camera() {

        
     }

     

    public void animate() {
        if (atRest) {
            return;
        }

        double animationProgress = 0;

        do {
            if (currentKeyframe == keyframes.size() - 1) {
                atRest = true;
                animationProgress = 0;
                break;
            }

            double totalTime = (System.currentTimeMillis() - animationStart) / 1000;
            animationProgress = totalTime / keyframes.get(currentKeyframe).afterTransitionTime;
            if (animationProgress > 1) {
                animationStart += keyframes.get(currentKeyframe).afterTransitionTime * 1000;
                currentKeyframe++;
            }
        } while(animationProgress > 1);


        CameraKeyframe keyframe = keyframes.get(currentKeyframe);
        if (currentKeyframe == keyframes.size() - 1) {
            position = keyframe.cameraPosition;
            rotation = keyframe.cameraRotation;
        } else {
            CameraKeyframe nextKeyframe = keyframes.get(currentKeyframe + 1);
            position = Vector3D.lerp(keyframe.cameraPosition, nextKeyframe.cameraPosition, animationProgress);            
            rotation = Vector3D.lerp(keyframe.cameraRotation, nextKeyframe.cameraRotation, animationProgress);
        }

        positionMatrix = Matrix4x4.getTranslationMatrix(position);        
        rotationMatrix = Matrix4x4.getRotationMatrix(rotation);
    }

    public Matrix4x4 getPositionMatrix() {
        return positionMatrix;
    }

    public Matrix4x4 getRotationMatrix() {
        return rotationMatrix;
    }

    public boolean isAtRest() {
        return atRest;
    }

    public void appendKeyframes(CameraKeyframe keyframe) {
        ArrayList<CameraKeyframe> arrayList = new ArrayList<>();
        arrayList.add(keyframe);

        appendKeyframes(arrayList);
    }

    public void appendKeyframes(ArrayList<CameraKeyframe> keyFramesToAdd) {
        for (CameraKeyframe keyframe : keyFramesToAdd) {
            keyframes.add(keyframe);
        }

        if (atRest) {
            atRest = false;
            animationStart = System.currentTimeMillis();
        }
    }
    
}
