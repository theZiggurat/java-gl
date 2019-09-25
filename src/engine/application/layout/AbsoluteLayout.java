package engine.application.layout;

import engine.application.element.Element;

import java.util.Optional;

public class AbsoluteLayout extends Layout {

    public AbsoluteLayout(Element e) {
        super(e);
    }

    /**
     * Absolute layout that takes the entire screen no matter the input
     * and is default the parameter in the Element class
     * @param e any element object
     * @param index index of element in owner heirarchy
     * @return Box where element conforms to
     */
    @Override
    public Optional<Box> findRelativeTransform(Element e, final int index) {
        return Optional.of(e.getRelativeBox());
    }

    @Override
    public void update() {

    }
}
