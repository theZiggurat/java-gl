package v2.modules.math;

import v2.engine.scene.node.ModuleNode;
import v2.engine.system.Shader;

public class SurfaceShader extends Shader {

    private static SurfaceShader instance;
    public static SurfaceShader instance(){
        if(instance() == null)
            instance = new SurfaceShader();
        return instance;
    }

    private SurfaceShader(){

        createVertexShader("res/shaders/surface_vertex.glsl");
        createFragmentShader("res/shaders/surface_frag.glsl");


    }

    @Override
    public void updateUniforms(ModuleNode surface){


    }
}
