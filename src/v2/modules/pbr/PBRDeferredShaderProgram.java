package v2.modules.pbr;

import org.joml.Vector3f;
import v2.engine.gldata.TextureObject;
import v2.engine.light.Light;
import v2.engine.light.LightManager;
import v2.engine.system.Config;
import v2.engine.system.ShaderProgram;

import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.*;

public class  PBRDeferredShaderProgram extends ShaderProgram {

    int numLights;
    Vector3f clearColor = new Vector3f(.4f,.15f,.15f);

    public PBRDeferredShaderProgram(){
        super();

        createComputeShader("res/shaders/pbr_deferred_lighting_cs.glsl");
        link();

        addUniform("camerapos");
        addUniform("numLights");
        addUniform("clearColor");

        addUniform("sun.color");
        addUniform("sun.intensity");
        addUniform("sun.direction");
        addUniform("sun.ambient");

        numLights = Config.instance().getNumLights();

        for(int i = 0; i<numLights; i++){
            addUniform("lights["+i+"].color");
            addUniform("lights["+i+"].intensity");
            addUniform("lights["+i+"].location");
            addUniform("lights["+i+"].activated");
        }


    }

    @Override
    public void compute(TextureObject albedo, TextureObject position, TextureObject normal,
                        TextureObject metal, TextureObject rough, TextureObject ao, TextureObject scene){

        bind();

        setUniform("sun.color", LightManager.getSun().getColor());
        setUniform("sun.intensity", LightManager.getSun().getIntensity());
        setUniform("sun.direction", LightManager.getSun().getWorldRotation());
        setUniform("sun.ambient", LightManager.getSun().getAmbientLight());

        int currlights = LightManager.getSceneLights().size();

        for(int i = 0; i<currlights; i++){
            Light currLight = LightManager.getLight(i);
            if(currLight == null)
            {
                setUniform("lights["+i+"].activated",0);
                setUniform("lights["+i+"].color", new Vector3f(0,0,0));
                setUniform("lights["+i+"].intensity",0f);
                setUniform("lights["+i+"].location",new Vector3f(0,0,0));
            }
            else
            {
                setUniform("lights["+i+"].activated",
                        currLight.isActivated() ? 1:0);
                setUniform("lights["+i+"].color",
                        currLight.getColor());
                setUniform("lights["+i+"].intensity",
                        currLight.getIntensity());
                setUniform("lights["+i+"].location",
                        currLight.getWorldTranslation());
            }

        }

        setUniform("clearColor", clearColor);
        setUniform("numLights", currlights);
        setUniform("camerapos", PBRRenderEngine.instance().getMainCamera().getTranslation());

        bindImage(0, albedo.getId(), GL_READ_ONLY, GL_RGBA16F);
        bindImage(1, position.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(2, normal.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(3, metal.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(4, rough.getId(), GL_READ_ONLY, GL_R16F);
        bindImage(5, ao.getId(), GL_READ_ONLY, GL_RGBA32F);
        bindImage(6, scene.getId(), GL_WRITE_ONLY, GL_RGBA16F);
        compute(16,16);
        unbind();
    }
}
