package cxv1.engine3D.draw;

import org.joml.Vector4f;

public class Material {

    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f ,1.0f, 1.0f, 1.0f);

    /*
        ambient parameter (ka, map_Ka)
     */
    private Vector4f ambientColor;
    private boolean isAmbientMapped;
    private Texture ambientMap;

    /*
        diffuse parameter (kd, map_Kd)
     */
    private Vector4f diffuseColor;
    private boolean isDiffuseMapped;
    private Texture diffuseMap;

    /*
        specular parameter (ks, map_Ks)
     */
    private Vector4f specularColor;
    private boolean isSpecularMapped;
    private Texture specularMap;

    /*
        reflectance parameter (refl)
     */
    private float reflectance;
    private boolean isReflectionMapped;
    private Texture reflectionMap;

    /*
        transparency parameter (d or Tr)
      */
    private float transparency;
    private boolean isTransparent;
    private boolean isTransparentMapped;
    private Texture transparencyMap;

    /*
        illumination parameter (illum)
     */
    /*

     */


    /*
        normal parameter
     */

    private boolean isNormalMapped;
    private Texture normalMap;

    public Material(){

        this.ambientColor = DEFAULT_COLOR;
        this.diffuseColor = DEFAULT_COLOR;
        this.specularColor = DEFAULT_COLOR;

        this.diffuseMap = null;
        isDiffuseMapped = false;
        isNormalMapped = false;
        isAmbientMapped = false;
        isTransparentMapped = false;
        isSpecularMapped = false;
        isReflectionMapped = false;

        this.reflectance = 0;
    }



    public Vector4f getSpecularColor() { return specularColor; }
    public void setSpecularColor(Vector4f specularColor) { this.specularColor = specularColor; }
    public float getReflectance() { return reflectance; }
    public void setReflectance(float reflectance) { this.reflectance = reflectance; }

    /* Normal Methods */

    public Texture getNormalMap(){
        return normalMap;
    }
    public void setNormalMap(Texture _normalMap){
        this.normalMap = _normalMap;
        if(normalMap != null){
           isNormalMapped = true;
        }
    }
    public boolean isNormalMapped(){
        return isNormalMapped;
    }

    /*
         Diffuse Methods
     */
    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }
    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }
    public Texture getDiffuseMap() {
        return diffuseMap;
    }
    public void setDiffuseMap(Texture _diffuseMap) {
        this.diffuseMap = _diffuseMap;
        isDiffuseMapped = true;
    }
    public boolean isDiffuseMapped(){
        return isDiffuseMapped;
    }

    /*
        Transparency Methods
     */

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    /*
        Ambient methods
     */

    public Vector4f getAmbientColor() {
        return ambientColor;
    }
    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }
    public boolean isAmbientMapped() {
        return isAmbientMapped;
    }
    public Texture getAmbientMap() {
        return ambientMap;
    }
    public void setAmbientMap(Texture ambientMap) {
        this.ambientMap = ambientMap;
        isAmbientMapped = true;
    }

    public boolean isSpecularMapped() {
        return isSpecularMapped;
    }

    public Texture getSpecularMap() {
        return specularMap;
    }

    public void setSpecularMap(Texture specularMap) {
        this.specularMap = specularMap;
        isSpecularMapped = true;
    }


    public boolean isReflectionMapped() {
        return isReflectionMapped;
    }

    public void setReflectionMapped(boolean reflectionMapped) {
        isReflectionMapped = reflectionMapped;
    }

    public Texture getReflectionMap() {
        return reflectionMap;
    }

    public void setReflectionMap(Texture reflectionMap) {
        this.reflectionMap = reflectionMap;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    public boolean isTransparentMapped() {
        return isTransparentMapped;
    }

    public void setTransparentMapped(boolean transparentMapped) {
        isTransparentMapped = transparentMapped;
    }

    public Texture getTransparencyMap() {
        return transparencyMap;
    }

    public void setTransparencyMap(Texture transparencyMap) {
        this.transparencyMap = transparencyMap;
    }
}
