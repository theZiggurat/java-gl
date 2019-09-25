package engine.application.element;

import engine.system.AppContext;
import engine.application.event.Event;
import engine.application.event.InputManager;
import engine.application.event.mouse.*;
import engine.system.Window;

import java.util.ArrayList;
import java.util.Optional;

import static engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;

public class ElementManager {

    private ArrayList<Event> events;

    private Element hovered, root, focused, top, lastAccepted;
    public Optional<Element> eventHog;
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
    public void init(AppContext context){
        this.root = context.getRoot();
        this.focused = root;
        this.hovered = root;
        this.lastAccepted = root;
        this.eventHog = Optional.empty();
    }

    /**
     * Sends events and updates the focused elements
     */
    public void update(){

        if(top == null) top = root.getChildren().get(0);

        for(Event e: events){

            if (eventHog.isPresent())
                eventHog.get().handle(e);

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
                        if(e.isConsumed())
                            lastAccepted = top;
                    }
                    else {
                        prev = root.findAtPos(m.getScreenPos());
                        if (prev == root) continue;
                        setTop(prev);
                        top.handle(e);
                        if(e.isConsumed())
                            lastAccepted = top;
                    }

                    while(!e.isConsumed()) {
                        post = prev.findAtPos(m.getScreenPos());

                        if(post == prev)
                            break;

                        if(post == root) {
                            lastAccepted = root;
                            break;
                        } else {
                            lastAccepted = post.getViewport();
                        }

                        lastAccepted.handle(e);
                        prev = post;
                    }
                } else {
                    if(focusLock) focused.handle(e);
                    else lastAccepted.handle(e);
                }
            } else {
                if(focusLock)focused.handle(e);
                else lastAccepted.handle(e);
            }
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

    public boolean isFocused(Element maybeFocused) {
        if (this.focused == maybeFocused) return true;
        return false;
    }

    /**
     * Resets the focused element to the root of the element tree
     */
    public void resetFocused(){

        this.focusLock = false;
        this.focused = root;
        System.out.println("Focused: " + focused);
    }

    public void setEventHog(Element e){
        if (e != null) eventHog = Optional.of(e);
    }

    public void resetEventHog() {
        eventHog = Optional.empty();
    }

    /**
     * Add event to the queue of events
     * @param e event
     */
    public void fire(Event e){
        events.add(e);
    }

    /**
     * pushes element to front of root list
     */
    public void setTop(Element element) {
        if (root.getChildren().contains(element)) {
            root.getChildren().remove(element);
        }
        root.getChildren().add(0, element);
        this.top = element;
    }

    public boolean isMouseOver(Element _element) {
        for (Element elem: root.getChildren()) {
            if (elem.getAbsoluteBox().isWithin(Window.instance().getCursorPosf())) {
                if (_element == elem) return true;
                else return false;
            }
        }
        return false;
    }

    public boolean isTop(Element _element) {
        if (_element == this.top) return true;
        else return false;
    }

}
