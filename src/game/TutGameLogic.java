package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import cxv1.engine3D.GameLogic;
import cxv1.engine3D.draw.Renderer;
import cxv1.engine3D.draw.lighting.*;
import cxv1.engine3D.entity.FixedObjectEntity;
import cxv1.engine3D.util.loaders.OBJLoader;
import cxv1.engine3D.util.Window;
import cxv1.engine3D.draw.Camera;
import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.util.State;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/*
    TutorialGameLogic is the implementation of
 */
public class TutGameLogic implements GameLogic {

    private static

    Entity[] entities;
    private HUD Hud;

    private final Renderer renderer;

    Camera camera;
    Vector3f cameraHandle;

    private Vector3f ambientLight;
    private SceneLight sceneLight;
    State state;

    Entity cube, bunny, terrain;

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENS = 0.15f;

    public TutGameLogic(){
        renderer = new Renderer();
        camera = new Camera();
        cameraHandle = new Vector3f(0,0,0);
        state = new State();
    }

    @Override
    public void init(Window window) throws Exception {

        state.init();

        // initialize renderer
        try{renderer.init();}
        catch(Exception e){e.printStackTrace();}

        Entity car = new FixedObjectEntity(OBJLoader.loadMesh("Lamborghini", "Avent.obj"));
        car.setPos(10, 0, 20);
        car.setScale(2);
        Entity bus = new FixedObjectEntity(OBJLoader.loadMesh("Bus", "bus.obj"));
        Entity face = new FixedObjectEntity(OBJLoader.loadMesh("face", "face.obj"));
        bus.setPos(new Vector3f(0,0,10));
        entities = new Entity[]{car, bus, face};

        // initialized light
        sceneLight = generateLights();

        Hud = new HUD("Hello friendo how are you doing today you littlebitch son");

        // lock cursor inside window and hide
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    float t = 0;


    @Override
    public void update(double interval, MouseInput mouseInput){

        state.update(interval);
        sceneLight.getSun().updateLight(state.getSunTimer());

        t++;

        //light.setPosition(new Vector3f((float) (10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005))));
        //cube.setPos((float)(10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005)));

        // HANDLE PLAYER MOVEMENT
        camera.movePosition(cameraHandle.x * CAMERA_POS_STEP,
                            cameraHandle.y * CAMERA_POS_STEP,
                            cameraHandle.z * CAMERA_POS_STEP);

        // HANDLE PLAYER ROTATION
         Vector2f rotation = mouseInput.getDisplacement();
         camera.moveRotation(rotation.x * MOUSE_SENS,
                             rotation.y * MOUSE_SENS,
                             0);
    }

    @Override
    public void input(Window window, MouseInput mouseInput){
        cameraHandle.set(0,0,0);
        if(window.isKeyPressed(GLFW_KEY_W)){
            cameraHandle.z = -1;
        } else if(window.isKeyPressed(GLFW_KEY_S)) {
            cameraHandle.z = 1;
        }if(window.isKeyPressed(GLFW_KEY_A)){
            cameraHandle.x = -1;
        } else if(window.isKeyPressed(GLFW_KEY_D)) {
            cameraHandle.x = 1;
        }if(window.isKeyPressed(GLFW_KEY_UP)){
            cameraHandle.y = -1;
        } else if(window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraHandle.y = 1;
        } if(window.isKeyPressed(GLFW_KEY_ENTER)){
            window.setLine(!window.isLine());
        }
    }

    @Override
    public void render(Window window,  double FPS){
        Hud.updateSize(window);
        //Hud.setStatusText(String.valueOf(FPS));
        // send entity meshes to renderer
        renderer.render(window, camera, entities, sceneLight, Hud);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        //for(Entity e: entities){e.cleanup();}
    }

    public State getState(){
        return state;
    }

    public SceneLight generateLights(){
        SceneLight sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(.5f, 0f, .5f));

        PointLight masterLight = new PointLight(new Vector3f(1,1,1), new Vector3f(1,1,1),
                1.0f, new PointLight.Attenuation(0,0,1));

        sceneLight.setPointLights(new PointLight[]{
                new PointLight(masterLight, new Vector3f(0,0.5f,0)),
                new PointLight(masterLight, new Vector3f(0,3,0)),
                new PointLight(masterLight, new Vector3f(0,10,5)),
                new PointLight(masterLight, new Vector3f(20,0,0)),
                new PointLight(masterLight, new Vector3f(20,0,0)),
        });

        sceneLight.setSpotLights(new SpotLight[]{});

        Sun sun = new Sun(90);
        sun.setIntensity(8);

        sceneLight.setSun(new Sun(90));
        return sceneLight;
    }



}
