package v2.engine.gui.element;

import v2.engine.gui.Box;
import v2.engine.gui.layout.VerticalSplitLayout;

public class DynamicWindow extends Element {

    private DynamicPanel dynamicPanel;
    private Panel topbar;

    public DynamicWindow(DynamicPanel dynamicPanel){
        super();

        this.dynamicPanel = dynamicPanel;
        this.topbar = new Panel();

        absoluteBox = new Box(0.1f, 0.1f, 0.8f, 0.8f);

        VerticalSplitLayout vsp = new VerticalSplitLayout(this);
        vsp.setCutoffY(20);
        setLayout(vsp);

        addChildren(this.topbar, this.dynamicPanel);
        layoutChildren();
    }
}
