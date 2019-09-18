package v2.engine.application.element.viewport;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.element.panel.ScenePanel;
import v2.engine.application.layout.Box;
import v2.engine.application.layout.Inset;
import v2.engine.scene.SceneContext;


public class SceneViewport extends Viewport {

    @Getter private SceneContext context;

    public SceneViewport(SceneContext sceneContext) {
        this(sceneContext, new ViewportSettings());
    }

    public SceneViewport(SceneContext sceneContext, ViewportSettings vs){

        super(vs);

        this.context = sceneContext;

        ScenePanel panel = new ScenePanel(sceneContext);
        setMainPanel(panel);

    }


}
