package v1.engine.util;

public class Timer {

    private double lastLoopTime;

    public void init(){
        lastLoopTime = getTime();
    }

    public double getTime(){
        return System.nanoTime()/1000_000_000.0;
    }

    public void mark(){
        lastLoopTime = getTime();
    }

    public double getTimeToNext(float interval){
        return interval - getTime() - lastLoopTime;
    }

    public double getTimeFromLast(){
        return getTime() - lastLoopTime;
    }
}
