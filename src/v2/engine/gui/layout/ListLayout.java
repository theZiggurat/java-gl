package v2.engine.gui.layout;

import v2.engine.gui.Box;
import v2.engine.gui.element.Element;

public class ListLayout extends Layout {

    public ListLayout(Element parent){
        super(parent);


    }

    @Override
    public Box findRelativeTransform(Element e, int index) {
        return null;
    }
}
