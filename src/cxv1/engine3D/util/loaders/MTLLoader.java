package cxv1.engine3D.util.loaders;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.util.Utils;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cxv1.engine3D.util.loaders.TextureLoader.loadTexture;

public class MTLLoader {

    // Parses materials from .mtl file and returns hashmap:
    //           Material name -> Material object
    public static Map<String, Material> loadMaterials(String filename) throws Exception{
        List<String> materialLines;
        try {
            materialLines = Utils.readAllLines("res/materials/" + filename);
        } catch(Exception e){
            System.err.println("Material " + filename + " not found.");
            return new HashMap<>();
        }
        Map<String, Material> materials = new HashMap<>();
        Material currentMaterial = null;
        String currentMaterialName = null;

        for(String line: materialLines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){

                // Save current material if there is one, and create a new material
                case "newmtl":
                    if(currentMaterial!=null&&currentMaterialName!=null){
                        materials.put(currentMaterialName, currentMaterial);
                    }
                    currentMaterialName = tokens[1];
                    currentMaterial = new Material();
                    break;

                // color textures

                case "Ka": // material ambient
                    currentMaterial.setAmbientColor(new Vector4f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]),
                            1.0f
                    ));
                    break;
                case "Kd": // material diffuse
                    currentMaterial.setDiffuseColor(new Vector4f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]),
                            1.0f
                    ));
                    break;
                case "Ks": // material specular
                    currentMaterial.setSpecularColor(new Vector4f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]),
                            1.0f
                    ));
                    break;

                // scalar textures

                case "Ns":
                    break;
                case "Ni":
                    break;
                case "d":
                    break;
                case "illum":
                    break;
                case "map_Kd": // set texture of material
                    try {
                        Texture texture = loadTexture(tokens[1]);
                        currentMaterial.setTexture(texture);
                    } catch(Exception e){
                        System.err.println("Texture "+tokens[1]+" could not be loaded.");
                    }
                default:
                    break;
            }
        }
        return materials;
    }
}
