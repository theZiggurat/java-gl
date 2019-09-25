package engine.glapi;

import engine.system.Window;
import org.joml.*;
import org.lwjgl.system.MemoryStack;
import engine.scene.SceneContext;
import engine.scene.light.Light;
import engine.scene.node.ModuleNode;
import engine.utils.Color;
import engine.utils.Utils;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;
import static org.lwjgl.opengl.GL42.glBindImageTexture;

public class Shader {

    protected static SceneContext boundContext = null;

    private final int programId;
    private int vertexShader = -1, fragmentShader = -1,
            geometryShader = -1, computeShader = -1;

    private final Map<String, Integer> uniforms;

    protected Shader(){
        this.programId = glCreateProgram();
        this.uniforms = new HashMap<>();

        if (programId == 0) {
            System.err.println("Shader failed to create: " + this.getClass().getName());
            System.exit(1);
        }
    }

    /**
     * https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glUseProgram.xhtml
     */
    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    protected int createShader(String shaderPath, int shaderType) throws Exception {

        String shaderCode = Utils.loadResource(shaderPath);

        int shaderId = glCreateShader(shaderType);

        if(shaderId == 0){
            throw new Exception("Error creating shader. Type: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0){
            throw new Exception("Error compiling shader: "+glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);
        return shaderId;

    }

    /**
     * From Khronos doc:
     * glDeleteProgram frees the memory and invalidates
     * the name associated with the program object specified
     * by program. This command effectively undoes the effects
     * of a call to glCreateProgram.
     *
     * https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glDeleteProgram.xhtml
     */
    public void cleanup(){
        unbind();
        if(programId !=0){
            glDeleteProgram(programId);
        }
    }

    /**
     * Dispatches compute shader if created/linked. Default size is window resolution
     * @param groupX group size x
     * @param groupY group size y
     */
    public void compute(int groupX, int groupY){
        if(computeShader != -1){

            int numX, numY;
            numX = (int)(Math.ceil((double)Window.instance().getWidth()/(double)groupX));
            numY = (int)(Math.ceil((double)Window.instance().getHeight()/(double)groupY));

            bind();
            glDispatchCompute(numX, numY, 1);

        }
    }

    /**
     * Dispatches compute shader is created/linked. Custom size
     * @param groupX group size x
     * @param groupY group size y
     * @param sizeX total size x
     * @param sizeY total size y
     */
    public void compute(int groupX, int groupY, Vector2i resolution){
        if(computeShader != -1){
            bind();
            glDispatchCompute((int)Math.ceil((double)resolution.x/(double)groupX),
            (int)Math.ceil((double)resolution.y/(double)groupY), 1);
        }
    }

    /**
     * Binds image for shader. They are imported in the shader with:
     * layout (binding = *index*, *format*) uniform *access* image2D __;
     * @param index index in shader | layout (binding = index)
     * @param textureID accessed by texture.getId()
     * @param access GL_READ_ONLY, GL_WRITE_ONLY, GL_READ_WRITE
     * @param format GL_RGBA16F, GL_RGBA32F, GL_RED, etc.
     */
    public void bindImage(int index, int textureID, int access, int format){
        glBindImageTexture(index, textureID, 0, false,
                0, access, format);
    }


    /**
     * Vertex Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Error if shader is not found
     */
    public void createVertexShader(String shaderPath) {
        try{
            vertexShader = createShader(shaderPath, GL_VERTEX_SHADER);
        } catch(Exception e) {
            System.err.println("Could not create vertex shader: " + shaderPath);
            e.printStackTrace();
        }
    }

    /**
     * Fragment Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Error if shader is not found
     */
    public void createFragmentShader(String shaderPath) {
        try {
            fragmentShader = createShader(shaderPath, GL_FRAGMENT_SHADER);
        } catch(Exception e){
            System.err.println("Could not create fragment shader: " + shaderPath);
            e.printStackTrace();
        }
    }

    /**
     * Geometry Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Error if shader is not found
     */
    public void createGeometryShader(String shaderPath) {
        try {
            geometryShader = createShader(shaderPath, GL_GEOMETRY_SHADER);
        } catch (Exception e) {
            System.err.println("Could not create geometry shader: " + shaderPath);
            e.printStackTrace();
        }
    }

    /**
     * Compute Shader
     * @param shaderPath  path of shader code in "res/shaders/*"
     * @throws Error if shader is not found
     */
    public void createComputeShader(String shaderPath) {
        try {
            computeShader = createShader(shaderPath, GL_COMPUTE_SHADER);
        } catch(Exception e) {
            System.err.println("Could not create compute shader: " + shaderPath);
            e.printStackTrace();
        }
    }

    /**
     * Links shader code added by createShader method. Must be called after createShader.
     * Will quit program if shaders do not link.
     */
    public void link(){
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0){
            System.out.println(this.getClass().getName() + " " + glGetProgramInfoLog(programId, 1024));
            System.exit(1);
        }

        glDetachShader(programId, vertexShader);
        glDetachShader(programId, fragmentShader);

        glValidateProgram(programId);

        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0){
            System.out.println(this.getClass().getName() + " " + glGetProgramInfoLog(programId, 1024));
            System.exit(1);
        }

        System.out.println(this.getClass().getName() + " linked");
    }

