package engine.system;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector4i;
import engine.application.element.*;
import engine.application.element.button.Button;
import engine.application.element.button.ButtonSettings;
import engine.application.element.viewport.SceneViewport;
import engine.application.element.viewport.VerticalViewport;
import engine.application.event.InputManager;
import engine.application.layout.Box;
import engine.scene.SceneContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.opengl.GL11.*;

public class AppContext {

    @Setter static boolean UI_DEBUG_MODE = false;

    @Getter private RootElement root;
    @Setter @Getter private Element renderElement;

    @Getter private SceneContext sceneContext;
    @Getter private ElementManager elementManager;

    private static AppContext instance;
    public static AppContext instance(){
        if(instance == null)
            instance = new AppContext();
        return instance;
    }

    public void init() {

        root = new RootElement();
        renderElement = root;
        elementManager = ElementManager.instance();

        elementManager.init(this);



        if (!UI_DEBUG_MODE) {

            // initialize pipeline from config
            try {

                this.sceneContext = Core.class.getClassLoader().loadClass(
                        Config.instance().getEngineInstance()).asSubclass(SceneContext.class).newInstance();
                sceneContext.loadRenderer();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Render engine class does not exist: " + Config.instance().getRenderEngine());
                System.exit(-1);
            } catch (InstantiationException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.err.println("Render engine does not take SceneContext in its constructor: " + Config.instance().getRenderEngine());
                System.exit(-1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            // initialize engine implementation

            sceneContext.init();
        }
        // replace with application load protocol
        __init__ui();
    }



    // TODO: create application loader
    // TEMPORARY FOR TESTING
    private void __init__ui(){

        Optional<SceneViewport> sceneViewport = Optional.empty();
        if (!UI_DEBUG_MODE) {
            sceneViewport = Optional.of(new SceneViewport(sceneContext));
        }

        ButtonSettings bs = new ButtonSettings();
//        bs.setButtonColor(new Color(0x6060FF));
//        bs.setClickColor(new Color(0x60FFFF));
//        bs.setHoverColor(new Color(0xFFFFFF));
        bs.setRounding(new Vector4i(5));

        Button button = new Button(bs), up = new Button(bs), down = new Button(bs), ssrToggle = new Button(bs);
        button.addListener(e -> Config.instance().setSsao(!Config.instance().isSsao()));
        button.addListener(e -> System.out.println("Pressed SSAO button"));
        up.addListener(e -> Config.instance().setSsaoPower(Config.instance().getSsaoPower()+1));
        up.addListener(e -> System.out.println("Pressed SSAO UP button"));
        down.addListener(e -> Config.instance().setSsaoPower(Config.instance().getSsaoPower()-1));
        down.addListener(e -> System.out.println("Pressed SSAO DOWN button"));
        ssrToggle.addListener(e -> Config.instance().setSsr(!Config.instance().isSsr()));
        ssrToggle.addListener(e -> System.out.println("Pressed SSR button"));

//        VerticalViewport pv = new VerticalViewport(40, 100, 100);
//        VerticalViewport p2 = new VerticalViewport(40, 100, 100);
//        VerticalViewport p3 = new VerticalViewport(40, 100, 100);
        VerticalViewport viewport = new VerticalViewport(40, 100, 100, button, up, down, ssrToggle);
        root.addChildren(viewport);//), pv, p2, p3);
        viewport.setBox(new Box(0.25f, 0.1f, 0.2f, 0.5f));
//        pv.setBox(new Box(0.8f, 0.3f, 0.15f, 0.5f));
//        p2.setBox(new Box(0.5f, 0.3f, 0.15f, 0.5f));
//        p3.setBox(new Box(0.3f, 0.3f, 0.15f, 0.5f));


        if(sceneViewport.isPresent()){
            root.addChildren(sceneViewport.get());
            sceneViewport.get().setBox(new Box(0.3f, 0.1f, 0.65f, 0.8f));
            ElementManager.instance().setTop(sceneViewport.get());
        }

        root.forceLayout();
    }

    public void update(){
        if(Window.instance().isResized()){
            root.forceLayout();
            Window.instance().setResized(false);
        }

        if(!UI_DEBUG_MODE) sceneContext.update();
        elementManager.update();
        root.update();

        if(InputManager.instance().isKeyPressed(GLFW_KEY_F1))
            Config.instance().loadFromConfigFile();

    }

    public void draw(){
        Window.instance().setBlending(false);
        if(!UI_DEBUG_MODE) sceneContext.render();
        Window.instance().setBlending(true);
        glDisable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT);
        renderElement.render();
        glEnable(GL_DEPTH_TEST);
    }

    public void resetRenderElement(){
        setRenderElement(root);
    }







}
