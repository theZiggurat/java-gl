package v1.engine.util;

import v1.engine.draw.Camera;
import v1.engine.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private final Matrix4f viewMatrix;
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f orthoMatrix;

    public Transformation(){
        viewMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        orthoMatrix = new Matrix4f();
    }

    public Matrix4f getViewMatrix(Camera camera){
        Vector3f pos = camera.getPosition(), rot = camera.getRotation();

        viewMatrix.identity();
        viewMatrix.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(rot.z), new Vector3f(0,0,1));

        viewMatrix.translate(-pos.x, -pos.y, -pos.z);
        return viewMatrix;
    }

    // 3D PROJECTIONS

    public final Matrix4f getProjectionMatrix(float fov, float width, float height,
                                              float z_near, float z_far){
        float aspectRatio = width/height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, z_near, z_far);
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
        Vector3f rotation = entity.getRot();
        modelViewMatrix.identity().translate(entity.getPos()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(entity.getScale());
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    // 2D PROJECTIONS

    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top){
        orthoMatrix.identity();
        orthoMatrix.setOrtho2D(left, right, bottom, top);
        return orthoMatrix;
    }

    public  Matrix4f getOrthoModelMatrix(Entity entity, Matrix4f orthoMatrix){
        Vector3f rotation = entity.getRot();
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.identity().translate(entity.getPos())
                .rotateX((float)Math.toRadians(-rotation.x))
                .rotateY((float)Math.toRadians(-rotation.y))
                .rotateZ((float)Math.toRadians(-rotation.z))
                .scale(entity.getScale());
        Matrix4f curr = new Matrix4f(orthoMatrix);
        curr.mul(modelMatrix);
        return curr;

    }
}
