package v2.engine.application.element;

import v2.engine.application.layout.Box;

import java.util.Collections;

public class RootElement extends Element{

    public RootElement(){
        super();
        relativeBox = new Box(0,0,1,1);
        absoluteBox = new Box(0,0,1,1);
    }

    public void forceLayout(){
        forceTreeLayout();
    }

    @Override
    public void render() {
        Collections.reverse(getChildren());
        getChildren().stream()
                .filter(e -> e.isActivated())
                .forEach(e -> e.render());
        Collections.reverse(getChildren());
    }

}
