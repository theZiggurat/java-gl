package v2.engine.scene;

import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;

public class Scenegraph extends Node {

    @Getter
    private Node opaqueObjects;
    @Getter
    private Node transparentObjects;

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
//        opaqueObjects.update();
//        transparentObjects.update();
    }

    @Override
    public void render(){
        super.render();
//        opaqueObjects.render();
//        transparentObjects.render();
    }

    @Override
    public void cleanup(){
        super.cleanup();
//        opaqueObjects.cleanup();
//        transparentObjects.cleanup();
    }


}
