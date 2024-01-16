package Engine3DStuff.customdatatypes;

public class Matrix4x4 {
    public double[][] vals;

    public Matrix4x4() {
        vals = new double[4][4];
    }

    public Vector4D mul(Vector4D point) {
        // Based on (https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/building-basic-perspective-projection-matrix.html) 
        Vector4D out = new Vector4D(0, 0, 0, 0);
        out.x   = point.x * vals[0][0] + point.y * vals[1][0] + point.z * vals[2][0] + /* point.z = 1 */ vals[3][0]; 
        out.y   = point.x * vals[0][1] + point.y * vals[1][1] + point.z * vals[2][1] + /* point.z = 1 */ vals[3][1]; 
        out.z   = point.x * vals[0][2] + point.y * vals[1][2] + point.z * vals[2][2] + /* point.z = 1 */ vals[3][2]; 
        out.w = point.x * vals[0][3] + point.y * vals[1][3] + point.z * vals[2][3] + /* point.z = 1 */ vals[3][3]; 
    
        return out;
    } 

    public static Matrix4x4 getTranslationMatrix(Vector3D translation) {
        Matrix4x4 viewToCamera = new Matrix4x4();

        viewToCamera.vals[0][0] = 1;        
        viewToCamera.vals[1][1] = 1;
        viewToCamera.vals[2][2] = 1;
        viewToCamera.vals[3][3] = 1;

        // Position, based on (http://www.euclideanspace.com/maths/geometry/affine/matrix4x4/index.htm)
        viewToCamera.vals[3][0] = translation.x;
        viewToCamera.vals[3][1] = translation.y;
        viewToCamera.vals[3][2] = translation.z;

        return viewToCamera;
    }

    public static Matrix4x4 getRotationMatrix(Vector3D rotation) {
        Matrix4x4 viewToCamera = new Matrix4x4();

        // Rotation, based on (https://en.wikipedia.org/wiki/Rotation_matrix)
        viewToCamera.vals[0][0] = Math.cos(rotation.x) * Math.cos(rotation.y);
        viewToCamera.vals[1][0] = Math.sin(rotation.x) * Math.cos(rotation.y);
        viewToCamera.vals[2][0] = -Math.sin(rotation.y);

        viewToCamera.vals[0][1] = Math.cos(rotation.x) * Math.sin(rotation.y) * Math.sin(rotation.z) - Math.sin(rotation.x) * Math.cos(rotation.z);
        viewToCamera.vals[1][1] = Math.sin(rotation.x) * Math.sin(rotation.y) * Math.sin(rotation.z) + Math.cos(rotation.x) * Math.cos(rotation.z);
        viewToCamera.vals[2][1] = Math.cos(rotation.y) * Math.sin(rotation.z);

        viewToCamera.vals[0][2] = Math.cos(rotation.x) * Math.sin(rotation.y) * Math.cos(rotation.z) + Math.sin(rotation.x) * Math.sin(rotation.z);
        viewToCamera.vals[1][2] = Math.sin(rotation.x) * Math.sin(rotation.y) * Math.cos(rotation.z) - Math.cos(rotation.x) * Math.sin(rotation.z);
        viewToCamera.vals[2][2] = Math.cos(rotation.y) * Math.cos(rotation.z);

        // Fourth column blank
        viewToCamera.vals[0][3] = 0;
        viewToCamera.vals[1][3] = 0;
        viewToCamera.vals[2][3] = 0;
        viewToCamera.vals[3][3] = 1;

        return viewToCamera;
    }

