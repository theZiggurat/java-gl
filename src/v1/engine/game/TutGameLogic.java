package v1.engine.game;

import static org.lwjgl.glfw.GLFW.*;

import v1.engine.GameLogic;
import v1.engine.draw.sceneRenderer;
import v1.engine.entity.CameraEntity;
import v1.engine.entity.FPSControllerEntity;
import v1.engine.entity.PanControllerEntity;
import v1.engine.enviorment.Scene;
import v1.engine.util.Window;
import v2.engine.system.Input;
import v1.engine.util.State;
import v1.engine.util.loaders.SceneLoader;
import org.joml.Vector3f;

/*
    TutorialGameLogic is the implementation of
 */
public class TutGameLogic implements GameLogic {


    private final sceneRenderer sceneRenderer;

    private FPSControllerEntity fps;
    private PanControllerEntity pan;

    private CameraEntity player;

    Vector3f cameraHandle;

    Scene scene;

    public TutGameLogic(){
        sceneRenderer = new sceneRenderer();
        fps = new FPSControllerEntity();
        pan = new PanControllerEntity();
        cameraHandle = new Vector3f(0,0,0);

        player = fps;
    }

    @Override
    public void init() throws Exception {

        // initialize sceneRenderer
        try{
            sceneRenderer.init();}
        catch(Exception e){
            e.printStackTrace();
        }

        scene = SceneLoader.loadScene("LWJGL_DEV_LEVEL.cvx");
        fps.setScene(scene);

        // lock cursor inside window and hide
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

    }

    float t = 0;
    float zRot = 0;

    @Override
    public void update(double interval, Window window, Input mouseInput) {

        State.getInstance().update(interval);
        scene.getSceneLight().getSun().updateLight(State.getInstance().getSunTimer()*20);

        if(t == 144){
            t = 0;
            player.debug();
        }
        t++;

        //light.setPosition(new Vector3f((float) (10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005))));
        //face.setPos((float)(10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005)));

        // HANDLE PLAYER MOVEMENT
        player.handle(window);

    }

    @Override
    public void input(Window window) {
         if(window.isKeyPressed(GLFW_KEY_ENTER)){
            window.setLine(!window.isLine());
        }
         if(window.isKeyPressed(GLFW_KEY_V)){
             if(player.equals(fps)){
                 player = pan;
             } else {
                 player = fps;
             }
         }
    }

    @Override
    public void render(Window window,  double FPS)  {

        //Hud.setStatusText(String.valueOf(FPS));
        // send entity meshes to sceneRenderer
        sceneRenderer.render(window, player, scene);
    }

    @Override
    public void cleanup()   {
        sceneRenderer.cleanup();
        //for(Entity e: entities){e.cleanup();}
    }

}
