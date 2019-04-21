package v2.engine.application.element;

import lombok.AccessLevel;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import v2.engine.application.layout.Box;
import v2.engine.application.event.InputManager;
import v2.engine.application.event.ResizeEvent;
import v2.engine.application.event.mouse.HoverLostEvent;
import v2.engine.application.event.mouse.HoverStartEvent;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.ApplicationContext;
import v2.engine.application.layout.VerticalSplitLayout;
import v2.engine.system.Window;
import v2.engine.utils.Color;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
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

    // child panels
    protected Panel topBar;
    @Setter(AccessLevel.PROTECTED)
    protected Panel mainPanel;

    protected Viewport(){

        super();

        this.topBar = new Panel();
        topBar.setColor(new Color(0x353535));
        topBar.setRounding(5,5,0,0);

        edgeBox = new Box[4];
        setControlling(true);


        // update viewport state
        onEvent(e -> {

            if(e instanceof ResizeEvent)
                _setEdgeBox();

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
            }
        });

        // make viewport draggable by the window
        topBar.onEvent(e -> {

            if(e instanceof MouseClickEvent){

                // move window
                MouseClickEvent m = (MouseClickEvent)e;
                if(m.getAction()==BUTTON_HELD){
                    getRelativeBox().translate(m.getScreenDelta());
                    getParent().layoutChildren();
                }
            }
        });

        // create window layout
        VerticalSplitLayout vsp = new VerticalSplitLayout(this);
        vsp.setHeightBottom(24);
        vsp.setHeightTop(24);
        setLayout(vsp);

        // add sub panels as children
        addChild(topBar);
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
                    setBox(box);
                    break;
                } case 1: { // top
                    box.height -= delta.y;
                    setBox(box);
                    break;
                } case 2: { // right
                    box.width += delta.x;
                    setBox(box);
                    break;
                } case 3: { // bottom
                    box.height += delta.y;
                    box.y -= delta.y;
                    setBox(box);
                    break;
                }

            }
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
    }

    @Override
    public void render(){
        topBar.render();
        glDisable(GL_DEPTH_TEST);
        mainPanel.render();
        glEnable(GL_DEPTH_TEST);
    }

    private void _setEdgeBox(){
        Vector2i res = getPixelSize();
        float xFactor = (float) edgeSize/res.x;
        float yFactor = (float) edgeSize/res.y;
        // left right top bottom
        edgeBox[0] = new Box(-xFactor,0,2*xFactor, 1);
        edgeBox[1] = new Box(0,1-yFactor,1, 2*yFactor);
        edgeBox[2] = new Box(1-xFactor,0,2*xFactor, 1);
        edgeBox[3] = new Box(0,-yFactor,1, 2*yFactor);
    }
}
