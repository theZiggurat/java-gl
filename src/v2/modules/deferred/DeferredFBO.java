package v2.modules.deferred;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import v2.engine.gldata.FrameBufferObject;
import v2.engine.gldata.TextureObject;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_RGBA32F;

@Getter
public class DeferredFBO extends FrameBufferObject {

    TextureObject position, albedo, normal, metalness, roughness, depth;

    public DeferredFBO(int width, int height, int samples){

        super();

//        position = new TextureObject(
//                GL_TEXTURE_2D, width, height, samples)
//                .allocateImage2D(GL_RGBA32F, GL_RGBA);

        albedo = new TextureObject(
            GL_TEXTURE_2D, width, height, samples)
                .allocateImage2D(GL_RGBA16F, GL_RGBA);

//        normal = new TextureObject(
//            GL_TEXTURE_2D, width, height, samples)
//                .allocateImage2D(GL_RGBA32F, GL_RGBA);
//
//         metalness = new TextureObject(
//                GL_TEXTURE_2D, width, height, samples)
//                .allocateImage2D(GL_R, GL_R);
//
//        roughness = new TextureObject(
//                GL_TEXTURE_2D, width, height, samples)
//                .allocateImage2D(GL_R, GL_R);
//
//        depth = new TextureObject(
//                GL_TEXTURE_2D, width, height, samples)
//                .allocateImage2D(GL_DEPTH_COMPONENT32F, GL_DEPTH_COMPONENT);

        IntBuffer drawBuffers = BufferUtils.createIntBuffer(1);
        drawBuffers.put(GL_COLOR_ATTACHMENT0);
//        drawBuffers.put(GL_COLOR_ATTACHMENT1);
//        drawBuffers.put(GL_COLOR_ATTACHMENT2);
//        drawBuffers.put(GL_COLOR_ATTACHMENT3);
//        drawBuffers.put(GL_COLOR_ATTACHMENT4);
        drawBuffers.flip();

        bind();
//        createColorTextureAttachment(getPosition().getId(), 0);
//        createColorTextureAttachment(getNormal().getId(), 1);
        createColorTextureAttachment(getAlbedo().getId(), 0);
//        createColorTextureAttachment(getMetalness().getId(), 3);
//        createColorTextureAttachment(getRoughness().getId(), 4);
        //createDepthTextureAttatchment(getDepth().getId());
        setDrawBuffer(drawBuffers);
        checkStatus();
        unbind();

    }
}
