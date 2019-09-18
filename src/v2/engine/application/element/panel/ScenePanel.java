package v2.engine.application.element.panel;

import org.joml.Vector2i;
import v2.engine.application.ElementManager;
import v2.engine.application.element.panel.Panel;
import v2.engine.application.layout.Box;
import v2.engine.application.event.KeyboardEvent;
import v2.engine.application.event.ResizeEvent;
import v2.engine.application.ApplicationContext;
import v2.engine.scene.SceneContext;
import v2.engine.system.Window;
import v2.engine.utils.Color;
import v2.modules.pbr.PBRPipeline;

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
                        ApplicationContext.instance().setRenderElement(this);
                        ElementManager.instance().setFocused(this);
                        if(setAbsoluteBox(new Box(0,0,1,1))) {
                            setDrawBorder(false);
                            context.setResolution(Window.instance().getResolution());
                            layoutChildren();
                        }
                    }

                    // unset fullscreen
                    else {
                        setDrawBorder(true);
                        ApplicationContext.instance().resetRenderElement();
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
