package v1.engine.draw.mesh;

import v1.engine.draw.Texture;
import org.joml.Vector4f;

public class MaterialParameter {

    public static final int PARAMETER_TYPES = 9;

    public static final int DIFFUSE = 0; // (Kd, map_Kd) Vec4 (0 to 1)
    public static final int AMBIENT = 1; // (Ka, map_Ka)
    public static final int SPECULAR = 3; // (Ks, map_Ks)
    public static final int EMISSIVE = 4; // (Ke, map_Ke)
    public static final int NORMAL = 5; // (norm, map_norm) Vec4 (0 to 1)
    public static final int SPECULAR_EXPONENT = 6; // (Ns) float (0 to 1000)
    public static final int OPTICAL_DENSITY = 7; // (Ni) float (1 normal to 10 refracted)
    public static final int DISSOLVE = 8; // (d) float (0.0 (dissolved) to 1.0 (opaque))

    private int type;

    // three possible data types
    private Texture texture_data;
    private Vector4f vector_data;
    private float float_data;

    private boolean isTextured;

    public MaterialParameter(int _type){
        this.type = _type;
        vector_data = new Vector4f(0,0,0,0);
        float_data = 1f;
    }

    public int getType(){
        return type;
    }

    public boolean isTextured() {
        return isTextured;
    }

    public void setData(Texture _texture) {
        this.texture_data = _texture;
        isTextured = true;
    }
    public void setData(float _f){
        this.float_data = _f;
    }
    public void setData(Vector4f _vector){
        this.vector_data = _vector;
    }

    public Texture getTexture() {
        return texture_data;
    }

    public float getFloat(){
        return float_data;
    }
    public Vector4f getVector(){
        return vector_data;
    }

}
