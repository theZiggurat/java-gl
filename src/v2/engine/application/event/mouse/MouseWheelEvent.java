package v2.engine.application.event.mouse;

import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import v2.engine.application.event.Event;
import v2.engine.system.Window;

public class MouseWheelEvent extends Event {

    Vector2i pixelScreenPos;
    Vector2f screenPos;

    @Getter private float wheelDelta;

    public MouseWheelEvent(double delta, Vector2f screenPos){
        this.screenPos = screenPos;
        this.pixelScreenPos = new Vector2i(
                (int)(screenPos.x * Window.instance().getWidth()),
                (int)(screenPos.y * Window.instance().getHeight()));
        this.wheelDelta = (float)delta;
    }
}
