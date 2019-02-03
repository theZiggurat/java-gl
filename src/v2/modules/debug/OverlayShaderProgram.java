package v2.modules.debug;

import org.joml.Matrix4f;
import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.EngineCore;
import v2.engine.system.ShaderProgram;

public class OverlayShaderProgram extends ShaderProgram {

    private static OverlayShaderProgram instance;
    public static OverlayShaderProgram instance(){
        if(instance == null)
            instance = new OverlayShaderProgram();
        return instance;
    }

    private OverlayShaderProgram(){
        createVertexShader("res/shaders/overlay_vs.glsl");
        createFragmentShader("res/shaders/overlay_fs.glsl");
        link();

        addUniform("projectionMatrix");
        addUniform("modelMatrix");
        addUniform("viewMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode parent){
        Camera camera = EngineCore.instance().getRenderEngine().getMainCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", parent.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }
}
