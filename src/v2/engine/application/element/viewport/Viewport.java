package v2.engine.application.element.viewport;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import v2.engine.application.element.Element;
import v2.engine.application.element.panel.Panel;
import v2.engine.application.layout.Box;
import v2.engine.application.event.InputManager;
import v2.engine.application.event.ResizeEvent;
import v2.engine.application.event.mouse.HoverLostEvent;
import v2.engine.application.event.mouse.HoverStartEvent;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.ApplicationContext;
import v2.engine.application.layout.Inset;
import v2.engine.application.layout.ViewportLayout;
import v2.engine.system.Window;
import v2.engine.utils.Color;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_HELD;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_RELEASED;

/**
 * Contains functionality akin to a window in an OS
 * Can be dragged, resized,
 * TODO: opened, closed, hidden, docked, named, debug stats
 */
public class Viewport extends Element {

    // defines areas where window cursor acts to resize
    // left , top , right, bottom
    private Box[] edgeBox;

    // 0: left , 1: top , 2: right, 3: bottom, -1: none
    private int dragEdge = -1;
    boolean activeDrag = false;
    boolean hover = false;

    // size in pixels the mouse can hover over to start a resize event
    private int edgeSize = 5;

    protected float minWidth = 40;
    protected float minHeight = 40;

    // child panels
    protected Panel topBar;
    protected Panel edgePanel;
    protected Panel mainPanel;

    private ViewportSettings viewportSettings;

    protected Viewport(ViewportSettings vs){

        super();

        this.viewportSettings = vs;

        this.topBar = new Panel();
        topBar.setColor(vs.getTopBarColor());
        topBar.setRounding(vs.getRounding().x, vs.getRounding().y, 0, 0);

        this.edgePanel = new Panel();
        edgePanel.setColor(vs.getBorderColor());
        edgePanel.setRounding(0,0,vs.getRounding().z, vs.getRounding().w);

        edgeBox = new Box[4];
        setControlling(true);
        setEdgeBoxs();


        // update viewport state
        onEvent(e -> {

            if(e instanceof ResizeEvent)
                setEdgeBoxs();

            else if(e instanceof HoverStartEvent)
                hover = true;

            else if(e instanceof HoverLostEvent)
                hover = false;

            else if(e instanceof MouseClickEvent) {
                MouseClickEvent m = (MouseClickEvent) e;

                // resize window start
                if ((m.getAction() == BUTTON_CLICK)) {

                    if(dragEdge != -1) {
                        ApplicationContext.instance().getElementManager().setFocused(this);
                        activeDrag = true;
                        e.consume();
                    }
                }

                // resize window end
                else if ((m.getAction() == BUTTON_RELEASED) && activeDrag) {

                    if(activeDrag) {
                        ApplicationContext.instance().getElementManager().resetFocused();
                        activeDrag = false;
                        e.consume();
                    }
                }

                if (!e.isConsumed() && mainPanel.getAbsoluteBox().isWithin(m.getScreenPos())){
                    mainPanel.handle(e);
                }
            }
        });

        // make viewport draggable by the window
        topBar.onEvent(e -> {

            if(e instanceof MouseClickEvent){

                // move window
                MouseClickEvent m = (MouseClickEvent)e;
                if(m.getAction()==BUTTON_HELD){
                    getRelativeBox().translate(m.getScreenDelta());
                    getParent().forceTreeLayout();
                }
                e.consume();
            }
        });

        // create window layout
        ViewportLayout vsp = new ViewportLayout(this);
        vsp.setTopBarHeight(vs.getTopBarSize());
        setLayout(vsp);

        // add sub panels as children
        addChildren(topBar, edgePanel);
        setAttached(true);
    }

    public void setMainPanel(Panel mainPanel) {
        this.mainPanel = mainPanel;
        mainPanel.setColor(viewportSettings.getPanelColor());
        mainPanel.setRounding(0, 0, viewportSettings.getRounding().z, viewportSettings.getRounding().w);
        int bs = viewportSettings.getBorderSize();
        mainPanel.setInset(new Inset(bs/2, bs, bs, bs));
        addChild(mainPanel);
    }

    @Override
    public void update(){

        // WINDOW RESIZE //
        // get cursor delta and update window size and position
        if(activeDrag){
            Vector2f delta = InputManager.instance().getCursorDelta();
            Box box = new Box(getRelativeBox());
            switch(dragEdge){
                case 0: {   // left
                    box.width -= delta.x;
                    box.x += delta.x;
                    break;
                } case 1: { // top
                    box.height -= delta.y;
                    break;
                } case 2: { // right
                    box.width += delta.x;
                    break;
                } case 3: { // bottom
                    box.height += delta.y;
                    box.y -= delta.y;
                    break;
                }

            }
            setBox(box);
            forceTreeLayout();
        }
        else {
            // if no active resize is going on, update the cursor and hover positions
            if (hover) {
                Vector2f coord = InputManager.instance().getCursorPos();
                if (edgeBox[0].relativeTo(this.absoluteBox).isWithin(coord)) {
                    Window.instance().setCursor(GLFW_HRESIZE_CURSOR);
                    dragEdge = 0;
                } else if (edgeBox[1].relativeTo(this.absoluteBox).isWithin(coord)) {
                    Window.instance().setCursor(GLFW_VRESIZE_CURSOR);
                    dragEdge = 1;
                } else if (edgeBox[2].relativeTo(this.absoluteBox).isWithin(coord)) {
                    Window.instance().setCursor(GLFW_HRESIZE_CURSOR);
                    dragEdge = 2;
                } else if (edgeBox[3].relativeTo(this.absoluteBox).isWithin(coord)) {
                    Window.instance().setCursor(GLFW_VRESIZE_CURSOR);
                    dragEdge = 3;
                } else {
                    if (dragEdge != -1) {
                        Window.instance().setCursor(GLFW_ARROW_CURSOR);
                        dragEdge = -1;
                    }
                }
            } else {
                // not within a drag edge so just make cursor act like normal
                if (dragEdge != -1) {
                    Window.instance().setCursor(GLFW_ARROW_CURSOR);
                    dragEdge = -1;
                }
            }
        }

        super.update();
    }

    private void setEdgeBoxs(){
        Vector2i res = getPixelSize();
        float xFactor = (float) viewportSettings.getBorderSize()/res.x;
        float yFactor = (float) viewportSettings.getBorderSize()/res.y;
        // left right top bottom
        edgeBox[0] = new Box(-xFactor,0,2*xFactor, 1);
        edgeBox[1] = new Box(0,1-yFactor,1, 2*yFactor);
        edgeBox[2] = new Box(1-xFactor,0,2*xFactor, 1);
        edgeBox[3] = new Box(0,-yFactor,1, 2*yFactor);
    }
}
