package v2.modules.pbr;

import lombok.Getter;
import v2.engine.gldata.TextureObject;
import v2.engine.system.ShaderProgram;
import v2.engine.system.Window;
import v2.modules.deferred.DeferredFBO;

import static org.lwjgl.opengl.GL11.*;

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
 */

@Getter
public class PBRDeferredRenderer {

    TextureObject sceneTexture;
    ShaderProgram deferredShader;

    public PBRDeferredRenderer(){
        Window window = Window.getInstance();

        sceneTexture = new TextureObject(window.getWidth(), window.getHeight()).allocateImage2D(
                GL_RGBA16, GL_RGBA
        );

    }

    /**
     * Renders lights
     */
    public void render(){
    }
}
