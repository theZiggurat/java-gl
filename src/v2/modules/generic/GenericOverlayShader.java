package v2.modules.generic;

import v2.engine.scene.node.ModuleNode;
import v2.engine.scene.Camera;
import v2.engine.system.Shader;

public class GenericOverlayShader extends Shader {

    private static GenericOverlayShader instance;
    public static GenericOverlayShader instance(){
        if(instance == null)
            instance = new GenericOverlayShader();
        return instance;
    }

    private GenericOverlayShader(){
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

        Camera camera = boundContext.getCamera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }

}
