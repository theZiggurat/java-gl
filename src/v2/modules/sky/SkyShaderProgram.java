package v2.modules.sky;

import org.joml.Matrix4f;
import v2.engine.scene.ModuleNode;
import v2.engine.system.ShaderProgram;
import v2.modules.pbr.PBRRenderEngine;

public class SkyShaderProgram extends ShaderProgram {

    private static SkyShaderProgram instance;
    public static SkyShaderProgram instance(){
        if (instance == null){
            instance = new SkyShaderProgram();
        }
        return instance;
    }

    private SkyShaderProgram(){

        createVertexShader("res/shaders/sky/sky_vert.glsl");
        createFragmentShader("res/shaders/sky/sky_frag.glsl");
        link();

        addUniform("viewProjectionMatrix");
        addUniform("modelMatrix");

        addUniform("scale");
    }

    @Override
    public void updateUniforms(ModuleNode skyNode) {
        setUniform("viewProjectionMatrix",
                new Matrix4f(PBRRenderEngine.instance().getMainCamera().getViewProjectionMatrix()));
        setUniform("modelMatrix",
                new Matrix4f(skyNode.getModelMatrix()));
        setUniform("scale", skyNode.getWorldScaling().y);

    }
}
