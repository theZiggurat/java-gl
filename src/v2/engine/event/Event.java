package v2.engine.event;

public class Event {





    @FunctionalInterface
    public interface MouseEventHandler {
        public void handle(MouseEvent e);
    }

}
