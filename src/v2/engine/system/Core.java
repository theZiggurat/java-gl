package v2.engine.system;

import lombok.Getter;
import v2.engine.application.ApplicationContext;
import v2.engine.application.event.InputManager;


public class Core implements Runnable {

    public static final int TARGET_FPS = 30;

    @Getter
    private float currentFPS;

    private final Thread gameLoopThread;
    private final Timer timer;
    private final Window window;
    private final InputManager input;
    private ApplicationContext app;

    @Getter private double timeDelta;

    //private HashMap<Class, Pipeline> renderEngines;


    private static Core instance;
    public static Core runtime(){
        return instance;
    }

    public Core() {

        this.gameLoopThread = new Thread(this, "ENGINE_MAIN");

        this.window = Window.instance();
        this.app = ApplicationContext.instance();
        this.input = InputManager.instance();

        this.currentFPS = 0;
        this.timeDelta = 0;

        this.timer = new Timer();

        if (instance == null) instance = this;

    }

    @Override
    public void run() {
        try {
            init();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Constructs context
     */
    protected void init() {

        // initialize core systems before initializing interface
        window.init();
        app.init();
        input.init(app);
        timer.mark();
    }

    /**
     * To be called from MAIN
     */
    public void start() {
        gameLoopThread.start();
    }

    protected void loop() {

        float frameLength = 1.0f / TARGET_FPS;

        while (!window.shouldClose()) {

            timeDelta = timer.getTimeFromLast()/1_000_000d;
            currentFPS = (float) (1 / timeDelta)*1000;
            timer.mark();

            /* update UI and scene */
            app.update();
            app.draw();


            //window.setTitle(1/interval, context.getCamera());

            /* update input */
            input.update();

            //System.out.println(timer.getTimeFromLast());

            window.update();

            while (timer.getTimeToNext(frameLength) > 1) {
                try { Thread.sleep(1);} catch (InterruptedException e){}
            }
        }

    }

    void cleanup() {

    }

//    public void addRenderer(@NotNull Pipeline pipeline){
//        renderEngines.put(pipeline.getClass(), pipeline);
//    }

    /**
     * Private class used to time the main loop
     */
    public class Timer {

        private double lastLoopTime;

        /**
         * Returns system time in seconds
         *
         * @return time in seconds
         */
        public double getTime() {
            return System.nanoTime();
        }

        public double getTimeMS() {
            return System.nanoTime() / 1_000_000;
        }

        /**
         * Marks the timer
         */
        public void mark() {
            lastLoopTime = getTime();
        }

        /**
         * From a given interval, returns time to next interval
         *
         * @param interval time per frame (FPS)^-1
         * @return time til next next frame, or 0 if passed
         */
        public double getTimeToNext(double interval) {
            double ret =  (interval * 100_000_000) + lastLoopTime - getTime();
            return ret < 0 ? ret : 0;
        }

        /**
         * Time elapsed since last mark() call
         *
         * @return time from last frame
         */
        public double getTimeFromLast() {
            return getTime() - lastLoopTime;
        }

    }

}
