package v2.engine.application.layout;

import v2.engine.application.element.Element;

import java.util.Optional;

public class ListLayout extends Layout {

    public ListLayout(Element parent){
        super(parent);


    }

    @Override
    public Optional<Box> findRelativeTransform(Element e, int index) {
        return Optional.empty();
    }

    @Override
    public void update() {

    }
}
