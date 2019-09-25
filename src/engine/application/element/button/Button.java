package engine.application.element.button;

import lombok.Getter;
import engine.application.element.panel.Panel;
import engine.application.event.ActionEvent;
import engine.application.event.mouse.MouseClickEvent;
import engine.system.Window;

import java.util.ArrayList;
import java.util.Iterator;

import static engine.application.event.mouse.MouseClickEvent.BUTTON_CLICK;
import static engine.application.event.mouse.MouseClickEvent.BUTTON_RELEASED;

/**
 * Simple clickable button
 */
public class Button extends Panel {

    @Getter
    private boolean hover = false;
    private ArrayList<ActionEvent.ActionHandler> listeners;
    private ButtonSettings buttonSettings;
    private boolean clicked = false;

    public Button(ButtonSettings buttonSettings) {
        super();

        setButtonSettings(buttonSettings);

        listeners = new ArrayList<>();
        setScissor(false);

        onEvent(e -> {

            if (e instanceof MouseClickEvent) {
                MouseClickEvent m = (MouseClickEvent) e;

                // button activated
                if (m.getAction() == BUTTON_CLICK && m.getKey() == 0) {

                    clicked = true;
                    setColor(buttonSettings.getClickColor());
                    ActionEvent action = new ActionEvent();
                    Iterator<ActionEvent.ActionHandler> listens = listeners.iterator();

                    while (!action.isConsumed() && listens.hasNext())
                        listens.next().handle(action);

                } else if (m.getAction() == BUTTON_RELEASED && m.getKey() == 0) {
                    clicked = false;
                    setColor(buttonSettings.getButtonColor());
                }
            }

//            if(e instanceof HoverStartEvent)
//                setColor(new Color(0xFF));
//            else if(e instanceof HoverLostEvent)
//                setColor(new Color(0x606060));

        });


    }

    public void setButtonSettings(ButtonSettings bs) {
        this.buttonSettings = bs;

        setRounding(bs.getRounding());

        if (bs.getButtonTexture().isPresent()) {
            setImage(true);
            setImageBuffer(bs.getButtonTexture().get());
        }

    }

    @Override
    public void update() {

        if (!clicked) {
            if (this.getAbsoluteBox().isWithin(Window.instance().getCursorPosf())
                    && this.isMouseOver() && !Window.instance().isHidden()) {
                setColor(buttonSettings.getHoverColor());
            } else {
                setColor(buttonSettings.getButtonColor());
            }
        }

        super.update();
    }

    public void addListener(ActionEvent.ActionHandler e) {
        listeners.add(e);
    }
}



