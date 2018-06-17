package cxv1.engine3D.entity;

import cxv1.engine3D.draw.Mesh;
import org.joml.Vector3f;

public interface Entity {

    public void cleanup();
    public Mesh getMesh();
    public Vector3f getPos();
    public Vector3f getRot();
    public float getScale();
    public void setPos(Vector3f pos);
    public void setPos(float x, float y, float z);
    public void setRot(Vector3f rot);
    public void setRot(float x, float y, float z);
    public void setScale(float scale);

}
