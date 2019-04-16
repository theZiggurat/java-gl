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

    // pls dont look at this too long
    public void setTop(Element e){
        Collections.reverse(getChildren());
        getChildren().remove(e);
        getChildren().add(0, e);
        Collections.reverse(getChildren());
    }

}
