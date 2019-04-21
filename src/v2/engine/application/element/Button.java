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

import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;

public class Button extends Panel {

    @Getter private boolean hover = false;
    private ArrayList<ActionEvent.ActionHandler> listeners;

    public Button() {
        super();

        listeners = new ArrayList<>();

        setRounding(2);

        relativeBox = new Box( 0.1f, 0.1f, 0.8f, 0.8f);

        onEvent(e -> {

            if(e instanceof MouseClickEvent){
                MouseClickEvent m = (MouseClickEvent)e;

                // button activated
                if(m.getAction()==BUTTON_CLICK  && m.getKey() == 0){

                    ActionEvent action = new ActionEvent();
                    Iterator<ActionEvent.ActionHandler> listens = listeners.iterator();

                    while(!action.isConsumed()&&listens.hasNext())
                        listens.next().handle(action);

                }
            }

            if(e instanceof HoverStartEvent)
                setColor(new Color(0xFF));
            else if(e instanceof HoverLostEvent)
                setColor(new Color(0x606060));

        });

    }

    @Override
    public void update(){
    }

    public void addListener(ActionEvent.ActionHandler e){
        listeners.add(e);
    }
}
