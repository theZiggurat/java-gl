package v2.engine.scene.node;

import lombok.Getter;
import lombok.Setter;
import v2.engine.scene.node.ModuleNode;

public class Module {

    @Getter @Setter
    private ModuleNode parent;

    public void update(){}
    public void render(){}
    public void cleanup(){}

}


