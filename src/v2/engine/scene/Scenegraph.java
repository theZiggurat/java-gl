package v2.engine.scene;

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
