package v2.engine.application.layout;

import lombok.Getter;
import org.joml.Vector2i;
import v2.engine.application.element.Element;

import java.util.Optional;

public abstract class Layout {

    protected @Getter Element owner;

    public Layout(Element parent){
        this.owner = parent;
    }

    public Box applyInset(Box input, Inset inset) {
        Vector2i inputPixelSize = owner.getPixelSizeForRelative(input);
        float x = (float) inset.left / (float) inputPixelSize.x;
        float y = (float) inset.bottom / (float) inputPixelSize.y;
        float width = 1 - ((float) inset.right / (float) inputPixelSize.x) - x;
        float height = 1 - ((float) inset.top / (float) inputPixelSize.y) - y;
        return (new Box(x, y, width, height)).relativeTo(input);
    }

    public abstract Optional<Box> findRelativeTransform(Element e, final int index);
    public abstract void update();
}
