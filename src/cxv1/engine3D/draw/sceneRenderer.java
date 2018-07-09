package cxv1.engine3D.draw;

import cxv1.engine3D.draw.lighting.DirectionalLight;
import cxv1.engine3D.draw.lighting.SceneLight;
import cxv1.engine3D.draw.lighting.SpotLight;
import cxv1.engine3D.draw.mesh.Mesh;
import cxv1.engine3D.draw.mesh.Mesh3D;
import cxv1.engine3D.entity.SkyBox;
import cxv1.engine3D.enviorment.Scene;
import cxv1.engine3D.util.ShaderUtil;
import cxv1.engine3D.util.Transformation;
import cxv1.engine3D.util.Utils;
import cxv1.engine3D.util.Window;
import cxv1.engine3D.draw.lighting.PointLight;
import cxv1.engine3D.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class sceneRenderer {

    public static final int MAX_POINT_LIGHT = 5;
    public static final int MAX_SPOT_LIGHT = 5;

    private float FOV = (float) Math.toRadians(90); // default val
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR  = 1000.0f;
    private Transformation transformation;
    private float specularPower;

    // LIGHT AFFECTTED
    private ShaderUtil sceneShader;

    // SKYBOX SHADER
    private ShaderUtil skyBoxShader;

    //
    private ShaderUtil terrainShader;

    // PUBLIC METHODS AND CONSTRUCTORS ----------------------------------------------------//

    public sceneRenderer(){
        transformation = new Transformation();
        specularPower = 10f;
    }

    /*
        Initialize shaders
     */
    public void init() throws Exception {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        setupSceneShader();
        setupSkyboxShader();
        setupTerrainShader();
    }

    /*
        Cleanup shaders
     */
    public void cleanup(){
        if(sceneShader != null){ sceneShader.cleanup(); }
        if(terrainShader != null){ terrainShader.cleanup(); }
        if(skyBoxShader != null){ skyBoxShader.cleanup(); }

        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /*
        Clear render buffer
     */
    public void clear(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public float getFov(){ return FOV; }
    public void setFov(float FOV){ this.FOV = FOV; }

    // SHADER SETUP -----------------------------------------------------------------------//

    /*
        Scene Shader
     */
    private void setupSceneShader() throws Exception{
        sceneShader = new ShaderUtil();
        sceneShader.createVertexShader(Utils.loadResource("/res/shaders/vertex.vs"));
        sceneShader.createFragmentShader(Utils.loadResource("/res/shaders/fragment.fs"));
        sceneShader.link();

        sceneShader.createUniform("projectionMatrix");
        sceneShader.createUniform("modelViewMatrix");
        sceneShader.createUniform("texture_sampler");

        sceneShader.createMaterialUniform("material");
        sceneShader.createUniform("specularPower");
        sceneShader.createUniform("ambientLight");
        sceneShader.createPointLightsUniform("pointLights", MAX_POINT_LIGHT);
        sceneShader.createSpotLightsUniform("spotLights", MAX_SPOT_LIGHT);
        sceneShader.createDirectionLightUniform("directionalLight");
    }

    private void setupTerrainShader() throws Exception{
        terrainShader = new ShaderUtil();
        terrainShader.createVertexShader(Utils.loadResource("/res/shaders/terrainvertex.vs"));
        terrainShader.createFragmentShader(Utils.loadResource("/res/shaders/terrainfragment.fs"));
        terrainShader.link();

        terrainShader.createUniform("projectionMatrix");
        terrainShader.createUniform("modelViewMatrix");
        terrainShader.createUniform("texture_sampler_grass");
        terrainShader.createUniform("texture_sampler_stone");

        terrainShader.createUniform("specularPower");
        terrainShader.createUniform("ambientLight");
        terrainShader.createPointLightsUniform("pointLights", MAX_POINT_LIGHT);
        terrainShader.createSpotLightsUniform("spotLights", MAX_SPOT_LIGHT);
        terrainShader.createDirectionLightUniform("directionalLight");
    }

    /*
        Skybox Shader
     */
    private void setupSkyboxShader() throws Exception {
        skyBoxShader = new ShaderUtil();
        skyBoxShader.createVertexShader(Utils.loadResource("/res/shaders/sky_vert.vs"));
        skyBoxShader.createFragmentShader(Utils.loadResource("/res/shaders/sky_frag.fs"));
        skyBoxShader.link();

        skyBoxShader.createUniform("modelViewMatrix");
        skyBoxShader.createUniform("projectionMatrix");
        skyBoxShader.createUniform("texture_sampler");
        skyBoxShader.createUniform("ambientLight");
    }

    // RENDER METHODS ------------------------------------------------------------------//


    /*
        Main render call
     */
    public void render(Window window, Camera camera, Scene scene){
        clear();

        // check for window resize
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // render scene
        renderScene(window, camera, scene);

        renderTerrain(window, camera, scene);



        // render skybox
        renderSkyBox(window, camera, scene);

    }

    /*
        Combines all renderScene methods into one call
        @param window, camera, entities, and sceneLight
     */
    private void renderScene(Window window, Camera camera, Scene scene){

        sceneShader.bind();

        // create projection for render
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(
                FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShader.setUniform("projectionMatrix", projectionMatrix);


        // add camera transforms
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // lighting
        renderLights(viewMatrix, scene.getSceneLight(), sceneShader);

        sceneShader.setUniform("texture_sampler", 0);

        for(Entity e: scene.getEntities()){

            // null checking on entity and mesh
            if(e == null){continue;}
            if(e.getMesh() == null){continue;}

            // model view matrix for this mesh
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(e, viewMatrix);
            sceneShader.setUniform("modelViewMatrix", modelViewMatrix);

            e.getMesh().render(sceneShader);
        }

        sceneShader.unbind();
    }

    private void renderTerrain(Window window, Camera camera, Scene scene){
        terrainShader.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(
                FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        terrainShader.setUniform("projectionMatrix", projectionMatrix);

        // add camera transforms
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // lighting
        renderLights(viewMatrix, scene.getSceneLight(), terrainShader);

        terrainShader.setUniform("texture_sampler_grass", scene.getTerrain().getGrass().getId());
        terrainShader.setUniform("texture_sampler_stone", scene.getTerrain().getStone().getId());

        scene.getTerrain().render(terrainShader, transformation, viewMatrix);

        terrainShader.unbind();
    }

    private void renderSkyBox(Window window, Camera camera, Scene scene){
        skyBoxShader.bind();

        skyBoxShader.setUniform("texture_sampler", 0);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(
            FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        skyBoxShader.setUniform("projectionMatrix", projectionMatrix);
        SkyBox skyBox = scene.getSkyBox();
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(skyBox, viewMatrix);
        skyBoxShader.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShader.setUniform("ambientLight", scene.getSceneLight().getAmbientLight());

        scene.getSkyBox().getMesh().render();

        skyBoxShader.unbind();
    }

    /*
        Combines all light render methods to one call
     */
    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight, ShaderUtil shader){

        shader.setUniform("ambientLight", sceneLight.getAmbientLight());
        shader.setUniform("specularPower", specularPower);

        PointLight[] pointLights = sceneLight.getPointLights();
        int numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++){
            renderPointLight(viewMatrix, shader, pointLights[i], i);
        }

        SpotLight[] spotLights = sceneLight.getSpotLights();
        numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++){
            renderSpotLight(viewMatrix, shader, spotLights[i], i);
        }

        renderDirectionalLight(viewMatrix, shader, sceneLight.getSun().getLight());
    }

    /*
        SpotLight
     */
    private void renderSpotLight(Matrix4f viewMatrix, ShaderUtil shader, SpotLight light, int index) {
        SpotLight currSpotLight = new SpotLight(light);
        Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
        dir.mul(viewMatrix);
        currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));

        Vector3f spotLightPos = currSpotLight.getPointLight().getPosition();
        Vector4f auxSpot = new Vector4f(spotLightPos, 1);
        auxSpot.mul(viewMatrix);
        spotLightPos.x = auxSpot.x;
        spotLightPos.y = auxSpot.y;
        spotLightPos.z = auxSpot.z;

        shader.setUniform("spotLights", currSpotLight, index);
    }

    /*
        PointLight
     */
    private void renderPointLight(Matrix4f viewMatrix, ShaderUtil shader, PointLight light, int index){
        // Get a copy of the point light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;

        shader.setUniform("pointLights", currPointLight, index);
    }

    /*
        Directional Light
     */
    private void renderDirectionalLight(Matrix4f viewMatrix, ShaderUtil shader, DirectionalLight light){
        DirectionalLight dcopy = new DirectionalLight(light);
        Vector4f direction = new Vector4f(dcopy.getDirection(),0);
        direction.mul(viewMatrix);
        dcopy.setDirection(new Vector3f(direction.x, direction.y, direction.z));

        shader.setUniform("directionalLight", dcopy);
    }
}
