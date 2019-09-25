package engine.utils;

public class Interpolation {

    public static float smoothstep(float x){
        return x * x * (3 - 2 * x);
    }

    public static float smoothstep(float t1, float t2, float x){
        float dif = t2 - t1;
        return t1 + dif * smoothstep(x);
    }

    public static float smootherstep(float x){
        return (x * x * x * (x * (x * 6 - 15) + 10));
    }

    public static float smootherstep(float t1, float t2, float x){
        float dif = t2 - t1;
        return t1 + dif * smootherstep(x);
    }
}
