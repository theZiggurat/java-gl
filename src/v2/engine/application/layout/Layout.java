package v2.engine.application.layout;

import lombok.Getter;
import v2.engine.application.element.Element;

import java.util.Optional;

public abstract class Layout {

    protected @Getter Element owner;

    public Layout(Element parent){
        this.owner = parent;
    }

    public abstract Optional<Box> findRelativeTransform(Element e, final int index);
    public abstract void update();
}
