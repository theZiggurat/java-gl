package v2.engine.application.element.viewport;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4i;
import v2.engine.utils.Color;

public class ViewportSettings {

    public final Color TOPBAR_COLOR_DEFAULT = new Color(0x1c2a5e);
    public final Color BORDER_COLOR_DEFAULT = new Color(0x1b2136);
    public final Color PANEL_COLOR_DEFAULT = new Color(0x151a2b);

    @Getter @Setter
    private Vector4i rounding = new Vector4i(5);

    @Setter @Getter
    private Color   topBarColor = TOPBAR_COLOR_DEFAULT,
                    borderColor = BORDER_COLOR_DEFAULT,
                    panelColor = PANEL_COLOR_DEFAULT;

    @Setter @Getter
    private int topBarSize = 25;

    @Setter @Getter
    private int borderSize = 5;




}
