package v2.engine.system;

import v2.instances.PBRTest;

public interface EngineInterface {

    void init();
    void update(double duration);
    void render();
    void cleanup();

}
