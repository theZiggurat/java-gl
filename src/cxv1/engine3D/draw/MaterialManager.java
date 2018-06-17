package cxv1.engine3D.draw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// aids in use of splitting meshes for seperate render
// used for .obj files with more than one material
public class MaterialManager {

    int totalFaces;

    // Holds index of tri
    private List<Integer> vboIndexList;

    // Maps the indexes from vboIndexList to materials
    private Map<Integer, Material> materialMap;

    public MaterialManager(){
        vboIndexList = new ArrayList<>();
        materialMap = new HashMap<>();
    }

    // Creates material manager with one material
    // texture & default values for the rest of the lighting components

    // Used in HUD rendering
    public static MaterialManager generateDefaultMaterial(Texture texture){
        MaterialManager ret = new MaterialManager();
        ret.setMaterial(0, new Material(texture));
        return ret;
    }

    // Creates material manager with one material
    // all material components default

    // Used in MTLLoader exception handling
    public static MaterialManager generateDefaultMaterial(){
        MaterialManager ret = new MaterialManager();
        ret.setMaterial(0, new Material());
        return ret;
    }

    public int getMaterialCount(){
        return vboIndexList.size();
    }

    public int getTriIndex(int index){
        if(index >= getMaterialCount()){
            return -1;
        }
        return vboIndexList.get(index);
    }

    public Material getMaterial(int index){
        return materialMap.get(vboIndexList.get(index));
    }

    public void setMaterial(int indexStart, Material material){
        vboIndexList.add(indexStart);
        if(material == null){
            materialMap.put(indexStart, new Material());
        }
        else {
            materialMap.put(indexStart, material);
        }
    }

}