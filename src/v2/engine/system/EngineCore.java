package v2.engine.system;

import lombok.Getter;

public class EngineCore implements Runnable {

    public static final int TARGET_FPS = 144;

    @Getter
    private float currentFPS;

    private final Thread gameLoopThread;
    private final Timer timer;

    @Getter private final Window window;
    @Getter private final RenderEngine renderEngine;
    @Getter private final EngineInterface engineInterface;
    @Getter private final InputCore input;

    public EngineCore(EngineInterface engineInterface) {

        this.gameLoopThread = new Thread(this, "ENGINE_0");
        this.window = Window.getInstance();
        this.engineInterface = engineInterface;
        this.timer = new Timer();
        this.renderEngine = RenderEngine.getInstance();
        this.input = InputCore.getInstance();
        this.currentFPS = 0;
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

    protected void init() throws Exception{

        // initialize core systems before initializing interface
        window.init();
        timer.mark();
        input.init(window);

        renderEngine.init();
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

        int frameCounter = 0;
        float timeTracker = (float) timer.getTime();
        float toOne = 0;

        while(!window.shouldClose()){

            if(toOne>=1){
                toOne -= 1;
                currentFPS = frameCounter;
                frameCounter = 0;
            }

            update(timer.getTime() - lastUpdate);
            lastUpdate = timer.getTime();
            render();

            while( timer.getTimeToNext(frameLength) - timer.getTime() > -.001 &&
                   timer.getTimeToNext(frameLength) - timer.getTime() < .001){
                try{ Thread.sleep(1);}
                catch(InterruptedException e){}
            }
            toOne += timer.getTime() - timeTracker;
            timeTracker = (float) timer.getTime();
            frameCounter++;
        }

    }

    protected void update(double interval){

        /* mark timer at start of each update */
        timer.mark();

        /* update input */
        input.update();

        /* call update code from interface */
        engineInterface.update(interval);
        window.setTitle(currentFPS, RenderEngine.getInstance().getMainCamera());

    }

    protected void render(){
        window.update();
        renderEngine.render();
    }

    void cleanup(){
        engineInterface.cleanup();
        renderEngine.cleanup();
    }

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
            return System.nanoTime()/1000_000_000.0;
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
        public double getTimeToNext(float interval){
            double ret = interval - getTime() - lastLoopTime;
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
