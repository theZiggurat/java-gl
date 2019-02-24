package v2.modules.sky;

import v2.engine.gldata.vbo.Meshs;
import v2.engine.utils.AssimpLoader;
import v2.engine.gldata.vbo.VertexBufferObject;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;

public class Sky extends ModuleNode {

    private static Sky instance;
    public static Sky instance(){
        if (instance == null){
            instance = new Sky();
        }
        return instance;
    }

    private Sky(){
        super();

        scaleTo(2800);

        addModule(ModuleType.RENDER_MODULE_SCENE, new RenderModule(
                SkyShaderProgram.instance(), Meshs.dome
        ));
    }

    @Override
    public void render(){
        glCullFace(GL_FRONT);
        super.render();
        glCullFace(GL_BACK);
    }


    @Override
    public void update(){
        super.update();
    }
}
