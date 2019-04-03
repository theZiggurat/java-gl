package v2.engine.scene.light;

import v2.engine.scene.RenderModule;
import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.RenderType;
import v2.modules.generic.LightOverlayShader;
import v2.modules.generic.UUIDShader;

import java.util.UUID;

public class PointLight extends Light {

    public PointLight(PointLight light){
        this();
        setColor(light.getColor());
        setIntensity(light.getIntensity());
    }

    public PointLight(){
        super();

        RenderModule debugRenderer = new RenderModule(
                LightOverlayShader.instance(), Meshs.cube
        );

        RenderModule UUIDrenderer = new RenderModule(
                UUIDShader.instance(), Meshs.cube
        );
        addModule(RenderType.TYPE_OVERLAY, debugRenderer);
        addModule(RenderType.TYPE_UUID, UUIDrenderer);
        scale(.2f);
    }
}
