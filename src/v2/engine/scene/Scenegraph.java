package v2.engine.scene;

import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;
import v2.modules.pbr.PBRModel;

import java.util.List;

public class Scenegraph extends Node {


    private static Scenegraph instance;

    public static Scenegraph getInstance(){
        if (instance == null){
            instance = new Scenegraph();
        }
        return instance;
    }

    @Override
    public void update(){
        super.update();
    }

    /**
     * Renders entire scenegraph. Called from renderEngine
     */
    @Override
    public void render(){
        super.render();
    }

    @Override
    public void cleanup(){
        super.cleanup();
    }


}
