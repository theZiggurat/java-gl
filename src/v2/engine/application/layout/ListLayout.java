package v2.engine.application.layout;

import v2.engine.application.element.Element;

public class ListLayout extends Layout {

    public ListLayout(Element parent){
        super(parent);


    }

    @Override
    public Box findRelativeTransform(Element e, int index) {
        return null;
    }
}
