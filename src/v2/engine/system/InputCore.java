package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.system.Window;
import org.joml.Vector2d;

import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.*;

public class InputCore {

    @Getter private HashSet<Integer> pressedButtons;
    @Getter private HashSet<Integer> heldButtons;
    @Getter private HashSet<Integer> releasedButtons;

    @Getter private HashSet<Integer> pressedKeys;
    @Getter private HashSet<Integer> heldKeys;
    @Getter private HashSet<Integer> releasedKeys;

    @Getter private float scrollAmount;

    /** holds previous frame's cursor position for comparison */
    private Vector2d prevPos;
    @Getter @Setter private Vector2d cursorPos;
    @Getter private Vector2d displacement;

    private boolean lockCursor;

    private static InputCore instance = null;
    public static InputCore getInstance(){
        if(instance == null){
            instance = new InputCore();
        }
        return instance;
    }

    private InputCore(){
        pressedButtons = new HashSet<>();
        heldButtons = new HashSet<>();
        releasedButtons = new HashSet<>();
        pressedKeys = new HashSet<>();
        heldKeys = new HashSet<>();
        releasedKeys = new HashSet<>();
    }

    public void init(Window window){

        this.prevPos = new Vector2d(0,0);
        this.cursorPos = new Vector2d(0,0);
        this.displacement = new Vector2d();

        /** Mouse position Callback */
        glfwSetCursorPosCallback(window.getHandle(), (windowHandle, x, y) -> {
            cursorPos.x = x;
            cursorPos.y = y;
        });

        /** Keyboard Callback */
        glfwSetKeyCallback (
                window.getHandle(), (windowHandle, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS){
                pressedKeys.add(key);
                heldKeys.add(key);
            } else if (action == GLFW_RELEASE){
                releasedKeys.add(key);
                heldKeys.remove(key);
            }
            });

        /** Mouse button Callback */
        glfwSetMouseButtonCallback (
                window.getHandle(), (windowHandle, button, action, mods) -> {

            if(button == 2 && action == GLFW_PRESS) {
                lockCursor = true;
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            }
            else if(button == 2 && action == GLFW_RELEASE) {
                lockCursor = false;
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }

            if(action == GLFW_PRESS){
                pressedButtons.add(button);
                heldButtons.add(button);
            } else if (action == GLFW_RELEASE){
                releasedButtons.add(button);
                heldButtons.remove(button);
            }
        });

        /** Mouse scroll Callback */
        glfwSetScrollCallback(window.getHandle(), (windowHandle, dx, dy) -> {
            scrollAmount = (float) dy;
        });
    }

    public void update(){

        /** update displacement vector */

        if(!lockCursor){
            prevPos.x = cursorPos.x;
            prevPos.y = cursorPos.y;

            cursorPos.x += displacement.x;
            cursorPos.y += displacement.y;
        }

        /** update key maps */

        pressedButtons.clear();
        releasedButtons.clear();

        pressedKeys.clear();
        releasedKeys.clear();

        /** reset scroll displacement */
        scrollAmount = 0;

    }

}
