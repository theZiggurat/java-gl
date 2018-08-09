package v2.instances;

import v2.engine.scene.Scenegraph;
import v2.engine.system.EngineInterface;
import v2.engine.system.RenderEngine;

public class PBRTest implements EngineInterface {

    private Scenegraph scene;

    @Override
    public void init() {

        scene = RenderEngine.getInstance().getScenegraph();

    }

    @Override
    public void update(double duration) {

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
