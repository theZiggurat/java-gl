package v2.engine.application.element.viewport;

import v2.engine.application.element.Element;
import v2.engine.application.element.panel.Panel;
import v2.engine.application.layout.Inset;
import v2.engine.application.layout.VerticalLayout;
import v2.engine.utils.Color;

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
