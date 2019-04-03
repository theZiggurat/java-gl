package v2.engine.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Vector;

public class Buffer {


    public static FloatBuffer buffer3f(ArrayList<Vector3f> positions){
        float [] ret = new float[positions.size()*3];
        int i = 0;
        for(Vector3f position: positions)
        {
            ret[i++] = position.x;
            ret[i++] = position.y;
            ret[i++] = position.z;
        }
        return floatBuffer(ret);
    }

    public static FloatBuffer buffer2f(ArrayList<Vector2f> UVs){

        float [] ret = new float[UVs.size()*2];
        int i = 0;
        for(Vector2f UV: UVs)
        {
            ret[i++] = UV.x;
            ret[i++] = UV.y;
        }
        return floatBuffer(ret);
    }

    public static IntBuffer indiciesBuffer(ArrayList<Integer> indices){
        return intBuffer(indices.parallelStream()
                .mapToInt(i -> i)
                .toArray());
    }

    public static FloatBuffer floatBuffer(float... data){
        FloatBuffer ret = MemoryUtil.memAllocFloat(data.length);
        return (FloatBuffer) ret.put(data).flip();
    }

    public static IntBuffer intBuffer(int... data){
        IntBuffer ret = MemoryUtil.memAllocInt(data.length);
        return (IntBuffer) ret.put(data).flip();
    }
}
