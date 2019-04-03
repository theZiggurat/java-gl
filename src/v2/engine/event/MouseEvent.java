package v2.engine.event;

import lombok.Getter;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Getter
public class MouseEvent extends Event {

    Vector2d glfwScreenPos;
    Vector2i pixelScreenPos;
    Vector2f ndcScreenPos;

    int key;
    int action;
    int mods;

    MouseEvent(int key, int action, int mods, Vector2d glfwScreenPos){
        this.key = key;
        this.action = action;
        this.mods = mods;
        this.glfwScreenPos =
    }


}
