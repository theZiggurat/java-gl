package engine.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Color {

    private int r, g, b;

    public Color(int hex){
        r = (hex & 0xFF0000) >> 16;
        g = (hex & 0xFF00) >> 8;
        b = hex & 0xFF;
    }

    public Color(int r, int g, int b) {
        this.r = r > 255 ? 255 : r;
        this.g = g > 255 ? 255 : g;
        this.b = b > 255 ? 255 : b;
    }

    public float redf(){
        return (float)r/255.0f;
    }
    public float greenf(){
        return (float)g/255.0f;
    }
    public float bluef(){
        return (float)b/255.0f;
    }

    public Color brighten() {
        return new Color(this.r + 20, this.g + 20, this.b + 20);
    }

    public Color darken() {
        return new Color(this.r - 20, this.g - 20, this.b - 20);
    }

}
