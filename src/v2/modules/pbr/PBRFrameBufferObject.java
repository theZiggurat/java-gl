package v2.modules.pbr;

import lombok.Getter;
import v2.engine.gldata.FrameBufferObject;
import v2.engine.gldata.TextureObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengles.GLES30.GL_RGBA32F;

/**
 * Channels:
 * layout (location = 0) out vec4 pos_vbo
 * layout (location = 1) out vec4 norm_vbo;
 * layout (location = 2) out vec4 albedo_vbo;
 * + depth buffer
 *
 * __0__________8__________16_________24__________
 * |0| pos.x    | pos.y    | pos.z    | roughness|
 * |1| normal.r | normal.g | normal.b | metalness|
 * |2| albedo.rg| albedo.ba|          |          |
 * |_|__________|__________|__________|__________|
 *
 */


@Getter
public class PBRFrameBufferObject extends FrameBufferObject {

    TextureObject position, albedo, normal, depth;

    public PBRFrameBufferObject(int width, int height, int samples){

        super();

        // contains position and roughness information
        position = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        albedo = new TextureObject(
            GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        // contains normal and metal information
        normal = new TextureObject(
            GL_TEXTURE_2D, width, height)
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        depth = new TextureObject(
                GL_TEXTURE_2D, width, height)
                .allocateDepth()
                .bilinearFilter();

        addAttatchments(position, normal, albedo, depth);
        checkStatus();

    }
}
