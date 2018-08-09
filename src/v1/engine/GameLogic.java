package v1.engine;

public interface GameLogic {

    void init() throws Exception;

    void input();

    void update(double interval);

    void render();

    void cleanup();

}
