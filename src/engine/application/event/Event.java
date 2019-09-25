package engine.application.event;

import lombok.Getter;

public class Event {

    @Getter private boolean consumed = false;

    public void consume(){ consumed = true; }

    @FunctionalInterface
    public interface EventHandler {
        void handle(Event e);
    }
}
