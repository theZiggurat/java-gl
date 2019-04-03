package v2.engine.gui.element;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2i;
import v2.engine.event.Event;
import v2.engine.event.MouseEvent;
import v2.engine.gui.Box;
import v2.engine.gui.layout.Layout;
import v2.engine.gui.layout.AbsoluteLayout;
import v2.engine.system.Window;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Element {

    @Getter protected Box relativeBox, absoluteBox;

    @Setter protected Layout layout;

    @Setter @Getter private Event.MouseEventHandler mouseEventHandler;

    @Setter @Getter boolean activated = true;
    @Getter @Setter boolean changed = true;

    @Getter private ArrayList<Element> children;
    @Getter @Setter private Element parent;

    protected Element(){
        children = new ArrayList<>();
        layout = new AbsoluteLayout(this);
        relativeBox = new Box(0,0,0,0);
        absoluteBox = new Box(0,0,0,0);

        applyDefaultHandlers();
    }

    private void applyDefaultHandlers(){
        setMouseEventHandler( e -> {

        });
    }

    public void render(){
        children.stream()
                .filter(e -> e.isActivated())
                .forEach(e -> e.render());
    }

    public void addChild(Element e){
        children.add(e);
        e.setParent(this);
    }

    public void addChildren(Element... elements){
        for(Element e: elements){
            children.add(e);
            e.setParent(this);
        }
    }

    protected void layoutChildren(){
        int i = 0;
        for(Element child: children){
            child.relativeBox = layout.findRelativeTransform(child, i++);
            child.absoluteBox = child.relativeBox.within(absoluteBox);
            child.layoutChildren();
        }
    }

    public Vector2i getAbsolutePixelSize(){
        Vector2i ret = new Vector2i();
        Box box = getAbsoluteBox();
        ret.x = (int) (Window.instance().getWidth() * box.getWidth());
        ret.y = (int) (Window.instance().getHeight() * box.getHeight());
        return ret;
    }

    public void handle(){
        children.stream().filter(e -> e.isActivated()).forEach(e -> e.handle());
    }

    public void cleanup(){
        children.stream().forEach(e -> e.cleanup());
    }
}
