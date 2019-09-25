package modules.post.ssao;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomKernel {

    /**
     * Generates a list of random vectors beloing to hemisphere:
     *  X: [-1,1]
     *  Y: [-1,1]
     *  Z: [0,1]
     *  of length [0,1]
     * @param size amount of vectors to be returned
     * @return list of random vectors
     */
    static List<Vector3f> generate3fHemisphere(int size) {

        List<Vector3f> ret = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            Vector3f randVec = new Vector3f();
            randVec.x = rand.nextFloat() * 2 - 1;
            randVec.y = rand.nextFloat() * 2 - 1;
            randVec.z = rand.nextFloat();

            randVec.normalize().mul(rand.nextFloat());
            ret.add(randVec);
        }
        return ret;
    }

    static float[] generateXYNoise(int length){

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
