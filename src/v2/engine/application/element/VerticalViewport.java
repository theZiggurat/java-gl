package v2.engine.application.element;

import v2.engine.application.layout.Inset;
import v2.engine.application.layout.VerticalLayout;

public class VerticalViewport extends PlainViewport {

    Element listPanel;

    public VerticalViewport(int elementHeight, int minimumX, int minimumY, Element... children) {
        super();

        this.listPanel = getChildren().get(1);

        listPanel.setLayout(new VerticalLayout(this.listPanel, elementHeight));
        listPanel.addChildren(children);
        listPanel.setChildrenInset(new Inset(5));
        this.minWidth = minimumX;
        this.minHeight = minimumY;
    }


}
