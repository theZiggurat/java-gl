package cxv1.engine3D;

import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.util.Window;

public interface GameLogic {
    void init(Window window) throws Exception;
    void input(Window window, MouseInput mouseInput);
    void update(double interval, MouseInput mouseInput);
    void render(Window window, double FPS);
    void cleanup();
}
