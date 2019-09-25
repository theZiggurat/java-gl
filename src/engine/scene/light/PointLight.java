package engine.scene.light;

import engine.scene.node.RenderModule;
import engine.glapi.vbo.Meshs;
import engine.scene.node.RenderType;
import modules.generic.LightOverlayShader;
import modules.generic.UUIDShader;

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
        transform.scale(.2f);
    }
}
