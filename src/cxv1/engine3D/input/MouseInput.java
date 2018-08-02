package cxv1.engine3D.input;

import cxv1.engine3D.util.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private Vector2d prev;
    private Vector2d curr;
    private Vector2f displacement;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private boolean leanLeft = false;
    private boolean leanRight = false;

    private static MouseInput instance = null;

    public static MouseInput getInstance(){
        if(instance == null){
            instance = new MouseInput();
        }
        return instance;
    }

    public void init(Window window){

        this.prev = new Vector2d(0,0);
        this.curr = new Vector2d(0,0);
        this.displacement = new Vector2f();

        glfwSetCursorPosCallback(window.getHandle(), (windowHandle, x, y) -> {
            curr.x = x;
            curr.y = y;
        });
        glfwSetCursorEnterCallback(window.getHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getHandle(),
                (windowHandle, button, action, mode) ->{
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
        if(window.isKeyPressed(GLFW_KEY_Q)){
            leanLeft = true;
        } else {
            leanLeft = false;
        }

        if(window.isKeyPressed(GLFW_KEY_E)){
            leanRight = true;
        } else {
            leanRight = false;
        }
    }

    public void input(){
        displacement.x = 0;
        displacement.y = 0;

        if(curr.x - prev.x !=0){ displacement.y = (float) (curr.x - prev.x);}
        if(curr.y - prev.y !=0){ displacement.x = (float) (curr.y - prev.y);}


        prev.x = curr.x;
        prev.y = curr.y;
    }

    public Vector2f getDisplacement(){ return displacement; }
    public boolean isRightPressed(){return rightButtonPressed;}
    public boolean isLeftPressed(){return leftButtonPressed;}

    public boolean isLeanLeft() {
        return leanLeft;
    }

    public boolean isLeanRight() {
        return leanRight;
    }
}
