package v2.modules.pbr;

import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengles.GLES30;
import v2.engine.gldata.FrameBufferObject;
import v2.engine.gldata.TextureObject;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_RGBA32F;

/**
 * Handles deferred rendering
 * Proper use:
 * 1. Bind renderer FBO
 * 2. Render scenegraph
 * 3. Use texture channel outputs from FBO to do other operations
 *
 *
 * Channels:
 * layout (location = 0) out vec4 pos_vbo
 * layout (location = 1) out vec4 norm_vbo;
 * layout (location = 2) out vec4 albedo_vbo;
 * layout (location = 3) out float metal_vbo;
 * layout (location = 4) out float rough_vbo;
 * + depth buffer
 */

@Getter
public class PBRFrameBufferObject extends FrameBufferObject {

    TextureObject position, albedo, normal, metalness, roughness, ao, depth;

    public PBRFrameBufferObject(int width, int height, int samples){

        super();

        position = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        albedo = new TextureObject(
            GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        normal = new TextureObject(
            GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        metalness = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_R16F, GL_RED)
                .bilinearFilter();

        roughness = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_R16F, GL_RED)
                .bilinearFilter();

        ao = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_R16F, GL_RED)
                .bilinearFilter();

        depth = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateDepth()
                .bilinearFilter();

        IntBuffer drawBuffers = BufferUtils.createIntBuffer(6);
        drawBuffers.put(GL_COLOR_ATTACHMENT0);
        drawBuffers.put(GL_COLOR_ATTACHMENT1);
        drawBuffers.put(GL_COLOR_ATTACHMENT2);
        drawBuffers.put(GL_COLOR_ATTACHMENT3);
        drawBuffers.put(GL_COLOR_ATTACHMENT4);
        drawBuffers.put(GL_COLOR_ATTACHMENT5);
        drawBuffers.flip();

        bind();
        createColorTextureAttachment(getPosition().getId(), 0);
        createColorTextureAttachment(getNormal().getId(), 1);
        createColorTextureAttachment(getAlbedo().getId(), 2);
        createColorTextureAttachment(getMetalness().getId(), 3);
        createColorTextureAttachment(getRoughness().getId(), 4);
        createColorTextureAttachment(getAo().getId(), 5);
        createDepthTextureAttatchment(getDepth().getId());
        setDrawBuffer(drawBuffers);
        checkStatus();
        unbind();

    }
}
