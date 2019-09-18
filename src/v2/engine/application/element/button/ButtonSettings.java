package v2.engine.application.element.button;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4i;
import v2.engine.glapi.tex.TextureObject;
import v2.engine.utils.Color;

import java.util.Optional;

public class ButtonSettings {

    public final Color BUTTON_COLOR_DEFAULT = new Color(0x333333);
    public final Color HOVER_COLOR_DEFAULT = new Color(0x222222);
    public final Color CLICK_COLOR_DEFAULT = new Color(0x111111);

    @Setter
    @Getter
    private Color
            buttonColor = BUTTON_COLOR_DEFAULT,
            hoverColor = HOVER_COLOR_DEFAULT,
            clickColor = CLICK_COLOR_DEFAULT;

    @Getter @Setter
    private Vector4i rounding = new Vector4i(5);

    @Getter
    private Optional<TextureObject> buttonTexture = Optional.empty();

    public void setButtonTexture(TextureObject tex) {
        this.buttonTexture = Optional.of(tex);
    }


}