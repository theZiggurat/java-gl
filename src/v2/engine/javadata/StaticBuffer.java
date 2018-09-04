package v2.engine.javadata;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class StaticBuffer {

    public static FloatBuffer floatBuffer(float... data){
        FloatBuffer ret = MemoryUtil.memAllocFloat(data.length);
        return (FloatBuffer) ret.put(data).flip();
    }

    public static IntBuffer intBuffer(int... data){
        IntBuffer ret = MemoryUtil.memAllocInt(data.length);
        return (IntBuffer) ret.put(data).flip();
    }
}
