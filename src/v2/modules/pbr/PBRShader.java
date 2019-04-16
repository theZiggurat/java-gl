package v2.modules.pbr;

import v2.engine.scene.Camera;
import v2.engine.system.Shader;
import v2.engine.scene.node.ModuleNode;

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

        addUniform("UVscale");
        //addUniform("invViewMatrix");


    }

    @Override
    public void updateUniforms(ModuleNode group){

        Camera camera = boundContext.getCamera();

        PBRMaterial material = ((PBRModel)group).getMaterial();

        if(material.isAlbedoMapped()) {
            activeTexture(material.albedoMap, 0);
            setUniform("albedoMap", 0);
            setUniform("map_albedo", 1);
        } else {
            setUniform("albedoConst", material.getAlbedoConst());
            setUniform("map_albedo", 0);
        }

        if(material.isNormalMapped()){
            activeTexture(material.normalMap, 1);
            setUniform("normalMap", 1);
            setUniform("map_normal", 1);
        } else {
            setUniform("map_normal", 0);
        }

        if(material.isRoughnessMapped()){
            activeTexture(material.roughnessMap, 2);
            setUniform("roughnessMap", 2);
            setUniform("map_roughness", 1);
        } else {
            setUniform("roughnessConst", material.getRoughnessConst());
            setUniform("map_roughness", 0);
        }

        if(material.isMetalMapped()){
            activeTexture(material.metalMap, 3);
            setUniform("metalMap", 3);
            setUniform("map_metal", 1);
        } else {
            setUniform("metalConst", material.getMetalConst());
            setUniform("map_metal", 0);
        }

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", group.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());

        setUniform("UVscale", ((PBRModel)group).getUVscalar());

    }

}
