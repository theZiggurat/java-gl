package v2.engine.light;

import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;
import v2.engine.vbo.Meshs;
import v2.modules.debug.OverlayShaderProgram;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengles.GLES20.glDisable;

public class PointLight extends Light {

    public PointLight(){
        super();

        RenderModule debugRenderer = new RenderModule(
                OverlayShaderProgram.instance(), Meshs.cube
        );
        addModule(ModuleType.RENDER_MODULE, debugRenderer);
    }
//
//    @Override
//    public void render(){
//        //glDisable(GL_DEPTH_TEST);
//        super.render();
//        //glEnable(GL_DEPTH_TEST);
//        System.out.println("Rendering Light");
//    }
}
