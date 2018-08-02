package cxv1.engine3D.util.loaders;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.lighting.PointLight;
import cxv1.engine3D.draw.lighting.Sun;
import cxv1.engine3D.draw.mesh.Mesh3D;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.entity.SkyBox;
import cxv1.engine3D.entity.StaticEntity;
import cxv1.engine3D.entity.Terrain;
import cxv1.engine3D.enviorment.Scene;
import cxv1.engine3D.util.Utils;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneLoader {

    public static Scene loadScene(String filename){

        List<String> sceneLines;

        try {
            sceneLines = Utils.readAllLines("res/levels/" + filename);
        } catch(Exception e){
            System.err.println("Scene " + filename + " not found.");
            return new Scene();
        }

        Scene scene = new Scene();

        for(String line: sceneLines){
            String[] tokens = line.split("\\s+");

            switch (tokens[0]){
                case "#": // comment
                    break;
                case "name": // set name
                    scene.setName(tokens[1]);
                    break;
                case "obj":

                    Mesh3D mesh3D;
                    try{
                        mesh3D = OBJLoader.loadMesh(tokens[1], tokens[2]);
                    } catch (Exception e){
                        System.err.println("Scene: Could not parse" + tokens[2]);
                        break;
                    }

                    StaticEntity entity = new StaticEntity(mesh3D);

                    entity.setPos(  Float.parseFloat(tokens[3]),
                                    Float.parseFloat(tokens[4]),
                                    Float.parseFloat(tokens[5]));

                    entity.setScale(Float.parseFloat(tokens[6]));

                    scene.getEntities().put(tokens[1], entity);

                    break;

                case "skybox":

                    SkyBox skyBox;
                    try{
                        skyBox = new SkyBox(tokens[1], tokens[2]);
                    } catch (Exception e){
                        System.err.println("Scene: Could not parse" + tokens[2]);
                        break;
                    }
                    skyBox.setScale(Float.parseFloat(tokens[3]));
                    scene.setSkyBox(skyBox);
                    break;
                case "sun":

                    Sun sun = new Sun(Float.parseFloat(tokens[1]));
                    sun.setIntensity(Float.parseFloat(tokens[2]));
                    scene.getSceneLight().setSun(sun);

                    break;
                case "point":

                    PointLight light = new PointLight(
                            new Vector3f(Float.parseFloat(tokens[1]),
                                         Float.parseFloat(tokens[2]),
                                         Float.parseFloat(tokens[3])),
                            new Vector3f(Float.parseFloat(tokens[4]),
                                         Float.parseFloat(tokens[5]),
                                         Float.parseFloat(tokens[6])),
                                         Float.parseFloat(tokens[7]),
                            new PointLight.Attenuation(
                                        Float.parseFloat(tokens[8]),
                                        Float.parseFloat(tokens[9]),
                                        Float.parseFloat(tokens[10])));

                    scene.getSceneLight().addPointLight(light);

                    break;
                case "cone":
                    break;
                case "terr":
                    Terrain terrain = new Terrain();
                    try {
                        terrain.init(
                                Integer.parseInt(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3]),
                                Float.parseFloat(tokens[4]),
                                tokens[5],
                                tokens[6],
                                tokens[7],
                                Integer.parseInt(tokens[8]));
                    } catch(Exception e){
                        System.err.println("Scene: Could not parse terrain");
                        e.printStackTrace();
                        break;
                    }

                    scene.setTerrain(terrain);

                    break;
                case "ambient":
                    scene.getSceneLight().setAmbientLight(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]))
                    );
            }
        }
        return scene;
    }

}
