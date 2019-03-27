package v2.engine.system;

import lombok.Getter;
import v2.engine.event.Picking;
import v2.engine.event.SelectionManager;
import v2.engine.gui.GLViewport;
import v2.engine.scene.Scenegraph;


public class Core implements Runnable {

    public static final int TARGET_FPS = 30;

    @Getter
    private float currentFPS;

    private final Thread gameLoopThread;
    private final Timer timer;

    private final Window window;
    private Context context;

    //private HashMap<Class, Pipeline> renderEngines;

    private final Input input;

    private static Core instance;

    public Core() {

        this.gameLoopThread = new Thread(this, "ENGINE_MAIN");
        this.window = Window.instance();
        this.timer = new Timer();
        this.input = Input.instance();
        this.currentFPS = 0;

        if (instance == null) instance = this;

    }

    @Override
    public void run(){
        try{
            init();
            loop();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Constructs context
     */
    protected void init(){

        // initialize core systems before initializing interface
        window.init();
        input.init();
        timer.mark();

        // initialize pipeline from config
        try {

            this.context = Core.class.getClassLoader().loadClass(
                    Config.instance().getEngineInstance()).asSubclass(Context.class).newInstance();
            context.loadRenderer();
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
        }
        // initialize engine implementation

        context.init();
    }

    /**
     *  To be called from MAIN
     */
    public void start(){
        gameLoopThread.start();
    }

    protected void loop(){

        float frameLength = 1 / TARGET_FPS;
        double lastUpdate = timer.getTime();

        while(!window.shouldClose()){

            timer.mark();
            update((timer.getTime() - lastUpdate)/1000000000);

            lastUpdate = timer.getTime();
            render();

            //System.out.println(timer.getTimeFromLast());

            while( timer.getTimeToNext(frameLength) > 900){
                try{ Thread.sleep(1);}
                catch(InterruptedException e){}
            }
        }

    }

    protected void update(double interval){

        /* update UI and scene */
        context.update();

        /* call update code from interface */
        context.update(interval);

        window.setTitle(1/interval, context.getCamera());

        /* update input */
        input.update();


    }

    protected void render(){
        window.update();
        context.render();
    }

    void cleanup(){
        context.cleanup();
    }

//    public void addRenderer(@NotNull Pipeline pipeline){
//        renderEngines.put(pipeline.getClass(), pipeline);
//    }

    /**
     * Private class used to time the main loop
     */
    private class Timer {

        private double lastLoopTime;

        /**
         * Returns system time in seconds
         * @return time in seconds
         */
        public double getTime(){
            return System.nanoTime();
        }

        /**
         * Marks the timer
         */
        public void mark(){
            lastLoopTime = getTime();
        }

        /**
         * From a given interval, returns time to next interval
         * @param interval time per frame (FPS)^-1
         * @return time til next next frame, or 0 if passed
         */
        public double getTimeToNext(double interval){
            double ret = (interval*1000000000) - getTime() - lastLoopTime;
            return ret < 0 ? ret : 0;
        }

        /**
         * Time elapsed since last mark() call
         * @return time from last frame
         */
        public double getTimeFromLast(){
            return getTime() - lastLoopTime;
        }

    }

    // macros

    /**
     * Macro to this current context's active pipeline
     * @return this current context's active pipeline
     */
    public static Pipeline pipeline(){
        return instance.context.getPipeline();
    }

    /**
     * Macro to this current context's active viewport
     * @return this current context's active viewport
     */
    public static GLViewport viewport(){
        return instance.context.getViewport();
    }

    /**
     * Macro to this current context's active scene
     * @return this current context's active scene
     */
    public static Scenegraph scene(){
        return instance.context.getScene();
    }

    /**
     * Macro to this current context's active camera
     * @return this current context's active camera
     */
    public static Camera camera(){
        return instance.context.getCamera();
    }

    /**
     * Macro to this current context's active picking
     * @return this current context's active picking
     */
    public static Picking picking(){ return instance.context.getPicking();}

    /**
     * Macro to this current context's active picking
     * @return this current context's active picking
     */
    public static SelectionManager selectionManager(){ return instance.context.getSelectionManager();}

}
