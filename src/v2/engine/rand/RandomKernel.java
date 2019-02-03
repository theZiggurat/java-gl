package v2.engine.rand;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomKernel {

    public static List<Vector3f> generate3fHemisphere(int size) {

        List<Vector3f> ret = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            Vector3f randVec = new Vector3f();
            randVec.x = rand.nextFloat() * 2 - 1;
            randVec.y = rand.nextFloat() * 2 - 1;
            randVec.z = rand.nextFloat();

            randVec.normalize().mul(rand.nextFloat());

//            float scale = i/size;
//            scale = .1f + scale*scale*.9f;
//            randVec.mul(scale);

            ret.add(randVec);
        }
        return ret;
    }

    public static float[] generateXYNoise(int length){

        float floats[] = new float[length*2];
        int i = 0;
        Random rand = new Random();

        while(i<length*2){
            floats[i++] = rand.nextFloat() * 2 -1;
            floats[i++] = rand.nextFloat() * 2 -1;
        }
        return floats;
    }


}
