package v2.engine.application.element;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.layout.Box;
import v2.engine.scene.SceneContext;


public class SceneViewport extends Viewport {

    @Getter @Setter private int borderSize = 4;

    private SceneContext context;

    public SceneViewport(SceneContext sceneContext){

        super();

        this.context = sceneContext;

        mainPanel = new ScenePanel(sceneContext);
        mainPanel.setRounding(0,0,10,10);
        mainPanel.setBorderSize(borderSize);

        relativeBox = new Box(0.06f, 0.1f, 0.45f, 0.8f);

        addChild(mainPanel);

    }


}
