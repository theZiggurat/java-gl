package cxv1.engine3D.draw.lighting;

import cxv1.engine3D.util.State;
import org.joml.Vector3f;

import java.awt.*;


public class Sun {

    private static final Vector3f DEFAULT_DIRECTION = new Vector3f(0f, 1f, 1f);
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1f, 1f, 1f);
    private static final float DEFAULT_INTENSITY = 1f;

    private float timeOfDay;
    private float sunAngle;
    private float intensity;

    DirectionalLight light;

    public Sun(float initial_angle){
        intensity = DEFAULT_INTENSITY;
        this.sunAngle = initial_angle;
        light = new DirectionalLight(DEFAULT_COLOR, DEFAULT_DIRECTION, DEFAULT_INTENSITY);
    }

    public void setColor(Vector3f color){
        light.setColor(color);
    }

    public Vector3f getColor(){
        return light.getColor();
    }

    public DirectionalLight getLight(){
        return light;
    }

    // to be invoked on every game update using state.getSunTimer()
    public void updateLight(double time){

        light.getDirection().x = (float) Math.sin(timeToRadians(time));
        light.getDirection().y = (float) Math.cos(timeToRadians(time));

        light.setIntensity(Math.max(intensity * (float) Math.cos(timeToRadians(time)),0));
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public static float timeToRadians(double time){
        return (float) Math.toRadians(360*time/State.SECONDS_IN_DAY);
    }

}

