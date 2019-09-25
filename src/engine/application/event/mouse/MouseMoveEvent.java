package engine.application.event.mouse;

import lombok.Getter;
import org.joml.Vector2f;
import engine.application.event.Event;

public class MouseMoveEvent extends Event {

    @Getter Vector2f screenPos;
    @Getter Vector2f screenDelta;

    public MouseMoveEvent(Vector2f screenPos, Vector2f screenDelta){
        this.screenPos = screenPos;
        this.screenDelta = screenDelta;
    }

    @Override
    public String toString(){
        return "MouseEvent: " + screenPos.toString();
    }
}
