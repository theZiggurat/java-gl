package v2.engine.system;

import com.sun.xml.internal.ws.api.pipe.Engine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.Properties;

@Getter @Setter
public class Config {

    private int windowWidth;
    private int windowHeight;
    private String windowName;
    private boolean vsync;
    private int multisamples;
    private String renderEngine, engineInstance;
    private int numLights;
    private float zfar;

    private int shadowBufferWidth;
    private int shadowBufferHeight;

    private boolean ssao;
    private float ssaoRadius;
    private int ssaoSamples;

    private boolean debugLayer;

    private boolean isWireframe;
    private Vector3f wireframeColor;

    Properties properties;


    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static Config instance;
    public static Config instance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }



    public Config(){
        properties = new Properties();
        init();
    }

    public void init(){

        try{
            InputStream str = Config.class.getClassLoader().getResourceAsStream("res/config.properties");
            properties.load(str);
            str.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        // max lights
        numLights = Integer.valueOf(properties.getProperty("numLights"));

        // window settings
        windowWidth = Integer.valueOf(properties.getProperty("windowWidth"));
        windowHeight = Integer.valueOf(properties.getProperty("windowHeight"));
        windowName = properties.getProperty("windowName");
        vsync = properties.getProperty("isvsync").equalsIgnoreCase("true");
        multisamples = Integer.valueOf(properties.getProperty("multisamples"));

        // engine instance
        renderEngine = properties.getProperty("renderEngine");
        engineInstance = properties.getProperty("engineInstance");

        shadowBufferWidth = Integer.valueOf(properties.getProperty("shadow_buffer_x"));
        shadowBufferHeight = Integer.valueOf(properties.getProperty("shadow_buffer_y"));

        // SSAO settings
        ssao = Boolean.valueOf(properties.getProperty("ssao"));
        ssaoRadius = Float.valueOf(properties.getProperty("ssao_radius"));
        ssaoSamples = Integer.valueOf(properties.getProperty("ssao_samples"));

        // generic layer
        debugLayer = Boolean.valueOf(properties.getProperty("debug_layer"));

        isWireframe = Boolean.valueOf(properties.getProperty("isWireframe"));
        wireframeColor = new Vector3f(0.2f, 0.8f, 0.2f);

    }
}
