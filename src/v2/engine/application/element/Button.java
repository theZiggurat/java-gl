package v2.engine.application.element;

import lombok.Getter;
import org.joml.Vector4i;
import v2.engine.application.event.ActionEvent;
import v2.engine.application.event.mouse.HoverLostEvent;
import v2.engine.application.event.mouse.HoverStartEvent;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.application.layout.Box;
import v2.engine.system.Window;
import v2.engine.utils.Color;

import java.util.ArrayList;
import java.util.Iterator;

import static v2.engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;

public class Button extends Panel {

    @Getter private boolean hover = false;
    private ArrayList<ActionEvent.ActionHandler> listeners;

    public Button() {
        super(new Color(0x0000FF), new Vector4i(5,5,5,5));

        listeners = new ArrayList<>();
        setScissor(false);

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

        if (this.getAbsoluteBox().isWithin(Window.instance().getCursorPosf())){
            setColor(new Color(0xFF));
        } else {
            setColor(new Color(0x606060));
        }

        super.update();
    }

    public void addListener(ActionEvent.ActionHandler e){
        listeners.add(e);
    }
}
