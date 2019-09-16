package v2.engine.application;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.element.*;
import v2.engine.application.event.InputManager;
import v2.engine.application.layout.Box;
import v2.engine.application.layout.VerticalLayout;
import v2.engine.application.layout.Inset;
import v2.engine.system.Config;
import v2.engine.scene.SceneContext;
import v2.engine.system.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.opengl.GL11.*;

public class ApplicationContext {

    @Getter private RootElement root;
    @Setter @Getter private Element renderElement;

    @Getter private SceneContext sceneContext;
    @Getter private ElementManager elementManager;

    private static ApplicationContext instance;
    public static ApplicationContext instance(){
        if(instance == null)
            instance = new ApplicationContext();
        return instance;
    }

    public void init(){

        root = new RootElement();
        renderElement = root;
        elementManager = ElementManager.instance();

//        // initialize pipeline from config
//        try {
//
//            this.sceneContext = Core.class.getClassLoader().loadClass(
//                    Config.instance().getEngineInstance()).asSubclass(SceneContext.class).newInstance();
//            sceneContext.loadRenderer();
//        }
//        catch(ClassNotFoundException e){
//            e.printStackTrace();
//            System.err.println("Render engine class does not exist: " + Config.instance().getRenderEngine());
//            System.exit(-1);
//        }
//        catch (InstantiationException e){
//            e.printStackTrace();
//            System.exit(-1);
//        }
//        catch (IllegalAccessException e){
//            e.printStackTrace();
//            System.exit(-1);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            System.err.println("Render engine does not take SceneContext in its constructor: " + Config.instance().getRenderEngine());
//            System.exit(-1);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        // initialize engine implementation
//
//        sceneContext.init();
        elementManager.init(this);

        // replace with application load protocol
        __init__ui();

    }

    // TODO: create application loader
    // TEMPORARY FOR TESTING
    private void __init__ui(){

        //SceneViewport sceneViewport = new SceneViewport(sceneContext);


//        sceneViewport.setBox(new Box(0.3f, 0.1f, 0.65f, 0.8f));


        Button button = new Button(), up = new Button(), down = new Button(), ssrToggle = new Button();
        button.addListener(e -> Config.instance().setSsao(!Config.instance().isSsao()));
        button.addListener(e -> System.out.println("Pressed SSAO button"));
        up.addListener(e -> Config.instance().setSsaoPower(Config.instance().getSsaoPower()+1));
        up.addListener(e -> System.out.println("Pressed SSAO UP button"));
        down.addListener(e -> Config.instance().setSsaoPower(Config.instance().getSsaoPower()-1));
        down.addListener(e -> System.out.println("Pressed SSAO DOWN button"));
        ssrToggle.addListener(e -> Config.instance().setSsr(!Config.instance().isSsr()));
        ssrToggle.addListener(e -> System.out.println("Pressed SSR button"));

        VerticalViewport viewport = new VerticalViewport(40, 100, 100, button, up, down, ssrToggle);
        root.addChildren(viewport);//, sceneViewport);
        viewport.setBox(new Box(0.25f, 0.1f, 0.5f, 0.5f));

        //root.setTop(sceneViewport);
        root.forceLayout();
    }

    public void update(){
        if(Window.instance().isResized()){
            root.forceLayout();
            Window.instance().setResized(false);
        }

        elementManager.update();
        //sceneContext.update();
        root.update();

        if(InputManager.instance().isKeyPressed(GLFW_KEY_F1))
            Config.instance().loadFromConfigFile();

    }

    public void draw(){
        Window.instance().setBlending(false);
        //sceneContext.render();
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
