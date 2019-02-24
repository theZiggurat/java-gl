package v2.modules.debug;

import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.EngineCore;
import v2.engine.system.ShaderProgram;

public class GenericOverlayShaderProgram extends ShaderProgram {

    private static GenericOverlayShaderProgram instance;
    public static GenericOverlayShaderProgram instance(){
        if(instance == null)
            instance = new GenericOverlayShaderProgram();
        return instance;
    }

    private GenericOverlayShaderProgram(){
        super();
        createVertexShader("res/shaders/overlay/overlay_vs.glsl");
        createFragmentShader("res/shaders/overlay/overlay_fs.glsl");
        link();

        addUniform("projectionMatrix");
        addUniform("modelMatrix");
        addUniform("viewMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode node){

        Camera camera = EngineCore.instance().getRenderEngine().getMainCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }

}
