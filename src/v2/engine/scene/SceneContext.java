package v2.engine.scene;

import lombok.Getter;
import org.joml.Vector2i;
import v2.engine.application.event.mouse.MouseMoveEvent;
import v2.engine.application.element.Element;
import v2.engine.scene.node.Scenegraph;
import v2.engine.system.Config;
import v2.engine.system.Core;
import v2.engine.system.Pipeline;
import v2.engine.system.Shader;

import java.lang.reflect.InvocationTargetException;

/**
 * Contains high level engine context for rendering scenes
 * and GUI layers, updating modules, and events
 */

public abstract class SceneContext extends Element {

    @Getter private Picking picking;

    // scenegraph of 3D scene
    @Getter protected Scenegraph scene;

    // uses scene to create texture of 3D scene
    @Getter private Pipeline pipeline;

    @Getter private SelectionManager selectionManager;

    @Getter protected Camera camera;

    @Getter private Vector2i resolution;

    protected SceneContext() {

        super();

        this.scene = new Scenegraph();
        this.camera = new Camera(this);
        this.picking = new Picking(this);
        this.selectionManager = new SelectionManager();
        this.resolution = new Vector2i(0,0);

        onEvent(e -> {
            if(!(e instanceof MouseMoveEvent))
                camera.handle(e);
        });

    }

    /**
     * Initialize engine scene.
     * GUI: addChildren(new Button())
     * 3D: scene.addChild(new PBRModel())
     * Custom classes, etc.
     */
    public abstract void init();

    /**
     * Called every engine update
     * @param duration time since last update in nano seconds
     */
    protected abstract void update(double duration);

    /**
     * Called on engine shutdown
     */
    public abstract void cleanup();


    /**
     * Reflective method for loading custom render pipeline
     * Will load whichever class of Pipeline is specified in
     * /res/config.properties: "renderEngine"
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void loadRenderer() throws
            ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException
    {
        this.pipeline = SceneContext.class
                        .getClassLoader()
                        .loadClass(Config.instance().getRenderEngine())
                        .asSubclass(Pipeline.class)
                        .getConstructor(SceneContext.class)
                        .newInstance(this);
    }

    public void update(){
        // instance update
        update(Core.runtime().getTimeDelta());

        // scene UI update
        super.update();

        // now update scene with proper inputs reaching the
        scene.update();
        camera.update();
    }

    /**
     * Contractually you must assign scene context for
     * shader class to be known for rendering information
     */
    public void render(){
        Shader.setBoundContext(this);
        pipeline.draw();
    }

    public void setResolution(Vector2i size){
        if(!resolution.equals(size)){
            this.resolution.x = size.x;
            this.resolution.y = size.y;
            pipeline.resize();
            picking.getUUIDmap().resize(size.x, size.y);
        }
    }

    public float getAspectRatio(){
        return (float)resolution.x/(float)resolution.y;
    }

}
