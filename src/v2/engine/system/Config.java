package v2.engine.system;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.Properties;

@Getter @Setter
public class Config {

    private int windowWidth;
    private int windowHeight;
    private String windowName;
    private boolean isvsync;
    private int multisamples;
    private String renderEngine;
    private int numLights;

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

        numLights = Integer.valueOf(properties.getProperty("numLights"));

        windowWidth = Integer.valueOf(properties.getProperty("windowWidth"));
        windowHeight = Integer.valueOf(properties.getProperty("windowHeight"));
        windowName = properties.getProperty("windowName");
        isvsync = properties.getProperty("isvsync").equalsIgnoreCase("true");
        multisamples = Integer.valueOf(properties.getProperty("multisamples"));

        renderEngine = properties.getProperty("renderEngine");


    }
}
