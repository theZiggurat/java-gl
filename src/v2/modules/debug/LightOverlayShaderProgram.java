package v2.modules.debug;

import v2.engine.light.Light;
import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.EngineCore;
import v2.engine.system.ShaderProgram;

public class LightOverlayShaderProgram extends ShaderProgram {

    private static LightOverlayShaderProgram instance;
    public static LightOverlayShaderProgram instance(){
        if(instance == null)
            instance = new LightOverlayShaderProgram();
        return instance;
    }

    private LightOverlayShaderProgram(){
        super();
        createVertexShader("res/shaders/overlay/overlay_vs.glsl");
        createFragmentShader("res/shaders/overlay/overlay_fs.glsl");
        link();

        addUniform("color");
        addUniform("projectionMatrix");
        addUniform("modelMatrix");
        addUniform("viewMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode node){

        Light light = (Light) node;

        Camera camera = EngineCore.instance().getRenderEngine().getMainCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", light.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("color", light.getColor());
    }
}