    /**
     * Registers uniform name in shader program
     * @param uniform_name  name of uniform in GLSL file
     */
    public void addUniform(String uniform_name){

        int uniformLocation = glGetUniformLocation(programId, uniform_name);
        //System.out.println(uniform_name + " " + uniformLocation);

        if(uniformLocation == -1){
            System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform_name);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform_name, uniformLocation);
    }

    // PRIMITIVE UNIFORM TYPES //

    /**
     * single int uniform (instance)
     * @param name uniform name registered from addUniform
     * @param value  int data
     */
    public void setUniform(String name, int value){
        if(uniforms.get(name)==null) return;
        glUniform1i(uniforms.get(name), value);
    }

    /**
     * vec2i uniform (x,y)
     * @param name  uniform name registered from addUniform
     * @param vec vector data
     */
    public void setUniform(String name, Vector2i vec){
        if(uniforms.get(name)==null) return;
        glUniform2i(uniforms.get(name), vec.x, vec.y);
    }

    /**
     * single float uniform (f)
     * @param name
     * @param value float data
     */
    public void setUniform(String name, float value) {
        if(uniforms.get(name)==null) return;
        glUniform1f(uniforms.get(name), value);
    }

    /**
     * vec2 uniform (x,y)
     * @param name  uniform name registered from addUniform
     * @param vec vector data
     */
    public void setUniform(String name, Vector2f vec){
        if(uniforms.get(name)==null) return;
        glUniform2f(uniforms.get(name), vec.x, vec.y);
    }

    /**
     * vec3 uniform (x,y,z)
     * @param name  uniform name registered from addUniform
     * @param vec vector data
     */
    public void setUniform(String name, Vector3f vec){
        if(uniforms.get(name)==null) return;
        glUniform3f(uniforms.get(name), vec.x, vec.y, vec.z);
    }

    /**
     * vec4 uniform (x,y,z,w)
     * @param name  uniform name registered from addUniform
     * @param value  vector data
     */
    public void setUniform(String name, Vector4f value) {
        if(uniforms.get(name)==null) return;
        glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    /**
     * matrix uniform (mat3)
     * @param name  uniform nname registered from addUniform
     * @param matrix  matrix data
     */
    public void setUniform(String name, Matrix3f matrix){
        if(uniforms.get(name)==null) return;
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(9);
            matrix.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    /**
     * matrix uniform (mat4)
     * @param name  uniform nname registered from addUniform
     * @param matrix  matrix data
     */
    public void setUniform(String name, Matrix4f matrix){
        if(uniforms.get(name)==null) return;
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    public void setUniform(String name, Color color){
        if(uniforms.get(name)==null) return;
        glUniform3f(uniforms.get(name), color.redf(), color.greenf(), color.bluef());
    }

    public void setUniform(String name, Vector4i vec){
        if(uniforms.get(name)==null) return;
        glUniform4i(uniforms.get(name), vec.x, vec.y, vec.z, vec.w);
    }

    /**
     * Calls glActiveTexture with arg GL_TEXTURE0 + index
     * @param index of texture
     */
    public void activeTexture(TextureObject texture, int index){
        glActiveTexture(GL_TEXTURE0 + index);
        texture.bind();
    }

    /** UPDATE INTERFACE **/

    public void updateUniforms(ModuleNode moduleNode){}
    public void updateUniforms(TextureObject textureObject){}
    public void updateUniforms(Light light){}
    public void updateUniforms(float... floats){}

    void compute(TextureObject scene,
                        TextureObject overlay, TextureObject dest) {}


    public static void setBoundContext(SceneContext context){
        boundContext = context;
    }

    static SceneContext getBoundContext(){
        return boundContext;
    }


}
