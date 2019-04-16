package v2.engine.application.element;

import lombok.AccessLevel;
import lombok.Setter;
import v2.engine.application.layout.Box;
import v2.engine.glapi.tex.TextureObject;

public class TextureViewport extends Viewport {

    @Setter(AccessLevel.PACKAGE) private TextureObject texture;

    public TextureViewport(TextureObject texture){
        super();
        this.texture = texture;

        setMainPanel(new Panel());
        mainPanel.setImageBuffer(texture);
        mainPanel.setImage(true);

        relativeBox = new Box(.525f,0.1f,0.45f,0.8f);

        addChild(mainPanel);
    }
}
