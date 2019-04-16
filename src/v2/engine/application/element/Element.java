package v2.engine.application.element;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import v2.engine.application.event.*;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.layout.Box;
import v2.engine.application.layout.Layout;
import v2.engine.application.layout.AbsoluteLayout;
import v2.engine.system.Window;
import v2.engine.utils.Utils;

import java.util.ArrayList;

public abstract class Element {

    /**
     * Decides whether or not this element's layout
     * should be decided by it's parents. If enabled,
     * an element can choose its absolute layout
     * by calling setAbsoluteBox()
     */
    @Getter @Setter boolean attached = false;

    /**
     * Decides whether element will let children
     * recieve events on their own or if this element
     * will have to explicitly pass down events to children
     */
    @Getter @Setter boolean controlling = false;

    /**
     * Spatial data of element
     */
    @Getter protected Box relativeBox, absoluteBox;

    @Setter protected Layout layout;

    @Setter @Getter boolean activated = true;
    @Getter @Setter boolean changed = true;

    @Getter private ArrayList<Element> children;
    @Getter @Setter private Element parent;
    private ArrayList<Event.EventHandler> handlers;

    @Getter private int UUID;

    protected Element(){
        children = new ArrayList<>(5);
        handlers = new ArrayList<>(2);
        layout = new AbsoluteLayout(this);
        relativeBox = new Box(0,0,0,0);
        absoluteBox = new Box(0,0,0,0);
        UUID = Utils.generateNewUUID();
    }

    /**
     * Renders all children. OpenGL functionality will have to
     * be added by subclasses
     */
    public void render(){
        children.stream()
                .filter(e -> e.isActivated())
                .forEach(e -> e.render());
    }

    /**
     * For a sub-element to take advantage of if it requires constant
     * re-calculation
     */
    public void update(){
        children.stream()
                .filter(e -> e.isActivated())
                .forEach(e -> e.update());
    }

    /**
     * Adds child element to this element
     * A child gains the following by being added as a child
     * (assuming it's attached in some way to the ApplicationContext
     * root)
     *
     *  1. Layout information and resize events
     *  2. Ordered rendering (parent renders before child)
     *  3. Registered for event traversal
     *  4. Registered for render traversal
     * @param e event to add
     */
    public void addChild(Element e){
        children.add(e);
        e.setParent(this);
    }

    /**
     * Read addChild() for detailed documentation
     * @param elements collection of elements
     */
    public void addChildren(Element... elements){
        for(Element e: elements){
            children.add(e);
            e.setParent(this);
        }
    }

    /**
     * This element and downwards on the graph, the element tree is spatially
     * laid out based on rules set by each element's Layout object
     */
    protected void layoutChildren(){
        int i = 0;
        for(Element child: children){
            if(child.isAttached()) continue;
            if(child.setBox(layout.findRelativeTransform(child, i++))) {

                child.handle(new ResizeEvent());
                child.layoutChildren();
            }
        }
    }

    protected void forceTreeLayout(){
        int i = 0;
        for(Element child: children){
            if(child.isAttached()) continue;
            child.setBox(layout.findRelativeTransform(child, i++));
            child.handle(new ResizeEvent());
            child.forceTreeLayout();
        }
    }

    /**
     * Get this elements size in pixels
     * @return resolution of element
     */
    public Vector2i getPixelSize(){
        Vector2i ret = new Vector2i();
        Box box = getAbsoluteBox();
        ret.x = (int) (Window.instance().getWidth() * box.getWidth());
        ret.y = (int) (Window.instance().getHeight() * box.getHeight());
        return ret;
    }

    /**
     * Sets new relative based on input relative box
     * Also sets absolute box by using data from parent element
     * if either of these were changed, method returns true
     * such that the caller can know if they need to update or render
     * @param newRelative relative box this element will be set to
     * @return true if box changed, false if it didn't
     */
    public boolean setBox(Box newRelative){
        boolean ret = true;
        if(getRelativeBox().equals(newRelative)) ret = false;
        else this.relativeBox.set(newRelative);
        Box newAbsolute = relativeBox.relativeTo(parent.absoluteBox);
        if(!getAbsoluteBox().equals(newAbsolute)) ret = true;
        this.absoluteBox.set(newAbsolute);
        return ret;
    }

    public boolean setAbsoluteBox(Box newAbsolute){
        if(!attached) return false;
        if(this.absoluteBox.equals(newAbsolute)) return false;
        this.absoluteBox.set(newAbsolute);
        return true;
    }

    /**
     * Appends event handler to front of list
     * This means on any subclass of class that derives from Element,
     * if that subclass calls super() before calling onEvent(), the super
     * class's handler functionality can be overwritten by consuming the event
     * @param handler
     */
    public void onEvent(Event.EventHandler handler){
        handlers.add(0, handler);
    }

    /**
     * Generic event handler that simply goes through each handler in the handler
     * list until the event is consumed
     * @param event event that this element needs to handle
     */
    public void handle(Event event){
        for(Event.EventHandler handler: handlers){
            handler.handle(event);
            if(event.isConsumed()) return;
        }
    }

    public Element findAtPos(Vector2f pos){
        Element ret = this;
        boolean leaf = false;
        while(!leaf){

            if(ret.isControlling()&&ret!=this) break;
            leaf = true;
            for(Element element: ret.getChildren()){
                if(element.getAbsoluteBox().isWithin(pos)){
                    ret = element;
                    leaf = false;
                    break;
                }
            }
        }
        return ret;
    }


    public void cleanup(){
        children.stream().forEach(e -> e.cleanup());
    }

//    @Override
//    public String toString(){
//        StringBuilder builder = new StringBuilder();
//        builder.append(getClass()+ "\n");
//        for(Element child: children)
//            builder.append("    - " + child.getClass() + "\n");
//        return builder.toString();
//    }

}
