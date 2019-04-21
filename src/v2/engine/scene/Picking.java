package v2.engine.scene;

import lombok.Getter;
import org.joml.Vector3f;
import v2.engine.glapi.fbo.FrameBufferObject;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.scene.node.Node;
import v2.engine.scene.node.RenderType;
import v2.engine.system.Window;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Picking {

    private SceneContext context;
    @Getter private UUIDFrameBufferObject UUIDmap;

    public Picking(SceneContext context){
        this.context = context;
        this.UUIDmap = new UUIDFrameBufferObject();
    }

    public Node pick(int x, int y){

        UUIDmap.bind();
            glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Window.instance().resizeViewport(context.getResolution());
            context.getScene().render(RenderType.TYPE_UUID, e -> !e.isSelected());
            Window.instance().resetViewport();
            ByteBuffer rgb = ByteBuffer.allocateDirect(4);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glReadPixels(x, y, 1, 1,
                    GL_RGBA, GL_UNSIGNED_BYTE, rgb);
        UUIDmap.unbind();

        int r, g, b;
        r = (int)rgb.get(0);
        g = (int)rgb.get(1) << 8;
        b = (int)rgb.get(2) << 16;

        int ID = r + b + g;

        for (Node node: context.getScene().collect())
            if(node.getUUID() == ID) return node;

        return null;
    }



//    public TextureObject updateMap(){
//        UUIDmap.bind();
//        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//        context.getScene().render(RenderType.TYPE_UUID);
//        UUIDmap.unbind();
//        return getUUIDmap().getMap();
//    }

    public class UUIDFrameBufferObject extends FrameBufferObject {
        @Getter
        private TextureObject map, depth;

        private UUIDFrameBufferObject(){
            super();
            map = new TextureObject(
                    GL_TEXTURE_2D, Window.instance().getWidth(),
                    Window.instance().getHeight())
                    .allocateImage2D(GL_RGBA16F, GL_RGBA)
                    .nofilter();

            depth = new TextureObject(
                    GL_TEXTURE_2D, Window.instance().getWidth(),
                    Window.instance().getHeight())
                    .allocateDepth()
                    .bilinearFilter();

            addAttatchments(map, depth);
            checkStatus();
        }

        @Override
        public void resize(int x, int y){
             map.resize(x,y);
             depth.resize(x,y);
        }
    }

    public static Vector3f getUUIDColor(int UUID){

        int r = (UUID & 0x000000FF) >> 0;
        int g = (UUID & 0x0000FF00) >> 8;
        int b = (UUID & 0x00FF0000) >> 16;

        return new Vector3f((float)r/255f, (float)g/255f, (float)b/255f);
    }


}
