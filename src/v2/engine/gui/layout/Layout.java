package v2.engine.gui.layout;

import lombok.Getter;
import v2.engine.gui.Box;
import v2.engine.gui.element.Element;

public abstract class Layout {

    protected @Getter Element parent;

    public Layout(Element parent){
        this.parent = parent;
    }

    public abstract Box findRelativeTransform(Element e, int index);
}
