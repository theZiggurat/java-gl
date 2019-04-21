package v2.modules.sky;

import v2.engine.glapi.vbo.Meshs;
import v2.engine.scene.node.ModuleNode;
import v2.engine.scene.node.RenderModule;
import v2.engine.scene.node.RenderType;

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

        transform.scaleTo(2800);

        addModule(RenderType.TYPE_SCENE, new RenderModule(
                SkyShader.instance(), Meshs.cube
        ));
    }

    @Override
    public void render(RenderType type){
        glCullFace(GL_FRONT);
        super.render(type);
        glCullFace(GL_BACK);
    }

    @Override
    public void render(RenderType type, Condition condition){
        glCullFace(GL_FRONT);
        super.render(type, condition);
        glCullFace(GL_BACK);
    }
}
