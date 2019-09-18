package v2.engine.scene.node;

import lombok.Getter;
import v2.modules.sky.Sky;

public class Scenegraph extends Node {

    @Getter
    private Sky sky;

    public Scenegraph() {
        super();

        this.sky = new Sky();
    }

}
