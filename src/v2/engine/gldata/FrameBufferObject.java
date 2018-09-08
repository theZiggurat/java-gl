package v2.engine.gldata;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class FrameBufferObject {

    // Class credited to Fynn Flugge of Oreon Engine - GLFramebuffer

    private int id;

    public FrameBufferObject(){
        id = glGenFramebuffers();
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setDrawBuffer(int i){
        glDrawBuffer(i);
    }

    public void setDrawBuffer(IntBuffer buffer){
        glDrawBuffers(buffer);
    }

    public void createDepthTextureAttatchment(int textureId){
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureId, 0);
    }

    public void createColorTextureAttachment(int textureId, int index) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index,
                GL_TEXTURE_2D, textureId, 0);
    }

    public void createColorTextureMultisampleAttatchment(int textureId, int index){
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + index,
                GL_TEXTURE_2D_MULTISAMPLE, textureId, 0);
    }

    /**
     * checks for errors
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
