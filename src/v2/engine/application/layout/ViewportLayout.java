package v2.engine.application.layout;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.element.Element;

import java.util.Optional;

public class ViewportLayout extends Layout{

    @Setter @Getter int topBarHeight;

    public ViewportLayout(Element parent){
        super(parent);
    }

    public void update() {

    }

    public Optional<Box> findRelativeTransform(Element e, int index){
        float factorTop = (float)topBarHeight/(float) owner.getAbsoluteBox().resolution().y;
        if(index == 0){ // top bar
            return Optional.of(new Box(0, 1-factorTop, 1, factorTop));
        } else if (index == 1){ // edge panel
            return Optional.of(new Box(0, 0, 1, 1-factorTop));
        } else {
            Inset inset = owner.getChildren().get(index).getInset();
            Box box = new Box(0, 0, 1, 1-factorTop);
            return Optional.of(applyInset(box, inset));
        }
    }
}
