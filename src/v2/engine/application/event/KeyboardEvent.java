package v2.engine.application.event;

import lombok.Getter;

import static org.lwjgl.glfw.GLFW.*;

@Getter
public class KeyboardEvent extends Event{

    public static final int KEY_HELD = 0x1;
    public static final int KEY_PRESSED = 0x2;
    public static final int KEY_RELEASED = 0x4;

    private int key;
    private int action;
    private int mods;

    public KeyboardEvent(int key, int action, int mods){
        this.key = key;
        this.action = action;
        this.mods = mods;
    }

    /**
     * Returns printable character the event is containing. If character
     * is not printable, returns 0
     * @return char
     */
    public char toChar(){
        char ret;
        boolean q = (mods & GLFW_MOD_SHIFT) == 0;
        switch (key) {
            case GLFW_KEY_SPACE: ret = ' ';
            case GLFW_KEY_APOSTROPHE: ret = q?'\'':'"';
            case GLFW_KEY_COMMA: ret = q?',':'<';
            case GLFW_KEY_MINUS: ret = q?'-':'_';
            case GLFW_KEY_PERIOD: ret = q?'.':'>';
            case GLFW_KEY_SLASH: ret = q?'/':'?';
            case GLFW_KEY_SEMICOLON: ret = q?';':':';
            case GLFW_KEY_EQUAL: ret = q?'=':'+';
            case GLFW_KEY_LEFT_BRACKET: ret = q?'[':'{';
            case GLFW_KEY_BACKSLASH: ret = q?'\\':'|';
            case GLFW_KEY_RIGHT_BRACKET: ret = q?']':'}';
            case GLFW_KEY_GRAVE_ACCENT: ret = q?'`':'~';
            case GLFW_KEY_0: ret = q?'0':')';
            case GLFW_KEY_1: ret = q?'1':'!';
            case GLFW_KEY_2: ret = q?'2':'@';
            case GLFW_KEY_3: ret = q?'3':'#';
            case GLFW_KEY_4: ret = q?'4':'$';
            case GLFW_KEY_5: ret = q?'5':'%';
            case GLFW_KEY_6: ret = q?'6':'^';
            case GLFW_KEY_7: ret = q?'7':'&';
            case GLFW_KEY_8: ret = q?'8':'*';
            case GLFW_KEY_9: ret = q?'9':'(';
            case GLFW_KEY_A: ret = q?'a':'A';
            case GLFW_KEY_B: ret = q?'b':'B';
            case GLFW_KEY_C: ret = q?'c':'C';
            case GLFW_KEY_D: ret = q?'d':'D';
            case GLFW_KEY_E: ret = q?'e':'E';
            case GLFW_KEY_F: ret = q?'f':'F';
            case GLFW_KEY_G: ret = q?'g':'G';
            case GLFW_KEY_H: ret = q?'h':'H';
            case GLFW_KEY_I: ret = q?'i':'I';
            case GLFW_KEY_J: ret = q?'j':'J';
            case GLFW_KEY_K: ret = q?'k':'K';
            case GLFW_KEY_L: ret = q?'l':'L';
            case GLFW_KEY_M: ret = q?'m':'M';
            case GLFW_KEY_N: ret = q?'n':'N';
            case GLFW_KEY_O: ret = q?'o':'O';
            case GLFW_KEY_P: ret = q?'p':'P';
            case GLFW_KEY_Q: ret = q?'q':'Q';
            case GLFW_KEY_R: ret = q?'r':'R';
            case GLFW_KEY_S: ret = q?'s':'S';
            case GLFW_KEY_T: ret = q?'t':'T';
            case GLFW_KEY_U: ret = q?'u':'U';
            case GLFW_KEY_V: ret = q?'v':'V';
            case GLFW_KEY_W: ret = q?'w':'W';
            case GLFW_KEY_X: ret = q?'x':'X';
            case GLFW_KEY_Y: ret = q?'y':'Y';
            case GLFW_KEY_Z: ret = q?'z':'Z';
            default: ret = 0;
        }
        return ret;
    }
}
