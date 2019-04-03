package v2.engine.gui.element;

import v2.engine.gui.Box;

public class RootElement extends Element{

    public RootElement(){
        super();
        relativeBox = new Box(0,0,1,1);
        absoluteBox = new Box(0,0,1,1);
    }
}
