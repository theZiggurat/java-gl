package v2.engine.application.event;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2d;
import org.joml.Vector2f;
import v2.engine.application.ElementManager;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.event.mouse.MouseWheelEvent;
import v2.engine.application.ApplicationContext;
import v2.engine.system.Window;

import java.util.HashSet;

import static org.lwjgl.glfw.GLFW.*;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_HELD;
import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_RELEASED;


/**
 * Responsible for handling window input callbacks for convienent retrieval
 */
public class InputManager {

    @Getter private int mods = 0;

    HashSet<Integer> pressedButtons;
    HashSet<Integer> heldButtons;
    HashSet<Integer> releasedButtons;

    HashSet<Integer> pressedKeys;
    HashSet<Integer> heldKeys;
    HashSet<Integer> releasedKeys;

    @Getter private float scrollAmount;

    private Vector2d prevPos;
    @Getter @Setter private Vector2d cursorPos;
    @Getter private Vector2d displacement;

    private ApplicationContext context;

    private static InputManager instance = null;
    public static InputManager instance(){
        if(instance == null){
            instance = new InputManager();
        }
        return instance;
    }

    private InputManager(){
        pressedButtons = new HashSet<>();
        heldButtons = new HashSet<>();
        releasedButtons = new HashSet<>();
        pressedKeys = new HashSet<>();
        heldKeys = new HashSet<>();
        releasedKeys = new HashSet<>();
    }

    /**
     * Initializes callbacks
     */
    public void init(ApplicationContext context){

        this.context = context;
        Window window = Window.instance();

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

            int _action = 0;
            this.mods = mods;

            if(action == GLFW_PRESS){

                pressedKeys.add(key);
                heldKeys.add(key);
                _action = KeyboardEvent.KEY_PRESSED;

            }

            else if(action == GLFW_REPEAT){
                _action = KeyboardEvent.KEY_HELD;
                pressedKeys.remove(key);
            }

            else if (action == GLFW_RELEASE){
                //this.mods = 0;
                releasedKeys.add(key);
                heldKeys.remove(key);
                pressedKeys.remove(key);
                _action = KeyboardEvent.KEY_RELEASED;
            }

            ElementManager.instance().fire(
                new KeyboardEvent(key, _action, mods)
            );

        });

        /** Mouse button Callback */
        glfwSetMouseButtonCallback (
                window.getHandle(), (windowHandle, button, action, mods) -> {

            int _action = 0;
                    this.mods = mods;

            if(action == GLFW_PRESS){
                pressedButtons.add(button);
                _action = BUTTON_CLICK;
            }
            else if (action == GLFW_RELEASE){
                this.mods = 0;
                releasedButtons.add(button);
                heldButtons.remove(button);
                pressedButtons.remove(button);
                _action = BUTTON_RELEASED;
            }


            ElementManager.instance().fire(new MouseClickEvent(
                button, _action, mods, new Vector2f (
                    (float)cursorPos.x/(float)Window.instance().getWidth(),
                    1f-(float)cursorPos.y/(float)Window.instance().getHeight())
                    , new Vector2f (
                    (float)displacement.x/(float)Window.instance().getWidth(),
                    (float)displacement.y/(float)Window.instance().getHeight())
            ));
        });

        /** Mouse scroll Callback */
        glfwSetScrollCallback(window.getHandle(), (windowHandle, dx, dy) -> {
            scrollAmount = (float) dy;
            ElementManager.instance().fire(new MouseWheelEvent(
                dy, new Vector2f(
                (float) cursorPos.x / (float) Window.instance().getWidth(),
                1f - (float) cursorPos.y / (float) Window.instance().getHeight())
            ));
        });
    }

    public boolean isMod(int keycode){ return (keycode & mods) == keycode;}

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

        for(Integer k: pressedButtons){
            ElementManager.instance().fire(new MouseClickEvent(k, BUTTON_HELD, mods, new Vector2f (
                (float)cursorPos.x/(float)Window.instance().getWidth(),
                1f-(float)cursorPos.y/(float)Window.instance().getHeight())
                , new Vector2f (
                (float)displacement.x/(float)Window.instance().getWidth(),
                -(float)displacement.y/(float)Window.instance().getHeight())
            ));
        }

        /** update key maps */

        releasedButtons.clear();
        releasedKeys.clear();

        /** reset scroll displacement */
        scrollAmount = 0;


    }

    public Vector2f getCursorPos() {
        return new Vector2f(
                (float) cursorPos.x / (float) Window.instance().getWidth(),
                1f - (float) cursorPos.y / (float) Window.instance().getHeight());
    }

    public Vector2f getCursorDelta(){
        return new Vector2f (
                (float)displacement.x/(float)Window.instance().getWidth(),
                (float)displacement.y/(float)Window.instance().getHeight());
    }

}
