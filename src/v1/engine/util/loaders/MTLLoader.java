package v1.engine.util.loaders;

import v1.engine.draw.Material;
import v1.engine.draw.Texture;
import v1.engine.draw.mesh.MaterialParameter;
import v1.engine.util.Utils;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        for(String line: materialLines){
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){

                // Save current material if there is one, and create a new material
                case "newmtl":
                    if(currentMaterial!=null){
                        materials.put(currentMaterial.getName(), currentMaterial);
                    }
                    currentMaterial = new Material(tokens[1]);
                    break;

                // color textures

                case "Ka": // material ambient
                    currentMaterial.getParameter(MaterialParameter.AMBIENT).setData(
                            new Vector4f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3]),
                                    1.0f
                            ));
                    break;
                case "Kd": // material diffuse
                    currentMaterial.getParameter(MaterialParameter.DIFFUSE).setData(
                            new Vector4f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3]),
                                    1.0f
                    ));
                    break;
                case "Ks": // material specular
                    currentMaterial.getParameter(MaterialParameter.SPECULAR).setData(
                            new Vector4f(
                                    Float.parseFloat(tokens[1]),
                                    Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3]),
                                    1.0f
                            ));
                    break;
                case "Ns": // shiny factor
                    currentMaterial.getParameter(MaterialParameter.SPECULAR_EXPONENT).setData(
                            Float.parseFloat(tokens[1])
                    );
                    break;
                case "Ni": // refraction factor
                    currentMaterial.getParameter(MaterialParameter.OPTICAL_DENSITY).setData(
                            Float.parseFloat(tokens[1])
                    );
                    break;

                case "d": // transparency (float)
                    currentMaterial.getParameter(MaterialParameter.DISSOLVE).setData(
                            Float.parseFloat(tokens[1])
                    );
                    break;

                    // illumination model 1 = flat 2 = specular highlights
                case "illum":
                    break;

                case "map_Ka": // ambient map
                    try {
                        Texture texture = ImageLoader.loadImage(convertToTexturePath(tokens[1]));
                        currentMaterial.getParameter(MaterialParameter.AMBIENT).setData(texture);
                    } catch(Exception e){
                        System.err.println("Texture "+tokens[1]+" could not be loaded.");
                    }

                case "map_Kd": // diffuse map
                    try {
                        Texture texture = ImageLoader.loadImage(convertToTexturePath(tokens[1]));
                        currentMaterial.getParameter(MaterialParameter.DIFFUSE).setData(texture);
                    } catch(Exception e){
                        System.err.println("Texture "+tokens[1]+" could not be loaded.");
                    }
                case "map_Ks": // specular map
                    try {
                        Texture texture = ImageLoader.loadImage(convertToTexturePath(tokens[1]));
                        currentMaterial.getParameter(MaterialParameter.SPECULAR).setData(texture);
                    } catch(Exception e){
                        System.err.println("Texture "+tokens[1]+" could not be loaded.");
                    }
                default:
                    break;
            }
        }
        if(currentMaterial!=null){
            materials.put(currentMaterial.getName(), currentMaterial);
        }
        return materials;
    }

    private static String convertToTexturePath(String oldPath){
        String tokens[] = oldPath.split("\\\\");
        return tokens[tokens.length-1];
    }
}
