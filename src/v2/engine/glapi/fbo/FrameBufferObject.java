package v2.engine.glapi.fbo;

import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import v2.engine.glapi.tex.TextureObject;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public abstract class FrameBufferObject {

    private ArrayList<TextureObject> attatchments;

    /** Framebuffer ID **/
    private int id;

    /**
     * Generates new openGL framebuffer with glGenFramebuffers
     */
    public FrameBufferObject(){
        id = glGenFramebuffers();
        attatchments = new ArrayList<>();
    }

    /**
     * Binds this framebuffer. A binded framebuffer will be used in
     * all subsequent glDraw calls.
     */
    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void bind(Runnable runner){
        bind();
        runner.run();
        unbind();
    }

    /**
     * Unbinds any currently binded framebuffer
     */
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Dicates how framebuffer draws with intbuffer that contains
     * {GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, ... , GL_COLOR_ATTACHMENTn}
     * for n framebuffer color attachments
     * @param buffer int buffer with color attachments
     */
    public void setDrawBuffer(IntBuffer buffer){
        glDrawBuffers(buffer);
    }

    /**
     * Must be used subsequently with an empty texture modified with allocateDepth()
     * @param textureId texture with depth allocation
     */
    public void createDepthTextureAttatchment(int textureId){
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureId, 0);
    }

    public void createDepthTextureMultisampleAttatchment(int textureId){
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D_MULTISAMPLE, textureId, 0);
    }

    /**
     * Must be used subsequently with an empty texture modified with allocateImage2D
     * with valid openGL color texture format
     * @param textureId of empty image texture
     * @param index index in glsl shader ex: | layout (location = index) out vec4 image_name; |
     */
    public void createColorTextureAttachment(int textureId, int index) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index,
                GL_TEXTURE_2D, textureId, 0);
    }

    public void createColorTextureMultisampleAttatchment(int textureId, int index){
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index,
                GL_TEXTURE_2D_MULTISAMPLE, textureId, 0);
    }

    public void addAttatchments(TextureObject... textures){
        IntBuffer drawBuffers = BufferUtils.createIntBuffer(textures.length);


        bind(()-> {
            int i = 0;
            for (TextureObject attatchment : textures) {
                attatchments.add(attatchment);

                if (attatchment.isDepth()) {
                    if(attatchment.isMultisample())
                        createDepthTextureMultisampleAttatchment(attatchment.getId());
                    else
                        createDepthTextureAttatchment(attatchment.getId());
                }
                else if(attatchment.isMultisample()) {
                    createColorTextureMultisampleAttatchment(attatchment.getId(), i);
                    drawBuffers.put(GL_COLOR_ATTACHMENT0 + i++);
                } else {
                    createColorTextureAttachment(attatchment.getId(), i);
                    drawBuffers.put(GL_COLOR_ATTACHMENT0 + i++);
                }

            }
            drawBuffers.flip();
            setDrawBuffer(drawBuffers);
        });
    }

    public void resize(int xsize, int ysize){
        for (TextureObject attatchement: attatchments)
            attatchement.resize(xsize, ysize);
    }

    public void resize(Vector2i size){
        resize(size.x, size.y);
    }

    /**
     * Checks all openGL framebuffer errors
     */
    public void checkStatus()
    {
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE){
            return;
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_UNDEFINED){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_UNDEFINED error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_UNSUPPORTED){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_UNSUPPORTED error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE error");
            System.exit(1);
        }
        else if (glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS){
            System.err.println("Framebuffer creation failed with GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS error");
            System.exit(1);
        }
    }

}
