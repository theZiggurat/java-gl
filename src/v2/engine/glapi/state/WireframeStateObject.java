package v2.engine.glapi.state;

import static org.lwjgl.opengl.GL11.*;

public class WireframeStateObject {

    public void enable(){
        glPolygonMode( GL_FRONT_AND_BACK, GL_LINE);


    }

    public void disable(){
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
    }
}
