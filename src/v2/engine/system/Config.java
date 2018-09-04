package v2.engine.system;

import lombok.Getter;

@Getter
public class Config {

    private int width, height;
    private String windowName;
    private boolean vsync;

    private static Config instance;

    public static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    public Config(){
        width = 1280;
        height = 720;
        windowName = "LWJGLTest";
        vsync = false;
    }
}
