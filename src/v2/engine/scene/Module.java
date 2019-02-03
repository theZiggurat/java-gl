package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;

public class Module {

    @Getter @Setter
    private ModuleNode parent;

    public void update(){}
    public void render(){}
    public void renderWireframe(){}
    public void renderOverlay(){}
    public void cleanup(){}


}


