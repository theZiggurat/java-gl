package v2.engine.gui.layout;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2i;
import v2.engine.gui.Box;
import v2.engine.gui.element.Element;

public class VerticalSplitLayout extends Layout{

    @Setter @Getter float cutoffY;

    public VerticalSplitLayout(Element parent){
        super(parent);
        cutoffY = 0.5f;
    }

    public void setCutoffY(int pixels){
        Vector2i pixSize = getParent().getAbsolutePixelSize();
        cutoffY = (float)pixels/(float)pixSize.y;
    }

    public Box findRelativeTransform(Element e, int index){
        if(index == 0){
            return new Box(0, 1-cutoffY, 1, cutoffY);
        } else if (index == 1){
            return new Box(0, 0, 1, 1-cutoffY);
        }
        return null;
    }
}
