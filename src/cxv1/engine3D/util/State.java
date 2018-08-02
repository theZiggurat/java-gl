package cxv1.engine3D.util;

public class State {

    public static final double SECONDS_IN_DAY = 86400f;
    private static final float DEFAULT_PHYSICS_MULTIPLIER = 1.0f;
    private static final float DEFAULT_SUN_SPEED_MODIFIER = 288;

    private float physicsMultiplier;
    private float sunSpeedMultiplier;

    private double physicsTimer;
    private double sunTimer;

    private int days;
    private double interval;

    private static State instance;

    public static State getInstance(){
        if(instance == null){
            instance = new State();
            instance.init();
        }
        return instance;
    }

    private State(){
        this.physicsMultiplier = DEFAULT_PHYSICS_MULTIPLIER;
        this.sunSpeedMultiplier = DEFAULT_SUN_SPEED_MODIFIER;
    }

    public State(float physicsMultiplier, float sunSpeedMultiplier){
        this.physicsMultiplier = physicsMultiplier;
        this.sunSpeedMultiplier = sunSpeedMultiplier;
    }

    public void init(){
        interval = 0;
        days = 0;
        physicsTimer = 0;
        sunTimer = 0;
    }

    public void update(double interval){
        this.interval = interval;

        sunTimer += interval * sunSpeedMultiplier;
        physicsTimer += interval * physicsMultiplier;

        if(sunTimer >= SECONDS_IN_DAY){
            sunTimer = 0;
            days++;
        }

        if(physicsTimer >= SECONDS_IN_DAY){
            physicsTimer = 0;
        }
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    public float getPhysicsMultiplier() {
        return physicsMultiplier;
    }

    public void setPhysicsMultiplier(float physicsMultiplier) {
        this.physicsMultiplier = physicsMultiplier;
    }

    public float getSunSpeedMultiplier() {
        return sunSpeedMultiplier;
    }

    public void setSunSpeedMultiplier(float sunSpeedMultiplier) {
        this.sunSpeedMultiplier = sunSpeedMultiplier;
    }

    public double getPhysicsTimer() {
        return physicsTimer;
    }

    public void setPhysicsTimer(double physicsTimer) {
        this.physicsTimer = physicsTimer;
    }

    public double getSunTimer() {
        return sunTimer;
    }

    public double getRelativeSunTimer() {
        return sunTimer/SECONDS_IN_DAY;
    }

    public void setSunTimer(double sunTimer) {
        this.sunTimer = sunTimer;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }


}
