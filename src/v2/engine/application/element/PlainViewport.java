package v2.engine.application.element;

import v2.engine.application.layout.Box;
import v2.engine.utils.Color;

public class PlainViewport extends Viewport {

    public PlainViewport(){
        super();

        mainPanel = new Panel();
        mainPanel.setColor(new Color(0x202020));

        relativeBox = new Box(0.05f, 0.05f, 0.15f, 0.6f);

        addChild(mainPanel);
    }
}