    public static Matrix4x4 getWorldToCameraMatrix(Vector3D position, Vector3D rotation) {
        Matrix4x4 viewToCamera = new Matrix4x4();

        // Rotation, based on (https://en.wikipedia.org/wiki/Rotation_matrix)
        viewToCamera.vals[0][0] = Math.cos(rotation.x) * Math.cos(rotation.y);
        viewToCamera.vals[1][0] = Math.sin(rotation.x) * Math.cos(rotation.y);
        viewToCamera.vals[2][0] = -Math.sin(rotation.y);

        viewToCamera.vals[0][1] = Math.cos(rotation.x) * Math.sin(rotation.y) * Math.sin(rotation.z) - Math.sin(rotation.x) * Math.cos(rotation.z);
        viewToCamera.vals[1][1] = Math.sin(rotation.x) * Math.sin(rotation.y) * Math.sin(rotation.z) + Math.cos(rotation.x) * Math.cos(rotation.z);
        viewToCamera.vals[2][1] = Math.cos(rotation.y) * Math.sin(rotation.z);

        viewToCamera.vals[0][2] = Math.cos(rotation.x) * Math.sin(rotation.y) * Math.cos(rotation.z) + Math.sin(rotation.x) * Math.sin(rotation.z);
        viewToCamera.vals[1][2] = Math.sin(rotation.x) * Math.sin(rotation.y) * Math.cos(rotation.z) - Math.cos(rotation.x) * Math.sin(rotation.z);
        viewToCamera.vals[2][2] = Math.cos(rotation.y) * Math.cos(rotation.z);

        // Position, based on (http://www.euclideanspace.com/maths/geometry/affine/matrix4x4/index.htm)
        viewToCamera.vals[3][0] = position.x;
        viewToCamera.vals[3][1] = position.y;
        viewToCamera.vals[3][2] = position.z;

        // Need to to this, so that it's Position - Rotation, and not Rotation - Position
        viewToCamera.vals[0][0] += position.x * viewToCamera.vals[0][3];
        viewToCamera.vals[1][0] += position.x * viewToCamera.vals[1][3];
        viewToCamera.vals[2][0] += position.x * viewToCamera.vals[2][3];

        viewToCamera.vals[0][1] += position.y * viewToCamera.vals[0][3];
        viewToCamera.vals[1][1] += position.y * viewToCamera.vals[1][3];
        viewToCamera.vals[2][1] += position.y * viewToCamera.vals[2][3];

        viewToCamera.vals[0][2] += position.z * viewToCamera.vals[0][3];
        viewToCamera.vals[1][2] += position.z * viewToCamera.vals[1][3];
        viewToCamera.vals[2][2] += position.z * viewToCamera.vals[2][3];

        // Fourth column blank
        viewToCamera.vals[0][3] = 0;
        viewToCamera.vals[1][3] = 0;
        viewToCamera.vals[2][3] = 0;
        viewToCamera.vals[3][3] = 1;

        return viewToCamera;
    }

    public static Matrix4x4 getIdentityMatrix() {
        Matrix4x4 identity = new Matrix4x4();
        identity.vals[0][0] = 1;
        identity.vals[1][1] = 1;
        identity.vals[2][2] = 1;
        identity.vals[3][3] = 1;

        return identity;
    }

    public static Matrix4x4 getProjectionMatrix(double width, double height) { 
        // Based on (https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/building-basic-perspective-projection-matrix.html)
        Matrix4x4 projectionMatrix = new Matrix4x4();

        // set the basic projection matrix
        double scale = 1 / Math.tan(Constants.FOV * 0.5 * Math.PI / 180); 
        projectionMatrix.vals[0][0] = scale / (width / height);  //scale the x coordinates of the projected point 
        projectionMatrix.vals[1][1] = scale;  //scale the y coordinates of the projected point 
        projectionMatrix.vals[2][2] = -Constants.FAR_PLANE / (Constants.FAR_PLANE - Constants.NEAR_PLANE);  //used to remap z to [0,1] 
        projectionMatrix.vals[3][2] = -Constants.FAR_PLANE * Constants.NEAR_PLANE / (Constants.FAR_PLANE - Constants.NEAR_PLANE);  //used to remap z [0,1] 
        projectionMatrix.vals[2][3] = -1;  //set w = -z 
        projectionMatrix.vals[3][3] = 0; 

        return projectionMatrix;
    } 
}
