package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.light.LightManager;
import v2.engine.scene.Scenegraph;
import v2.engine.gui.GLViewport;
import v2.modules.debug.OverlayBlendingShaderProgram;
import v2.modules.debug.OverlayFrameBufferObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public abstract class RenderEngine {

    // Status of render engine
    private Boolean running;

    // holds spatial relationship of all models & lights
    @Setter @Getter
    protected Scenegraph scenegraph;

    private OverlayFrameBufferObject overlayFrameBufferObject;
    private TextureObject overlayBlendedImage;

    // final texture to be displayed
    @Setter @Getter
    protected TextureObject sceneTexture;

    /** TODO: refactor with camera controller **/
    @Setter @Getter
    protected Camera mainCamera;

    // gui to render scene texture with
    private GLViewport screenQuad;

    protected RenderEngine(){
        Window window = Window.instance();
        scenegraph = Scenegraph.instance();
        sceneTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        overlayBlendedImage = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        overlayFrameBufferObject = new OverlayFrameBufferObject();
        mainCamera = new Camera();
        screenQuad = new GLViewport();
        running = true;
    }

    public void onResize(){
        overlayFrameBufferObject.resize(Window.instance().getWidth(), Window.instance().getHeight());
        overlayBlendedImage.resize(Window.instance().getWidth(), Window.instance().getHeight());
    }

    public void prepare3D(){

    }

    public void prepare2D(){

    }

    protected abstract void renderCamera();

    void draw(){

        // adjust viewport size
        if(Window.instance().isResized()){
            glViewport(0,0, Window.instance().getWidth(),
                    Window.instance().getHeight());
            onResize();
            Window.instance().setResized(false);
        }

        // call sub-render method
        // that will populate the scene texture
        renderCamera();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(Config.instance().isDebugLayer()) {

            overlayFrameBufferObject.bind();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            scenegraph.renderOverlay();
            overlayFrameBufferObject.unbind();

            OverlayBlendingShaderProgram.instance().compute(
                    sceneTexture, overlayFrameBufferObject.getOverlay(), overlayBlendedImage
            );

            // render scene texture to the gui
            screenQuad.setScreenTexture(overlayBlendedImage);
        }
        else
            screenQuad.setScreenTexture(sceneTexture);


        screenQuad.render();
    }

    public void cleanup(){
        scenegraph.cleanup();
        sceneTexture.cleanup();
    }

    public Boolean isRunning(){
        return running;
    }

    public void activate(){
        running = true;
    }

    public void deactivate(){
        running = false;
    }

}