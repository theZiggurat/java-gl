package cxv1.engine3D.draw.lighting;

import cxv1.engine3D.util.State;
import org.joml.Vector3f;

import java.awt.*;


public class Sun {

    private static final Vector3f DEFAULT_DIRECTION = new Vector3f(0f, 0f, 0f);
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1f, 1f, 1f);
    private static final float DEFAULT_INTENSITY = 1f;

    private float timeOfDay;
    private float sunAngle;

    DirectionalLight light;

    public Sun(float initial_angle){
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

    public void updateLight(double time){

        light.getDirection().x = (float) Math.sin(timeToRadians(time));
        light.getDirection().y = (float) Math.cos(timeToRadians(time));

        light.setIntensity(Math.max(DEFAULT_INTENSITY * (float) Math.sin(timeToRadians(time)),0));
    }

    public static float timeToRadians(double time){
        return (float) Math.toRadians(360*time/State.SECONDS_IN_DAY);
    }
}
