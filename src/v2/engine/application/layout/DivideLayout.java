package v2.engine.application.layout;

import v2.engine.application.element.Element;

public class DivideLayout extends Layout {

    public DivideLayout(Element parent){
        super(parent);
    }

    public Box findRelativeTransform(Element e, int index){
        float loc = (float)index/(float)parent.getChildren().size();
        Box outer = new Box(0, 1-loc-1/(float)parent.getChildren().size(),
                1, 1/(float)parent.getChildren().size());
        return new Box(0.2f, 0.2f, 0.6f, 0.6f).relativeTo(outer);
    }
}
