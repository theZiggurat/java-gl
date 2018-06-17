package cxv1.engine3D.draw;

import org.joml.Vector4f;

public class Material {

    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f ,1.0f, 1.0f, 1.0f);
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private float reflectance;
    private Texture texture;
    boolean isTextured;

    public Material(){
        this.ambientColor = DEFAULT_COLOR;
        this.diffuseColor = DEFAULT_COLOR;
        this.specularColor = DEFAULT_COLOR;
        this.texture = null;
        isTextured = false;
        this.reflectance = 0;
    }

    public Material(Vector4f ambientColor, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColour;
        this.specularColor = specularColour;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, 0);
    }

    public Vector4f getAmbientColor() { return ambientColor; }
    public void setAmbientColor(Vector4f ambientColor) { this.ambientColor = ambientColor; }
    public Vector4f getDiffuseColor() { return diffuseColor; }
    public void setDiffuseColor(Vector4f diffuseColor) { this.diffuseColor = diffuseColor; }
    public Vector4f getSpecularColor() { return specularColor; }
    public void setSpecularColor(Vector4f specularColor) { this.specularColor = specularColor; }
    public float getReflectance() { return reflectance; }
    public void setReflectance(float reflectance) { this.reflectance = reflectance; }
    public Texture getTexture() { return texture; }
    public void setTexture(Texture texture) { this.texture = texture; isTextured = true; }
    public boolean isTextured(){return isTextured;}

}
