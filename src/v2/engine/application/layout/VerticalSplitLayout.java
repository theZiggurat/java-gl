package v2.engine.application.layout;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.element.Element;

import java.util.Optional;

public class VerticalSplitLayout extends Layout{

    @Setter @Getter int heightTop;
    @Setter @Getter int heightBottom;

    public VerticalSplitLayout(Element parent){
        super(parent);
    }

    public void update() {

    }

    public Optional<Box> findRelativeTransform(Element e, int index){
        float factorTop = (float)heightTop/(float) owner.getAbsoluteBox().resolution().y;
        float factorBottom = (float)heightBottom/(float) owner.getAbsoluteBox().resolution().y;
        if(index == 0){
            return Optional.of(new Box(0, 1-factorTop, 1, factorTop));
        } else if (index == 1){
            return Optional.of(new Box(0, 0, 1, 1-factorBottom));
        }
        return null;
    }
}
