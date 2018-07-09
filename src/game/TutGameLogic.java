package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import cxv1.engine3D.GameLogic;
import cxv1.engine3D.draw.sceneRenderer;
import cxv1.engine3D.draw.lighting.*;
import cxv1.engine3D.entity.*;
import cxv1.engine3D.enviorment.Scene;
import cxv1.engine3D.util.loaders.OBJLoader;
import cxv1.engine3D.util.Window;
import cxv1.engine3D.draw.Camera;
import cxv1.engine3D.input.MouseInput;
import cxv1.engine3D.util.State;
import org.joml.Vector2f;
import org.joml.Vector3f;

/*
    TutorialGameLogic is the implementation of
 */
public class TutGameLogic implements GameLogic {

    private static

    Entity[] entities;

    private final sceneRenderer sceneRenderer;

    Player player;
    Vector3f cameraHandle;

    private Vector3f ambientLight;
    private SceneLight sceneLight;
    State state;

    Scene scene;

    Entity car, bunny, terrain, face;

    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENS = 0.15f;

    public TutGameLogic(){
        sceneRenderer = new sceneRenderer();
        player = new Player();
        cameraHandle = new Vector3f(0,0,0);
        state = new State();
    }

    @Override
    public void init(Window window) throws Exception {

        state.init();

        // initialize sceneRenderer
        try{
            sceneRenderer.init();}
        catch(Exception e){e.printStackTrace();}

        Terrain terrain = new Terrain(3, 1000, -.01f, .01f,
                "height.png", "grass2.png", "stone.png",1);

        SkyBox skyBox = new SkyBox("skybox.obj", "skybox.png");
        skyBox.setScale(100f);

        car = new StaticEntity(OBJLoader.loadMesh("Lamborghini", "Avent.obj"));
        car.setPos(10, -10, 20);
        car.setScale(2);
        //Entity bus = new StaticEntity(OBJLoader.loadMesh("Bus", "bus.obj"));
        face = new StaticEntity(OBJLoader.loadMesh("bus", "bus.obj"));
        //bus.setPos(new Vector3f(0,0,10));
        entities = new Entity[]{face, car};

        // initialized light
        sceneLight = generateLights();

        scene = new Scene();
        //scene.setEntities(entities);
        scene.setSceneLight(sceneLight);
        scene.setSkyBox(skyBox);
        scene.setEntities(entities);
        scene.setTerrain(terrain);

        // lock cursor inside window and hide
        glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

    }

    float t = 0;
    float zRot = 0;


    @Override
    public void update(double interval, Window window, MouseInput mouseInput) {

        state.update(interval);
        sceneLight.getSun().updateLight(state.getSunTimer());

        if(t == 144){
            t = 0;
            player.debug();
        }
        t++;

        //light.setPosition(new Vector3f((float) (10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005))));
        //face.setPos((float)(10*-Math.sin(t*.005)), 10,(float) (10*-Math.cos(t*.005)));

        // HANDLE PLAYER MOVEMENT
        player.update(scene.getTerrain(), state, window);


        // HANDLE PLAYER ROTATION
         Vector2f rotation = mouseInput.getDisplacement();
         player.getCamera().moveRotation(rotation.x * MOUSE_SENS,
                             rotation.y * MOUSE_SENS,
                             0);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
         if(window.isKeyPressed(GLFW_KEY_ENTER)){
            window.setLine(!window.isLine());
        }
    }

    @Override
    public void render(Window window,  double FPS)  {

        //Hud.setStatusText(String.valueOf(FPS));
        // send entity meshes to sceneRenderer
        sceneRenderer.render(window, player.getCamera(), scene);
    }

    @Override
    public void cleanup()   {
        sceneRenderer.cleanup();
        //for(Entity e: entities){e.cleanup();}
    }

    public State getState(){
        return state;
    }

    public SceneLight generateLights()  {
        SceneLight sceneLight = new SceneLight();
        sceneLight.setAmbientLight(new Vector3f(1f, 1f, 1f));

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

        Sun sun = new Sun(-90);
        sun.setIntensity(2);

        sceneLight.setSun(sun);
        return sceneLight;
    }




}
