package v2.engine.application.event;

import java.net.URL;

public class ActionEvent extends Event {

    public ActionEvent(){
    }


    @FunctionalInterface
    public interface ActionHandler {
        void handle(ActionEvent e);
    }
}
