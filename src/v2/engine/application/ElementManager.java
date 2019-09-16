package v2.engine.application;

import jdk.internal.util.xml.impl.Input;
import v2.engine.application.element.Element;
import v2.engine.application.element.RootElement;
import v2.engine.application.event.Event;
import v2.engine.application.event.InputManager;
import v2.engine.application.event.mouse.*;
import v2.engine.system.Window;

import java.util.ArrayList;

import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;

public class ElementManager {

    private ArrayList<Event> events;

    private Element hovered, root, focused, top, lastAccepted;
    private boolean focusLock;

    private static ElementManager instance;
    public static ElementManager instance(){
        if(instance == null)
            instance = new ElementManager();
        return instance;
    }

    private ElementManager(){
        events = new ArrayList<>();
    }

    /**
     * Initialize parameters that need access to applications data
     * @param context currently running context
     */
    public void init(ApplicationContext context){
        this.root = context.getRoot();
        this.focused = root;
        this.hovered = root;
        this.lastAccepted = root;
    }

    /**
     * Sends events and updates the focused elements
     */
    public void update(){

        if(top == null) top = root.getChildren().get(0);

        for(Event e: events){

            // if event is mouse event, the event could be pointing at something
            // further down the element tree. We must first pass the event to the
            // first element that is either
            //      1. A leaf, so its enclosing area has nothing over it
            //      2. Controlling, as in isControlling() returns true. This
            //         flag forces any refocusing to go through it first
            if(e instanceof MouseClickEvent){
                MouseClickEvent m = (MouseClickEvent)e;
                if(m.getAction() == BUTTON_CLICK){

                    // if were focus locked, we only want to look at elements
                    // deeper in the element tree than the focus element.

                    Element prev, post;
                    if(focusLock) {
                        focused.handle(m);
                        prev = focused;
                    }
                    else if(top.getAbsoluteBox().isWithin(m.getScreenPos())) {
                        top.handle(e);
                        prev = top;
                    }
                    else {
                        prev = root.findAtPos(m.getScreenPos());
                        if (prev == root) continue;
                        top = prev;
                        top.handle(e);
                        ((RootElement)root).setTop(top);
                    }

                    while(!e.isConsumed()) {
                        post = prev.findAtPos(m.getScreenPos());

                        if(post == prev)
                            break;

                        if(post == root) {
                            lastAccepted = root;
                            break;
                        }

                        lastAccepted = post;
                        lastAccepted.handle(e);
                        prev = post;
                    }
                } else { if(focusLock)focused.handle(e); else lastAccepted.handle(e); }
            } else { if(focusLock)focused.handle(e); else lastAccepted.handle(e); }
        }
        // we processed all the events, so we can clear til next frame
        events.clear();

        Element elem;
        if(focusLock || Window.instance().isHidden())
            return;

        elem = root.findAtPos(InputManager.instance().getCursorPos());

        // update if the element being hovered over changed
        if(elem != hovered){
            hovered.handle(new HoverLostEvent());
            hovered = elem;
            elem.handle(new HoverStartEvent());
        }

    }

    /**
     * Force this element to be the first to recieve any events
     * @param focused element to handle all incoming events
     */
    public void setFocused(Element focused){
        if(focusLock) return;
        this.focused = focused;
        this.focusLock = true;
        System.out.println("Focused: " + focused);
    }

    /**
     * Resets the focused element to the root of the element tree
     */
    public void resetFocused(){

        this.focusLock = false;
        this.focused = root;
        System.out.println("Focused: " + focused);
    }

    /**
     * Add event to the queue of events
     * @param e event
     */
    public void fire(Event e){
        events.add(e);
    }

}
