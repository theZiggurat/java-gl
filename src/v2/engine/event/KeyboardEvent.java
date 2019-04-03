package v2.engine.event;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardEvent {


    int key;
    int action;
    int mods;

    KeyboardEvent(int key, int action, int mods){
        this.key = key;
        this.action = action;
        this.mods = mods;
    }

    public char toChar(int key){
        char ret;
        switch (key) {
            case GLFW_KEY_0: ret = '0';
            case GLFW_KEY_1: ret = '1';
            case GLFW_KEY_2: ret = '2';
            case GLFW_KEY_3: ret = '3';
            case GLFW_KEY_4: ret = '4';
            case GLFW_KEY_5: ret = '5';
            case GLFW_KEY_6: ret = '6';
            case GLFW_KEY_7: ret = '7';
            case GLFW_KEY_8: ret = '8';
            case GLFW_KEY_A: ret = 'a';
            case GLFW_KEY_B: ret = 'b';
            case GLFW_KEY_C: ret = 'c';
            case GLFW_KEY_D: ret = 'd';
            case GLFW_KEY_E: ret = 'e';
            case GLFW_KEY_F: ret = 'f';
            case GLFW_KEY_G: ret = 'g';
            case GLFW_KEY_H: ret = 'h';
            case GLFW_KEY_I: ret = 'i';
            case GLFW_KEY_J: ret = 'j';
            case GLFW_KEY_K: ret = 'k';
            case GLFW_KEY_L: ret = 'l';
            case GLFW_KEY_M: ret = 'm';
            case GLFW_KEY_N: ret = 'n';
            case GLFW_KEY_O: ret = 'o';
            case GLFW_KEY_P: ret = 'p';
            case GLFW_KEY_Q: ret = 'q';
            case GLFW_KEY_R: ret = 'r';
            case GLFW_KEY_S: ret = 's';
            case GLFW_KEY_T: ret = 't';
            case GLFW_KEY_U: ret = 'u';
            case GLFW_KEY_V: ret = 'v';
            case GLFW_KEY_W: ret = 'w';
            case GLFW_KEY_X: ret = 'x';
            case GLFW_KEY_Y: ret = 'y';
            case GLFW_KEY_Z: ret = 'z';
            case GLFW_KEY_SPACE: ret = ' ';
            default: ret = 0;
        }
        return ret;
    }
}
