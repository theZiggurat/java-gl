package v2.engine.application;

import lombok.Getter;
import lombok.Setter;
import v2.engine.application.element.*;
import v2.engine.scene.light.LightManager;
import v2.engine.system.Config;
import v2.engine.system.Core;
import v2.engine.scene.SceneContext;
import v2.engine.system.Window;
import v2.modules.pbr.PBRPipeline;

import java.lang.reflect.InvocationTargetException;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

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

        // initialize pipeline from config
        try {

            this.sceneContext = Core.class.getClassLoader().loadClass(
                    Config.instance().getEngineInstance()).asSubclass(SceneContext.class).newInstance();
            sceneContext.loadRenderer();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Render engine class does not exist: " + Config.instance().getRenderEngine());
            System.exit(-1);
        }
        catch (InstantiationException e){
            e.printStackTrace();
            System.exit(-1);
        }
        catch (IllegalAccessException e){
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
        elementManager.init(this);

        // replace with application load protocol
        __init__ui();

    }

    // TODO: create application loader
    // TEMPORARY FOR TESTING
    private void __init__ui(){

        //panel = new Panel();
        //root.addChild(panel);
        root.addChild(new SceneViewport(sceneContext));

        root.addChild(new PBRDEBUGViewport((PBRPipeline) sceneContext.getPipeline()));

        PlainViewport viewport = new PlainViewport();

        Button button = new Button();
        button.addListener(e -> {
            Config.instance().setSsao(!Config.instance().isSsao());
        });
        viewport.getChildren().get(1).addChild(button);
        root.addChild(viewport);
        //panel.setColor(new Color(0xaa00aa));
        //panel.setBox(new Box(0.05f, 0.05f, 0.15f, 0.7f));

        root.setTop(root.getChildren().get(0));
        root.forceLayout();
    }

    public void update(){
        if(Window.instance().isResized()){
            root.forceLayout();
            Window.instance().setResized(false);
        }

        elementManager.update();
        sceneContext.update();
        root.update();

    }

    public void draw(){
        Window.instance().setBlending(false);
        sceneContext.render();
        Window.instance().setBlending(true);
        glDisable(GL_DEPTH_TEST);
        renderElement.render();
        glEnable(GL_DEPTH_TEST);
    }

    public void resetRenderElement(){
        setRenderElement(root);
    }







}
