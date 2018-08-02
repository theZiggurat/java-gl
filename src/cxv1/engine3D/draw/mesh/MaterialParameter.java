package cxv1.engine3D.draw.mesh;

import cxv1.engine3D.draw.Texture;

public class MaterialParameter {

    private MaterialParameterType type;


    private Texture texture;
    private boolean isTextured;

    MaterialParameter(MaterialParameterType _type){
        this.type = _type;
    }

    public MaterialParameterType getType(){
        return type;
    }

    public boolean isTextured(){
        return isTextured;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        isTextured = true;
    }
}
