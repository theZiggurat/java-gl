package v2.engine.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Color {

    private int r, g, b;

    public Color(int hex){
        r = (hex & 0xFF0000) >> 16;
        g = (hex & 0xFF00) >> 8;
        b = hex & 0xFF;
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

}
