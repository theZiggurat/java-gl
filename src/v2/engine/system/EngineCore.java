package v2.engine.system;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLOutput;
import java.util.HashMap;


public class EngineCore implements Runnable {

    public static final int TARGET_FPS = 30;

    @Getter
    private float currentFPS;

    private final Thread gameLoopThread;
    private final Timer timer;

    private final Window window;
    @Getter private RenderEngine renderEngine;
    private EngineInterface engineInterface;

    //private HashMap<Class, RenderEngine> renderEngines;

    private final Input input;

    private static EngineCore instance;

    public static EngineCore instance(){
        return instance;
    }

    public EngineCore() {

        this.gameLoopThread = new Thread(this, "ENGINE_MAIN");
        this.window = Window.instance();
        this.timer = new Timer();
        this.input = Input.instance();
        this.currentFPS = 0;

        instance = this;

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

    protected void init(){

        // initialize core systems before initializing interface
        window.init();
        input.init();
        timer.mark();

        // initialize renderer from config
        try {
            this.renderEngine = EngineCore.class.getClassLoader().loadClass(
                    Config.instance().getRenderEngine()).asSubclass(RenderEngine.class).newInstance();
            this.engineInterface = EngineCore.class.getClassLoader().loadClass(
                    Config.instance().getEngineInstance()).asSubclass(EngineInterface.class).newInstance();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Render engine class does not exist: " + renderEngine);
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
        engineInterface.init();
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

        /* update input */
        input.update();

        /* call update code from interface */
        engineInterface.update(interval);
        window.setTitle(1/interval, renderEngine.getMainCamera());

    }

    protected void render(){
        window.update();
        renderEngine.draw();
    }

    void cleanup(){
        engineInterface.cleanup();
        renderEngine.cleanup();
    }

//    public void addRenderer(@NotNull RenderEngine renderer){
//        renderEngines.put(renderer.getClass(), renderer);
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
}
