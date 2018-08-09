package v2.engine.render;

import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import v2.engine.scene.ModuleNode;
import v2.engine.system.StaticLoader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengles.GLES20.glCreateProgram;
import static org.lwjgl.opengles.GLES20.glUseProgram;

public class ShaderProgram {

    private final int programId;
    private int vertexShader, fragmentShader, geometryShader, computeShader;

    private final Map<String, Integer> uniforms;

    public ShaderProgram(){
        this.programId = glCreateProgram();
        this.uniforms = new HashMap<>();

        if (programId == 0) {
            System.err.println("Shader failed to create: " + this.getClass().getName());
            System.exit(1);
        }
    }

    /**
     *  Bind shaderprogram to openGL render pipeline
     */
    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){
        glUseProgram(0);
    }

    protected int createShader(String shaderPath, int shaderType) throws Exception {

        String shaderCode = StaticLoader.loadResource(shaderPath);

        int shaderId = glCreateShader(shaderType);

        if(shaderId == 0){
            throw new Exception("Error creating shader. Type: " +glGetShaderInfoLog(shaderId, 1024));
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0){
            throw new Exception("Error compiling shader: "+glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);
        return shaderId;

    }

    public void cleanup(){
        unbind();
        if(programId !=0){
            glDeleteProgram(programId);
        }
    }

    /**
     * Vertex Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Exception  if shader is not found
     */
    public void createVertexShader(String shaderPath)throws Exception {
        vertexShader = createShader(shaderPath, GL_VERTEX_SHADER);
    }

    /**
     * Fragment Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Exception  if shader is not found
     */
    public void createFragmentShader(String shaderPath) throws Exception {
        fragmentShader = createShader(shaderPath, GL_FRAGMENT_SHADER);
    }

    /**
     * Geometry Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Exception  if shader is not found
     */
    public void createGeometryShader(String shaderPath) throws Exception {
        geometryShader = createShader(shaderPath, GL_GEOMETRY_SHADER);
    }

    /**
     * Compute Shader
     * @param shaderPath  path of shader code in "/res/shaders/*"
     * @throws Exception  if shader is not found
     */
    public void createComputeShader(String shaderPath) throws Exception {
        computeShader = createShader(shaderPath, GL_COMPUTE_SHADER);
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

        glValidateProgram(programId);

        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0){
            System.out.println(this.getClass().getName() + " " + glGetProgramInfoLog(programId, 1024));
            System.exit(1);
        }
    }

    /**
     * Registers uniform name in shader program
     * @param uniform_name  name of uniform in GLSL file
     */
    public void addUniform(String uniform_name){
        int uniformLocation = glGetUniformLocation(programId, uniform_name);

        if(uniformLocation == 0){
            System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform_name);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform_name, uniformLocation);
    }

    // PRIMITIVE UNIFORM TYPES //

    /**
     * single int uniform (i)
     * @param name uniform name registered from addUniform
     * @param value  int data
     */
    public void setUniform(String name, int value){
        if(uniforms.get(name)==null){ return; }
        glUniform1i(uniforms.get(name), value);
    }

    /**
     * single float uniform (f)
     * @param name
     * @param value float data
     */
    public void setUniform(String name, float value) {
        if(uniforms.get(name)==null){ return; }
        glUniform1f(uniforms.get(name), value);
    }

    /**
     * vec4 uniform (x,y,z,w)
     * @param name  uniform name registered from addUniform
     * @param value  vector data
     */
    public void setUniform(String name, Vector4f value) {
        if(uniforms.get(name)==null){ return; }
        glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    /**
     * vec3 uniform (x,y,z)
     * @param name  uniform name registered from addUniform
     * @param value  vector data
     */
    public void setUniform(String name, Vector3f vec){
        if(uniforms.get(name)==null){ return; }
        glUniform3f(uniforms.get(name), vec.x, vec.y, vec.z);
    }

    /**
     * matrix uniform (mat4)
     * @param name  uniform nname registered from addUniform
     * @param matrix  matrix data
     */
    public void setUniform(String name, Matrix4f matrix){
        if(uniforms.get(name)==null){ return; }
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    // UPDATE INTERFACE //

    public void updateUniforms(ModuleNode moduleNode){}



}
