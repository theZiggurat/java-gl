package engine.application.element.viewport;

import engine.application.element.Element;
import engine.application.element.panel.Panel;
import engine.application.event.ButtonEvent;
import engine.application.event.mouse.MouseClickEvent;
import engine.application.layout.Inset;
import engine.application.layout.VerticalLayout;
import engine.utils.Color;

public class VerticalViewport extends Viewport {

    public VerticalViewport(int elementHeight, int minimumX, int minimumY, Element... children){
        this(elementHeight, minimumX, minimumY, new ViewportSettings(), children);
    }

    public VerticalViewport(int elementHeight, int minimumX, int minimumY, ViewportSettings vs, Element... children) {
        super(vs);

        Panel listPanel = new Panel();
        setMainPanel(listPanel);

        listPanel.setLayout(new VerticalLayout(listPanel, elementHeight));
        listPanel.addChildren(children);
        listPanel.setChildrenInset(new Inset(5));

        listPanel.onEvent(e -> {
            if(e instanceof MouseClickEvent)
            {
                MouseClickEvent m = (MouseClickEvent) e;
                for(Element child: listPanel.getChildren()){
                    if(child.getAbsoluteBox().isWithin(m.getScreenPos())){
                        child.handle(m);
                        break;
                    }

                }
            }

        });

        this.minWidth = minimumX;
        this.minHeight = minimumY;
    }

//    @Override
//    public void render() {
//        super.render();
//
//        listPanel.render();
//    }


}
