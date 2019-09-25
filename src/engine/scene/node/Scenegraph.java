package engine.scene.node;

import lombok.Getter;
import modules.sky.Sky;

public class Scenegraph extends Node {

    @Getter
    private Sky sky;

    public Scenegraph() {
        super();

        this.sky = new Sky();
    }

}
