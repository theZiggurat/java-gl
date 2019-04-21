package v2.engine.application.element;

import v2.engine.application.layout.Box;
import v2.engine.glapi.tex.TextureObject;

public class TextureViewport extends Viewport {

    public TextureViewport(TextureObject texture){
        super();

        setMainPanel(new Panel());
        mainPanel.setImageBuffer(texture);
        mainPanel.setImage(true);
        mainPanel.setDrawBorder(true);
        mainPanel.setBorderSize(4);

        relativeBox = new Box(.525f,0.15f,0.4f,0.7f);

        addChild(mainPanel);
    }
}
