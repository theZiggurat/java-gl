package v2.engine.application.element;

import lombok.Getter;
import v2.engine.application.event.ActionEvent;
import v2.engine.application.event.mouse.HoverLostEvent;
import v2.engine.application.event.mouse.HoverStartEvent;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.layout.Box;
import v2.engine.utils.Color;

import java.util.ArrayList;
import java.util.Iterator;

public class Button extends Panel {

    @Getter private boolean hover = false;
    private ArrayList<ActionEvent.ActionHandler> listeners;

    public Button() {
        super();

        listeners = new ArrayList<>();

        setRounding(12);
        setColor(new Color(0x606060));
        relativeBox = new Box( 0.1f, 0.1f, 0.8f, 0.8f);

        onEvent(e -> {

            if(e instanceof MouseClickEvent){
                MouseClickEvent m = (MouseClickEvent)e;

                // button activated
                if(m.getAction()==MouseClickEvent.BUTTON_CLICK  && m.getKey() == 0){

                    ActionEvent action = new ActionEvent();
                    Iterator<ActionEvent.ActionHandler> listens = listeners.iterator();

                    while(!action.isConsumed()&&listens.hasNext())
                        listens.next().handle(action);

                }
            }

            if(e instanceof HoverStartEvent)
                hover = true;
            else if(e instanceof HoverLostEvent)
                hover = false;

        });

    }

    public void addListener(ActionEvent.ActionHandler e){
        listeners.add(e);
    }
}
