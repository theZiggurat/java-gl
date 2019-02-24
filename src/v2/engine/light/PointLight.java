package v2.engine.light;

import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;
import v2.engine.gldata.vbo.Meshs;
import v2.modules.debug.LightOverlayShaderProgram;

public class PointLight extends Light {

    public PointLight(PointLight light){
        this();
        setColor(light.getColor());
        setIntensity(light.getIntensity());
    }

    public PointLight(){
        super();

        RenderModule debugRenderer = new RenderModule(
                LightOverlayShaderProgram.instance(), Meshs.cube
        );
        addModule(ModuleType.RENDER_MODULE_OVERLAY, debugRenderer);
        scale(.2f);
    }
}
