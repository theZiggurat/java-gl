package v1.engine.draw.mesh;

import v1.engine.draw.Texture;
import v1.engine.entity.Terrain;
import v1.engine.util.ShaderUtil;

import java.util.ArrayList;
import java.util.List;

public class Mesh2D implements Mesh{

    private Texture texture;
    private int vaoId;
    private final List<Integer> vboList;
    private String id;
    private float scale;

    public Mesh2D(String id, float[] positions, float[] textureCoords,
                  int[] indices, Texture texture){
        this.id = id;
        this.texture = texture;
        vboList = new ArrayList<>();

    }

    public void render(ShaderUtil shaderUtil){}
    public void render(){}
    public void render(Terrain terrain){}

    public int getVaoId(){
        return vaoId;
    }

    public List<Integer> getVboList(){
        return vboList;
    }



}
