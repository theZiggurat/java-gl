package cxv1.engine3D.entity;

import cxv1.engine3D.draw.Mesh;
import org.joml.Vector3f;

public class FixedObjectEntity implements Entity{

    private Mesh mesh;
    private Vector3f pos, rot;
    private float scale;

    public FixedObjectEntity(){
        pos = new Vector3f(0,0,0);
        rot = new Vector3f(0,0,0);
        scale =  1;
    }

    public FixedObjectEntity(Mesh mesh){
        this();
        this.mesh = mesh;
    }
    public void cleanup(){
        mesh.cleanup();
    }
    public Mesh getMesh(){return mesh;}
    public void setMesh(Mesh mesh){this.mesh = mesh;}
    public Vector3f getPos(){return pos;}
    public Vector3f getRot(){return rot;}
    public float getScale(){return scale;}
    public void setPos(Vector3f pos){this.pos = pos;}
    public void setPos(float x, float y, float z){ pos.x = x; pos.y = y; pos.z = z; }
    public void setRot(Vector3f rot){this.rot = rot;}
    public void setRot(float x, float y, float z){ rot.x = x; rot.y = y; rot.z = z; }
    public void setScale(float scale){this.scale = scale;}

}
