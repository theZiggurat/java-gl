package engine.application.event;

public class ActionEvent extends Event {

    public ActionEvent(){

    }


    @FunctionalInterface
    public interface ActionHandler {
        void handle(ActionEvent e);
    }
}
