package cxv1.engine3D.util;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.lighting.DirectionalLight;
import cxv1.engine3D.draw.lighting.PointLight;
import cxv1.engine3D.draw.lighting.SpotLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderUtil() throws Exception{
        uniforms = new HashMap<>();
        programId = glCreateProgram();
        if(programId == 0){
            throw new Exception("Could not create shader");
        }
    }

    // SHADERS ----------

    /*
        VERTEX SHADER
     */
    public void createVertexShader(String shaderCode)throws Exception{
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /*
        FRAGMENT SHADER
     */
    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /*
        Compiles shader from shader source code and attatches to shaderId
     */
    protected int createShader(String shaderCode, int shaderType) throws Exception{
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

    /*
        Must be launcehd after creating shaders
     */
    public void link() throws Exception{
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0){
            throw new Exception("Error linking Shader: " + glGetProgramInfoLog(programId, 1024));
        }
        if(vertexShaderId !=0){ glDetachShader(programId, vertexShaderId); }
        if(fragmentShaderId !=0){ glDetachShader(programId, fragmentShaderId); }
        glValidateProgram(programId);

        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0){
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    // UINIFORMS -----------

    // create uniform and add it to class map
    public void createUniform(String name) throws Exception{
        int uniformLocation = glGetUniformLocation(programId, name);
        if(uniformLocation < 0){
            throw new Exception("Could not find uniform "+ name);
        }
        uniforms.put(name, uniformLocation);
    }

    // matrix4 uniform
    public void setUniform(String name, Matrix4f matrix){

        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            matrix.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    // single int uniform (i)
    public void setUniform(String name, int id){
        glUniform1i(uniforms.get(name), id);
    }

    // single float uniform (f)
    public void setUniform(String name, float value) {
        glUniform1f(uniforms.get(name), value);
    }

    // vec4 uniform (<x,y,z,w>)
    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    // vec3 uniform (<x,y,z>)
    public void setUniform(String name, Vector3f vec){
        glUniform3f(uniforms.get(name), vec.x, vec.y, vec.z);
    }

    // STRUCT UNIFORMS -------------------------------------------------------

    // SpotLight uniform
    public void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".point");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }   public void setUniform(String uniformName, SpotLight spotLight){
        setUniform(uniformName + ".point", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    // SpotLight array uniform
    public void createSpotLightsUniform(String uniformName, int size) throws Exception {
        for(int i = 0; i < size; i++){
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }   public void setUniform(String uniformName, SpotLight spotLight, int pos){
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    // PointLight uniform
    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }   public void setUniform(String name, PointLight light){
        setUniform(name + ".color", light.getColor());
        setUniform(name + ".position", light.getPosition());
        setUniform(name + ".intensity", light.getIntensity());
        PointLight.Attenuation att = light.getAttenuation();
        setUniform(name + ".att.constant", att.getConstant());
        setUniform(name + ".att.linear", att.getLinear());
        setUniform(name + ".att.exponent", att.getExponent());
    }

    // PointLight array uniform
    public void createPointLightsUniform(String uniformName, int size) throws Exception {
        for(int i = 0; i < size; i++){
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }   public void setUniform(String uniformName, PointLight pointLight, int pos){
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    // DirectionLight uniform
    public void createDirectionLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }   public void setUniform(String uniformName, DirectionalLight light){
        setUniform(uniformName + ".color", light.getColor());
        setUniform(uniformName + ".direction", light.getDirection());
        setUniform(uniformName + ".intensity", light.getIntensity());
    }


    // Material uniform -------------------------------------------------------
    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }   public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    // ----------------------

    public void bind(){
        glUseProgram(programId);
    }

    public void unbind(){ glUseProgram(0); }

    public void cleanup(){
        unbind();
        if(programId !=0){
            glDeleteProgram(programId);
        }
    }
}
