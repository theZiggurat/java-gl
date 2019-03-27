package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.gui.GLViewport;
import v2.engine.scene.RenderType;
import v2.modules.generic.OverlayBlendingShader;
import v2.modules.generic.OverlayFrameBufferObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;

public abstract class Pipeline {

    // Status of render engine
    private Boolean running;

    private OverlayFrameBufferObject overlayFrameBufferObject;
    private TextureObject overlayBlendedImage;

    // final texture to be displayed
    @Setter @Getter
    protected TextureObject sceneTexture;

    /** TODO: refactor with camera controller **/


//    private GUITEST gui1, gui2;

    protected Pipeline(){
        Window window = Window.instance();
        sceneTexture = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        overlayBlendedImage = new TextureObject(GL_TEXTURE_2D, window.getWidth(), window.getHeight())
                .allocateImage2D(GL_RGBA16F, GL_RGBA)
                .bilinearFilter();
        overlayFrameBufferObject = new OverlayFrameBufferObject();
        running = true;
//        gui1 = new GUITEST(-1f, -1f, 0.5f, 0.5f);
//        gui2 = new GUITEST(-1f, -1f, 0.4f, 0.4f);
    }

    public void onResize(){
        overlayFrameBufferObject.resize(Window.instance().getWidth(), Window.instance().getHeight());
        overlayBlendedImage.resize(Window.instance().getWidth(), Window.instance().getHeight());
    }

    public void prepare3D(){

    }

    public void prepare2D(){

    }

    protected abstract void renderScene(Context context);

    void draw(GLViewport viewport, Context context){

        // adjust viewport size
        if(Window.instance().isResized()){
            glViewport(0,0, Window.instance().getWidth(),
                    Window.instance().getHeight());
            onResize();
            Window.instance().setResized(false);
        }

        // call sub-render method
        // that will populate the scene texture
        renderScene(context);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(Config.instance().isDebugLayer()) {

            overlayFrameBufferObject.bind();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            context.getScene().render(RenderType.TYPE_OVERLAY);
            overlayFrameBufferObject.unbind();

            OverlayBlendingShader.instance().compute(
                    sceneTexture, overlayFrameBufferObject.getOverlay(), overlayBlendedImage
            );

            // render scene texture to the gui
            viewport.setScreenTexture(overlayBlendedImage);
        }
        else
            viewport.setScreenTexture(sceneTexture);


        viewport.render();
    }

    public void cleanup(){
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