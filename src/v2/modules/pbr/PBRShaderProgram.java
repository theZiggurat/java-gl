package v2.modules.pbr;

import org.joml.Vector3f;
import v2.engine.system.Camera;
import v2.engine.system.RenderEngine;
import v2.engine.system.ShaderProgram;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class PBRShaderProgram extends ShaderProgram {

    /**
     * -Buffers uv-maps from object tangent space to screen space
     * Renders texture maps from objec
     */

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
        //createGeometryShader("res/shaders/pbr_geometry.gs");
        link();

        addUniform("albedoMap");
        addUniform("normalMap");
        addUniform("roughnessMap");
        addUniform("metalMap");

        addUniform("modelMatrix");
        addUniform("viewMatrix");
        addUniform("projectionMatrix");
        //addUniform("invViewMatrix");

        //addUniform("randomVec");

    }

    @Override
    public void updateUniforms(ModuleNode group){

        Camera camera = RenderEngine.instance().getMainCamera();

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

        //setUniform("randomVec", new Vector3f(1).normalize());

        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", group.getWorldTransform().getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("invViewMatrix",group.getWorldTransform().getModelMatrix().mul(camera.getViewMatrix()).invert() );
    }

}
