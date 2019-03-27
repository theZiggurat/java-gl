package v2.engine.utils;

import org.joml.Vector2d;
import org.joml.Vector2f;
import v2.engine.gui.Box;
import v2.engine.system.Window;

public class Coordinates {

    /**
     * Converts glfw pixel coordinates to opengl NDC coordinates.
     * Note: glfw y axis starts at top whereas opengl NDC coordinates
     * at the bottom
     * @param screenCoords screen pixel coordinates
     * @return normalized device coordinates
     */
    public static Vector2f screenToNDC(Vector2f screenCoords){
        float x = screenCoords.x / Window.instance().getWidth() * 2 -1;
        float y = 2-screenCoords.y / Window.instance().getHeight() * 2 -1;
        return new Vector2f(x,y);
    }

    /**
     * Converts glfw pixel coordinates to opengl NDC coordinates.
     * Note: glfw y axis starts at top whereas opengl NDC coordinates
     * at the bottom
     * @param screenCoords screen pixel coordinates
     * @return normalized device coordinates
     */
    public static Vector2f screenToNDC(Vector2d screenCoords){
        float x = (float)screenCoords.x / Window.instance().getWidth() * 2 - 1;
        float y = 2-(float)screenCoords.y / Window.instance().getHeight() * 2 - 1;
        return new Vector2f(x,y);
    }

    /**
     *
     * @param box representing area in opengl NDC coordinates
     * @param screenCoords screen pixel coordinates
     * @return whether or not the coord is within the box
     */
    public static boolean insideBox(Box box, Vector2d screenCoords){
        Vector2f ndc = screenToNDC(screenCoords);
        float left = box.getX();
        float bottom = box.getY();
        float right = left + box.getWidth();
        float top = bottom + box.getHeight();

        if(ndc.x >= left && ndc.x <= right &&
                ndc.y >= bottom && ndc.y <= top)
            return true;
        return false;
    }
}
