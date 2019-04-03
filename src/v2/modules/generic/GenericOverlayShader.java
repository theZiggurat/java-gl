package v2.modules.generic;

import v2.engine.scene.ModuleNode;
import v2.engine.system.Camera;
import v2.engine.system.Core;
import v2.engine.system.Shader;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

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

        Camera camera = Core.camera();
        setUniform("projectionMatrix", camera.getProjectionMatrix());
        setUniform("modelMatrix", node.getModelMatrix());
        setUniform("viewMatrix", camera.getViewMatrix());
    }

}
