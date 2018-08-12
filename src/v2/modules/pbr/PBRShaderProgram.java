package v2.modules.pbr;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import v2.engine.buffer.GLTexture;
import v2.engine.system.Camera;
import v2.engine.system.RenderEngine;
import v2.engine.system.ShaderProgram;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;

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

        //addUniform("albedoMap");
        addUniform("modelViewMatrix");
        addUniform("projectionMatrix");

    }

    @Override
    public void updateUniforms(ModuleNode group){

        Camera camera = RenderEngine.getInstance().getMainCamera();

        PBRMaterial material = (PBRMaterial) group.getModules().
                                get(ModuleType.MATERIAL);
        GLTexture albedo = material.getAlbedo();

        glActiveTexture(GL_TEXTURE0);
        albedo.bind();
        //setUniform("albedoMap", 0);

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelViewMatrix", group.getWorldTransform().getModelMatrix().mul(camera.getViewMatrix()));
    }

}
