package engine.application.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4i;
import engine.system.Window;

@Getter @Setter @AllArgsConstructor
public class Box {

    public float x, y, width, height;

    public Box(Box box){
        this(box.x, box.y, box.width, box.height);
    }

    /**
     * Return box
     * @param outer
     * @return
     */
    public Box relativeTo(Box outer){
        return new Box(
            outer.x + outer.width * this.x,
            outer.y + outer.height * this.y,
            outer.width * this.width,
            outer.height * this.height
        );
    }

    public Vector2f within(Vector2f vec){
        return new Vector2f(
            (vec.x - this.x)/this.width,
            (vec.y - this.y)/this.height
        );
    }

    public boolean isWithin(Vector2f vec){
        return vec.x >= this.x && vec.y >= this.y
                && vec.x <= this.x+this.width && vec.y <= this.y+this.height;
    }

    public Vector2i resolution(){
        return new Vector2i(
            (int) (width * Window.instance().getWidth()),
            (int) (height * Window.instance().getHeight())
        );
    }

    public Vector2i sizeWithin(Box outer){
        return this.relativeTo(outer).resolution();
    }

    public void set(Box box){
        this.x = box.x;
        this.y = box.y;
        this.width = box.width;
        this.height = box.height;
    }

    public void translateX(int pixels){
        this.x += (float)pixels / (float)Window.instance().getWidth();
    }

    public void translateY(int pixels){
        this.y += (float)pixels / (float)Window.instance().getHeight();
    }

    public void translateX(float x){
        this.x += x;
    }

    public void translateY(float y){
        this.y += y;
    }

    public void translate(int xpixels, int ypixels){
        translateX(xpixels);
        translateY(ypixels);
    }

    public void translate(Vector2i translation){
        translateX(translation.x);
        translateY(translation.y);
    }

    public void translate(float x, float y){
        translateX(x);
        translateY(y);
    }

    public void translate(Vector2f translation){
        translateX(translation.x);
        translateY(translation.y);
    }

    public Vector4i pixelDimensions(Vector2i dimensions) {
        return new Vector4i(
                (int)(dimensions.x * this.x),
                (int)(dimensions.y * this.y),
                (int)(dimensions.x * this.width),
                (int)(dimensions.y * this.height)
        );
    }


    @Override
    public boolean equals(Object object){
        if(this == object)
            return true;
        if(object instanceof Box){
            return x == ((Box) object).x
                    && y == ((Box) object).y
                    && width == ((Box) object).width
                    && height == ((Box) object).height;
        }
        return false;
    }

    @Override
    public String toString(){
        return "{x: " + x + ", y: " + y + ", width: " + width + ", height: " + height+ "}";
    }

}
