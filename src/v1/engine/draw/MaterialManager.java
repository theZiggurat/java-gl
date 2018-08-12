package v1.engine.draw;

import v1.engine.draw.mesh.MaterialParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// aids in use of splitting meshes for seperate buffer
// used for .obj files with more than one material
public class MaterialManager {

    int totalFaces;

    private static int DEFAULT_ID = 0;

    // Holds index of tri
    private List<Integer> vboIndexList;

    // Maps the indexes from vboIndexList to materials
    private Map<Integer, Material> materialMap;

    public MaterialManager(){
        vboIndexList = new ArrayList<>();
        materialMap = new HashMap<>();
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
        materialMap.put(indexStart, material);
    }

    public static Material generateDefaultMaterial(){
        Material ret = new Material("default_"+String.valueOf(DEFAULT_ID++));
        for(int i = 0; i<MaterialParameter.PARAMETER_TYPES-1; i++){
            ret.setDefault(i);
        }
        return ret;
    }

}