package v2.engine.gui.element;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import v2.engine.gldata.tex.TextureObject;
import v2.engine.gldata.vbo.Mesh2D;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.gui.Box;
import v2.engine.system.Shader;


import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class DynamicPanel extends Element {

    @Setter @Getter private TextureObject screenTexture;
    private DynamicPanelShader fsShader;

    public DynamicPanel() {
        super();
        fsShader = new DynamicPanelShader();
        relativeBox = new Box(0,0,1,1);
        absoluteBox = new Box(0,0,1,1);
    }

    @Override
    public void render(){

        glPolygonMode(GL_FRONT, GL_FILL);
        fsShader.bind();
        fsShader.updateUniforms(this);
        Meshs.posquad.render();
        fsShader.unbind();

    }

    @Override
    public void handle(){
        super.handle();
    }

    @Override
    public void cleanup(){
        super.cleanup();
        fsShader.cleanup();
    }

    private class DynamicPanelShader extends Shader {

        public DynamicPanelShader(){
            super();

            createVertexShader("shaders/gui/panel_vs.glsl");
            createFragmentShader("shaders/dynamicpanel/dynamicpanel_fs.glsl");
            link();

            addUniform("texture");
            addUniform("box.x");
            addUniform("box.y");
            addUniform("box.width");
            addUniform("box.height");

            addUniform("isDepth");
        }

        public void updateUniforms(DynamicPanel e){

            activeTexture(e.screenTexture, 0);
            setUniform("texture", 0);

            Box box = e.getAbsoluteBox();

            setUniform("box.x", box.getX());
            setUniform("box.y", box.getY());
            setUniform("box.width", box.getWidth());
            setUniform("box.height", box.getHeight());

            setUniform("isDepth", e.getScreenTexture().isDepth() ? 1 : 0);

        }
    }

}
