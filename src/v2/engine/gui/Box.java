package v2.engine.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.vbo.Mesh2D;
import v2.engine.system.Window;

@Getter @Setter @AllArgsConstructor
public class Box {

    float x, y, width, height;

    public void translateX(int pixels){
        this.x += (float)pixels / (float)Window.instance().getWidth();
    }

    public void translateY(int pixels){
        this.y += (float)pixels / (float)Window.instance().getHeight();
    }

    public void translate(int xpixels, int ypixels){
        translateX(xpixels);
        translateY(ypixels);
    }

    public Box within(Box outer){
        return new Box(
            outer.x + outer.width * this.x,
            outer.y + outer.height * this.y,
            outer.width * this.width,
            outer.height * this.height
        );
    }

}
