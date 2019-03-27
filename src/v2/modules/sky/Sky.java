package v2.modules.sky;

import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.RenderModule;
import v2.engine.scene.RenderType;

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

        addModule(RenderType.TYPE_SCENE, new RenderModule(
                SkyShader.instance(), Meshs.dome
        ));
    }

    @Override
    public void render(RenderType type){
        glCullFace(GL_FRONT);
        super.render(type);
        glCullFace(GL_BACK);
    }


    @Override
    public void update(){
        super.update();
    }
}
