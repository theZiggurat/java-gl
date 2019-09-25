package engine.application.layout;

import org.joml.Vector2i;
import engine.application.element.Element;

import java.util.Optional;

public class TopbarLayout extends Layout {

    public int spacing;

    public TopbarLayout(Element parent, int spacing) {
        super(parent);
        this.spacing = spacing;
    }

    @Override
    public Optional<Box> findRelativeTransform(Element e, int index) {
        Vector2i size = owner.getPixelSize();
        float ratio = (float) size.y / (float) size.x;
        float space = (float) spacing / (float) size.x;

        Box outer = new Box((ratio + space) * index, 0f, ratio, 1f);

        return Optional.of(applyInset(outer, owner.getChildren().get(index).getInset()));
    }

    @Override
    public void update() {

    }
}
