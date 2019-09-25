package modules.generic;

import engine.scene.node.ModuleNode;
import engine.scene.Camera;
import engine.glapi.Shader;

public class GenericOverlayShader extends Shader {

    private static GenericOverlayShader instance;
    public static GenericOverlayShader instance(){
        if(instance == null)
            instance = new GenericOverlayShader();
        return instance;
    }

    private GenericOverlayShader(){
        super();
        createVertexShader("res/shaders/overlay/outline_vs.glsl");
        createFragmentShader("res/shaders/overlay/outline_fs.glsl");
        link();

        addUniform("projectionMatrix");
        addUniform("modelMatrix");
        addUniform("viewMatrix");




    }

    @Override
    public void updateUniforms(ModuleNode node){

        Camera camera = boundContext.getCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }

}
