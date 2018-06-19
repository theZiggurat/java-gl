package cxv1.engine3D.entity;

import cxv1.engine3D.draw.mesh.Mesh;
import org.joml.Vector3f;

public interface Entity {

    void cleanup();
    Mesh getMesh();
    Vector3f getPos();
    Vector3f getRot();
    float getScale();
    void setPos(Vector3f pos);
    void setPos(float x, float y, float z);
    void setRot(Vector3f rot);
    void setRot(float x, float y, float z);
    void setScale(float scale);

}
