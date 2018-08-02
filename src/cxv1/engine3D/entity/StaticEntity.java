package cxv1.engine3D.entity;

import cxv1.engine3D.draw.mesh.Mesh;
import cxv1.engine3D.draw.mesh.Mesh3D;
import org.joml.Vector3f;

public class StaticEntity implements Entity {

    private Mesh3D mesh3D;
    private Vector3f pos, rot;
    private float scale;

    public StaticEntity(){
        pos = new Vector3f(0,0,0);
        rot = new Vector3f(0,0,0);
        scale =  1;
    }

    public StaticEntity(Mesh3D mesh3D){
        this();
        this.mesh3D = mesh3D;
    }
    public void cleanup(){
        mesh3D.cleanup();
    }
    public Mesh getMesh(){return mesh3D;}
    public void setMesh3D(Mesh3D mesh3D){this.mesh3D = mesh3D;}
    public Vector3f getPos(){return pos;}
    public Vector3f getRot(){return rot;}
    public float getScale(){return scale;}
    public void setPos(Vector3f pos){this.pos = pos;}
    public void setPos(float x, float y, float z){ pos.x = x; pos.y = y; pos.z = z; }
    public void setRot(Vector3f rot){this.rot = rot;}
    public void setRot(float x, float y, float z){ rot.x = x; rot.y = y; rot.z = z; }
    public void setScale(float scale){this.scale = scale;}

    public void debug(){
        System.out.println("PlayerPos: "
                + getPos().x + " "
                + getPos().y + " "
                + getPos().z);
        System.out.println("PlayerRot: "
                + getRot().x + " "
                + getRot().y + " "
                + getRot().z);
    }

}
