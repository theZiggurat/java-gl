package v2.modules.pbr;

import v2.engine.system.Camera;
import v2.engine.system.Core;
import v2.engine.system.Shader;
import v2.engine.scene.ModuleNode;

import static org.lwjgl.opengl.GL13.*;

public class PBRShader extends Shader {

    /**
     * -Buffers uv-maps from object tangent space to screen space
     * Renders texture maps from object
     */

    private static PBRShader instance = null;

    public static PBRShader instance(){
        if(instance == null){
            instance = new PBRShader();
        }
        return instance;
    }

    private PBRShader()  {

        super();

        createVertexShader("res/shaders/pbr/pbr_vs.glsl");
        createFragmentShader("res/shaders/pbr/pbr_fs.glsl");
        link();

        addUniform("map_albedo");
        addUniform("albedoMap");
        addUniform("albedoConst");

        addUniform("map_normal");
        addUniform("normalMap");

        addUniform("map_roughness");
        addUniform("roughnessMap");
        addUniform("roughnessConst");

        addUniform("map_metal");
        addUniform("metalMap");
        addUniform("metalConst");

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");
        //addUniform("invViewMatrix");


    }

    @Override
    public void updateUniforms(ModuleNode group){

        Camera camera = Core.camera();

        PBRMaterial material = ((PBRModel)group).getMaterial();

        if(material.isAlbedoMapped()) {
            glActiveTexture(GL_TEXTURE0);
            material.getAlbedoMap().bind();
            setUniform("albedoMap", 0);
            setUniform("map_albedo", 1);
        } else {
            setUniform("albedoConst", material.getAlbedoConst());
            setUniform("map_albedo", 0);
        }

        if(material.isNormalMapped()){
            glActiveTexture(GL_TEXTURE1);
            material.getNormalMap().bind();
            setUniform("normalMap", 1);
            setUniform("map_normal", 1);
        } else {
            setUniform("map_normal", 0);
        }

        if(material.isRoughnessMapped()){
            glActiveTexture(GL_TEXTURE2);
            material.getRoughnessMap().bind();
            setUniform("roughnessMap", 2);
            setUniform("map_roughness", 1);
        } else {
            setUniform("roughnessConst", material.getRoughnessConst());
            setUniform("map_roughness", 0);
        }

        if(material.isMetalMapped()){
            glActiveTexture(GL_TEXTURE3);
            material.getMetalMap().bind();
            setUniform("metalMap", 3);
            setUniform("map_metal", 1);
        } else {
            setUniform("metalConst", material.getMetalConst());
            setUniform("map_metal", 0);
        }

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", group.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        //setUniform("invViewMatrix",group.getModelMatrix().mul(camera.getViewMatrix()).invert() );
    }

}