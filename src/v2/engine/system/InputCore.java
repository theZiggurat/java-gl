package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.system.Window;
import org.joml.Vector2d;

import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.*;


/**
 * Responsible for handling window input callbacks for convienent retrieval
 */
public class InputCore {

    private HashSet<Integer> pressedButtons;
    private HashSet<Integer> heldButtons;
    private HashSet<Integer> releasedButtons;

    private HashSet<Integer> pressedKeys;
    private HashSet<Integer> heldKeys;
    private HashSet<Integer> releasedKeys;

    @Getter private float scrollAmount;

    private Vector2d prevPos;
    @Getter @Setter private Vector2d cursorPos;
    @Getter private Vector2d displacement;

    @Getter private boolean lockCursor;
    @Getter @Setter Vector2d lockCursorPos;

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

    /**
     * Initializes callbacks
     * @param window to make callbacks from
     */
    public void init(Window window){

        this.prevPos = new Vector2d();
        this.cursorPos = new Vector2d();
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


            if(button == 1 && action == GLFW_PRESS) {
                lockCursor = true;
                lockCursorPos = new Vector2d(cursorPos);
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            }
            else if(button == 1 && action == GLFW_RELEASE) {
                lockCursor = false;
                glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                glfwSetCursorPos(windowHandle, lockCursorPos.x, lockCursorPos.y);
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

    /**
     * Returns true for GLFW keycode of any key hold this update
     * @param keycode GLFW keycode
     * @return boolean held or not
     */
    public boolean isKeyHeld(int keycode){
        return heldKeys.contains(keycode);
    }

    /**
     * Returns true for GLFW keycode of any key pressed this update
     * @param keycode GLFW keycode (key)
     * @return boolean pressed or not
     */
    public boolean isKeyPressed(int keycode){
        return pressedKeys.contains(keycode);
    }

    /**
     * Returns true for GLFW keycode of any key released this update
     * @param keycode GLFW keycode (key)
     * @return boolean released or not
     */
    public boolean isKeyReleased(int keycode){
        return releasedKeys.contains(keycode);
    }

    /**
     * Returns true for GLFW keycode of any button hold this update
     * @param keycode GLFW keycode (buttons)
     * @return boolean held or not
     */
    public boolean isButtonHeld(int keycode){
        return heldButtons.contains(keycode);
    }

    /**
     * Returns true for GLFW keycode of any button pressed this update
     * @param keycode GLFW keycode (buttons)
     * @return boolean pressed or not
     */
    public boolean isButtonPressed(int keycode){
        return pressedButtons.contains(keycode);
    }

    /**
     * Returns true for GLFW keycode of any button released this update
     * @param keycode GLFW keycode (buttons)
     * @return boolean released or not
     */
    public boolean isButtonReleased(int keycode){
        return releasedButtons.contains(keycode);
    }

    /**
     * Clears the key and button maps and updates displacement vectors
     */
    public void update(){

        /** update displacement vector */

        displacement.x = cursorPos.x - prevPos.x;
        displacement.y = cursorPos.y - prevPos.y;

        prevPos.x = cursorPos.x;
        prevPos.y = cursorPos.y;


        /** update key maps */

        pressedButtons.clear();
        releasedButtons.clear();

        pressedKeys.clear();
        releasedKeys.clear();

        /** reset scroll displacement */
        scrollAmount = 0;
    }

}
