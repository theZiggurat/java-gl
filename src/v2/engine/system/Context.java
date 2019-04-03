package v2.engine.system;

import lombok.Getter;
import lombok.Setter;
import v2.engine.event.Picking;
import v2.engine.event.SelectionManager;
import v2.engine.gui.element.DynamicWindow;
import v2.engine.gui.element.Element;
import v2.engine.gui.element.DynamicPanel;
import v2.engine.gui.element.RootElement;
import v2.engine.scene.Scenegraph;

/**
 * Contains high level engine context for rendering scenes
 * and GUI layers, updating modules, and events
 */

public abstract class Context {

    // root UI element
    @Getter private Element root;

    @Getter private Picking picking;

    // scenegraph of 3D scene
    @Getter protected Scenegraph scene;

    // uses scene to create texture of 3D scene
    @Getter private Pipeline pipeline;

    // dynamicpanel UI element that displays 3D scene texture
    @Getter private DynamicPanel viewport;

    @Getter private SelectionManager selectionManager;

    @Setter @Getter
    protected Camera camera;

    protected Context() {
        this.root = new RootElement();
        this.viewport = new DynamicPanel();
        this.root.addChild(new DynamicWindow(viewport));
        this.scene = new Scenegraph();
        this.camera = new Camera();
        this.picking = new Picking(this);
        this.selectionManager = new SelectionManager();
    }

    void loadRenderer() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        this.pipeline = Context.class.getClassLoader().loadClass(
                Config.instance().getRenderEngine()).asSubclass(Pipeline.class).newInstance();
    }

    public abstract void init();
    protected abstract void update(double duration);
    public abstract void cleanup();

    void update(){
        // let UI updates happen first so the event can propogate to scene
        root.handle();

        // now update scene with proper inputs reaching the
        scene.update();

        camera.update();
    }

    void render(){
        pipeline.draw(viewport, this);
        root.render();
    }



}
