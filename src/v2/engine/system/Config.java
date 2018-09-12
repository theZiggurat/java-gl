package v2.engine.system;

import lombok.Getter;

@Getter
public class Config {

    private int width, height;
    private String windowName;
    private boolean vsync;

    private int multisamples;

    private static Config instance;

    public static Config instance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    public Config(){
        multisamples = 4;
        width = 1280;
        height = 720;
        windowName = "LWJGLTest";
        vsync = false;
    }
}
