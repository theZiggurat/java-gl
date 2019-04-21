package v2.engine.glapi.fbo;

import lombok.Getter;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Config;
import v2.engine.system.Pipeline;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
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
public class PBRFrameBufferObject extends PipelineFrameBufferObject {

    TextureObject position, albedo, normal;

    public PBRFrameBufferObject(Pipeline pipeline){

        super(pipeline, false);

        int target;
        if(Config.instance().getMultisamples() > 0)
            target = GL_TEXTURE_2D_MULTISAMPLE;
        else
            target = GL_TEXTURE_2D;

        // contains position and roughness information
        position = new TextureObject(
                target, pipeline.getResolution())
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        // contains normal and metal information
        normal = new TextureObject(
                target, pipeline.getResolution())
                .allocateImage2D(GL_RGBA32F, GL_RGBA)
                .bilinearFilter();

        // contains albedo rgba information
        albedo = new TextureObject(
                target, pipeline.getResolution())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();

        addAttatchments(position, normal, albedo, getScene(), getDepth());
        checkStatus();

    }
}
