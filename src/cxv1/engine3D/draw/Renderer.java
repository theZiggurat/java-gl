package cxv1.engine3D.draw;

import cxv1.engine3D.draw.lighting.DirectionalLight;
import cxv1.engine3D.draw.lighting.SceneLight;
import cxv1.engine3D.draw.lighting.SpotLight;
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


public class Renderer {

    public static final int MAX_POINT_LIGHT = 5;
    public static final int MAX_SPOT_LIGHT = 5;

    private float FOV = (float) Math.toRadians(90); // default val
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR  = 1000.0f;
    private Transformation transformation;
    private float specularPower;

    private ShaderUtil sceneShader;
    private ShaderUtil hudShader;

    // PUBLIC METHODS AND CONSTRUCTORS ----------------------------------------------------//

    public Renderer(){
        transformation = new Transformation();
        specularPower = 10f;
    }

    /*
        Initialize shaders
     */
    public void init() throws Exception {
        setupSceneShader();
        setupHudShader();
    }

    /*
        Cleanup shaders
     */
    public void cleanup(){
        if(sceneShader != null){ sceneShader.cleanup(); }
        if(hudShader != null){ hudShader.cleanup(); }

        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render(Window window, Camera camera, Entity[] entities, SceneLight sceneLight, Hud HUD){
        clear();

        // check for window resize
        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // render scene and hud
        renderScene(window, camera, entities, sceneLight);
        renderHud(window, HUD);
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

    /*
        HUD Shader
     */
    private void setupHudShader() throws Exception {
        hudShader = new ShaderUtil();
        hudShader.createVertexShader(Utils.loadResource("/res/shaders/text_vert.vs"));
        hudShader.createFragmentShader(Utils.loadResource("/res/shaders/text_frag.fs"));
        hudShader.link();

        hudShader.createUniform("orthoMatrix");
        hudShader.createUniform("color");
    }

    // RENDER METHODS ------------------------------------------------------------------//

    /*
        Combines all renderScene methods into one call
        @param window, camera, entities, and sceneLight
     */
    private void renderScene(Window window, Camera camera, Entity[] entities, SceneLight sceneLight){
        sceneShader.bind();

        // create projection for render
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(
                FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        sceneShader.setUniform("projectionMatrix", projectionMatrix);

        // add camera transforms
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // lighting
        renderLights(viewMatrix, sceneLight);


        sceneShader.setUniform("texture_sampler", 0);
        for(Entity e: entities){

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

    /*
        Combines all light render methods to one call
     */
    private void renderLights(Matrix4f viewMatrix, SceneLight sceneLight){

        sceneShader.setUniform("ambientLight", sceneLight.getAmbientLight());
        sceneShader.setUniform("specularPower", specularPower);

        PointLight[] pointLights = sceneLight.getPointLights();
        int numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++){
            renderPointLight(viewMatrix, pointLights[i], i);
        }

        SpotLight[] spotLights = sceneLight.getSpotLights();
        numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++){
            renderSpotLight(viewMatrix, spotLights[i], i);
        }

        renderDirectionalLight(viewMatrix, sceneLight.getSun().getLight());
    }

    /*
        SpotLight
     */
    private void renderSpotLight(Matrix4f viewMatrix, SpotLight light, int index) {
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

        sceneShader.setUniform("spotLights", currSpotLight, index);
    }

    /*
        PointLight
     */
    private void renderPointLight(Matrix4f viewMatrix, PointLight light, int index){
        // Get a copy of the point light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(light);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;

        sceneShader.setUniform("pointLights", currPointLight, index);
    }

    /*
        Directional Light
     */
    private void renderDirectionalLight(Matrix4f viewMatrix, DirectionalLight light){
        DirectionalLight dcopy = new DirectionalLight(light);
        Vector4f direction = new Vector4f(dcopy.getDirection(),0);
        direction.mul(viewMatrix);
        dcopy.setDirection(new Vector3f(direction.x, direction.y, direction.z));

        sceneShader.setUniform("directionalLight", dcopy);
    }

    private void renderHud(Window window, Hud HUD){
        hudShader.bind();
        Matrix4f ortho = transformation.getOrthoProjectionMatrix(
                0, window.getWidth(), window.getHeight(), 0);

        for(Entity entity: HUD.getItems()){
            Mesh mesh = entity.getMesh();

            Matrix4f projModelMatrix = transformation.getOrthoModelMatrix(entity, ortho);
            hudShader.setUniform("orthoMatrix", ortho);
            hudShader.setUniform("color", entity.getMesh().
                    getMaterial(0).getAmbientColor());

            mesh.render(sceneShader);
        }
        hudShader.unbind();
    }
}
