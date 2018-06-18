package cxv1.engine3D.draw.mesh;

import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.util.ShaderUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mesh2D implements Mesh{

    private Texture texture;
    private int vaoId;
    private final List<Integer> vboList;
    private String id;

    public Mesh2D(String id, float[] positions, float[] textureCoords,
                  int[] indices, Texture texture){
        this.id = id;
        this.texture = texture;
        vboList = new ArrayList<>();

    }

    public void render(ShaderUtil shaderUtil){

    }

    public int getVaoId(){
        return vaoId;
    }

    public List<Integer> getVboList(){
        return vboList;
    }



}
