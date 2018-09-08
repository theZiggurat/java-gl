package v2.modules.pbr;

import v2.engine.system.Camera;
import v2.engine.system.RenderEngine;
import v2.engine.system.ShaderProgram;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;

import static org.lwjgl.opengl.GL13.*;

public class PBRShaderProgram extends ShaderProgram {

    private static PBRShaderProgram instance = null;

    public static PBRShaderProgram getInstance(){
        if(instance == null){
            instance = new PBRShaderProgram();
        }
        return instance;
    }

    private PBRShaderProgram()  {

        super();

        createVertexShader("res/shaders/pbr_vertex.vs");
        createFragmentShader("res/shaders/pbr_fragment.fs");
        link();

        addUniform("albedoMap");
        addUniform("normalMap");
        addUniform("roughnessMap");
        addUniform("metalMap");
        addUniform("aoMap");

        addUniform("modelViewMatrix");
        addUniform("projectionMatrix");
        //addUniform("invViewMatrix");

    }

    @Override
    public void updateUniforms(ModuleNode group){

        Camera camera = RenderEngine.getInstance().getMainCamera();

        PBRMaterial material = (PBRMaterial) group.getModules().
                                get(ModuleType.MATERIAL);

        glActiveTexture(GL_TEXTURE0);
        material.getAlbedoMap().bind();
        setUniform("albedoMap", 0);

        glActiveTexture(GL_TEXTURE1);
        material.getNormalMap().bind();
        setUniform("normalMap", 1);

        glActiveTexture(GL_TEXTURE2);
        material.getRoughnessMap().bind();
        setUniform("roughnessMap", 2);

        glActiveTexture(GL_TEXTURE3);
        material.getMetallicMap().bind();
        setUniform("metalMap", 3);

        glActiveTexture(GL_TEXTURE4);
        material.getAoMap().bind();
        setUniform("aoMap", 4);

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelViewMatrix", group.getWorldTransform().getModelMatrix().mul(camera.getViewMatrix()));
        //setUniform("invViewMatrix",group.getWorldTransform().getModelMatrix().mul(camera.getViewMatrix()).invert() );
    }

}
