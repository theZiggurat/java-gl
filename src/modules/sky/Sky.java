package modules.sky;

import engine.glapi.vbo.Meshs;
import engine.scene.node.ModuleNode;
import engine.scene.node.RenderModule;
import engine.scene.node.RenderType;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;

public class Sky extends ModuleNode {

    public Sky(){
        super();

        transform.scaleTo(2800);

        addModule(RenderType.TYPE_SCENE, new RenderModule(
                SkyShader.instance(), Meshs.sphere
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
