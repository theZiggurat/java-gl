package modules.generic;

import engine.scene.light.Light;
import engine.scene.node.ModuleNode;
import engine.scene.Camera;
import engine.glapi.Shader;

public class LightOverlayShader extends Shader {

    private static LightOverlayShader instance;
    public static LightOverlayShader instance(){
        if(instance == null)
            instance = new LightOverlayShader();
        return instance;
    }

    private LightOverlayShader(){
        super();
//        createVertexShader("res/shaders/overlay/overlay_vs.glsl");
//        createFragmentShader("res/shaders/overlay/outline_fs.glsl");
//        link();
//
//        addUniform("color");
//        addUniform("projectionMatrix");
//        addUniform("modelMatrix");
//        addUniform("viewMatrix");
    }

    @Override
    public void updateUniforms(ModuleNode node){

        Light light = (Light) node;

        Camera camera = boundContext.getCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", light.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
        setUniform("color", light.getColor());
    }
}
