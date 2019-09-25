package engine.application.element.panel;

import engine.system.AppContext;
import org.joml.Vector2i;
import engine.application.element.ElementManager;
import engine.application.layout.Box;
import engine.application.event.KeyboardEvent;
import engine.application.event.ResizeEvent;
import engine.scene.SceneContext;
import engine.system.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;

public class ScenePanel extends Panel {

    private SceneContext context;


    public ScenePanel(SceneContext _context) {

        super();
        this.context = _context;
        context.setParent(this);

//        setDrawBorder(true);
//        setBorderColor(new Color(0x202020));
//        setBorderSize(2);


        setImageBuffer(context.getPipeline().getSceneBuffer());
        setImage(true);

        onEvent(e -> {

            if(e instanceof ResizeEvent){
                Vector2i resolution = getAbsoluteBox().resolution();
                resolution.x -= 2*getBorderSize();
                resolution.y -= 2*getBorderSize();
                context.setResolution(resolution);
                e.consume();
            }

            else if(e instanceof KeyboardEvent){

                /** F11: TOGGLE FULLSCREEN  **/
                if(((KeyboardEvent) e).getKey()==GLFW_KEY_F11 &&
                        ((KeyboardEvent) e).getAction()==KeyboardEvent.KEY_PRESSED){

                    // set fullscreen
                    if(!isAttached()){
                        setAttached(true);
                        AppContext.instance().setRenderElement(this);
                        ElementManager.instance().setFocused(this.getViewport());
                        if(setAbsoluteBox(new Box(0,0,1,1))) {
                            setDrawBorder(false);
                            context.setResolution(Window.instance().getResolution());
                            layoutChildren();
                        }
                    }

                    // unset fullscreen
                    else {
                        setDrawBorder(true);
                        AppContext.instance().resetRenderElement();
                        ElementManager.instance().resetFocused();
                        setAttached(false);
                        layoutChildren();
                    }
                    e.consume();
                }

            }

            // pass to scene context if not consumed
            if(!e.isConsumed())
                context.handle(e);
        });

    }
}
