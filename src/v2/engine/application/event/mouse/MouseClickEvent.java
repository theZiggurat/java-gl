package v2.engine.application.event.mouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import v2.engine.application.event.Event;
import v2.engine.system.Window;

@Getter @AllArgsConstructor
public class MouseClickEvent extends Event {

    public static final int BUTTON_CLICK = 0x1;
    public static final int BUTTON_HELD = 0x2;
    public static final int BUTTON_RELEASED = 0x4;
    public static final int BUTTON_DRAGGED = 0x8;

    private int key;
    private int action;
    private int mods;

    private Vector2f screenPos;
    private Vector2f screenDelta;

    public Vector2i getPixelScreenPos(){
        return new Vector2i(
            (int)(screenPos.x * Window.instance().getWidth()),
            (int)(screenPos.y * Window.instance().getHeight())
        );
    }

    public Vector2i getPixelScreenDelta(){
        return new Vector2i(
            (int)(screenDelta.x * Window.instance().getWidth()),
            (int)(screenDelta.y * Window.instance().getHeight())
        );
    }

}
