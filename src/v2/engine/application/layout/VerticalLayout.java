package v2.engine.application.layout;

import org.joml.Vector2i;
import v2.engine.application.element.Element;

import java.util.Optional;

public class VerticalLayout extends Layout {

    int elementHeight;
    int scrollHeight = 0;

    public VerticalLayout(Element parent, int elementHeight){
        super(parent);
        this.elementHeight = elementHeight;
    }

    public void update() {

    }

    public Optional<Box> findRelativeTransform(Element e, int index){

        int heightPixelY = index * elementHeight - scrollHeight;

        if (index >= owner.getChildren().size() || heightPixelY > owner.getPixelSize().y)
            return Optional.of(new Box(0,0,0,0));

        float heightRelativeY = (float) heightPixelY / (float) owner.getPixelSize().y;
        float sizeRelativeY = (float) elementHeight / (float) owner.getPixelSize().y;

        Box outer = new Box(0, 1 - heightRelativeY - sizeRelativeY,
                1,  sizeRelativeY);
        Inset inset = owner.getChildren().get(index).getInset();

        return Optional.of(applyInset(outer, inset));
    }
}
