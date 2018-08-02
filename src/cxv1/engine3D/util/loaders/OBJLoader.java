package cxv1.engine3D.util.loaders;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.MaterialManager;
import cxv1.engine3D.draw.mesh.Mesh3D;
import cxv1.engine3D.util.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJLoader {

    /*
        Class for loading in .obj files
        #TODO
     */

    public static Mesh3D loadMesh(String id, String filename) throws Exception {
        List<String> meshLines;
        try {
            meshLines = Utils.readAllLines("res/models/" + filename);
        }   catch(Exception e){
            System.err.println("Mesh3D " + filename + " not found");
            return null;
        }
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        MaterialManager materialManager = new MaterialManager();
        Map<String, Material> parsedMaterials = new HashMap<>();
        boolean useMaterial = false;
        int faceIndex = 0;

        for(String line: meshLines){
            String[] tokens = line.split("\\s+");

            switch(tokens[0]){

                case"mtllib": // parse materials from file
                    if(!useMaterial) {
                        useMaterial = true;
                        parsedMaterials = MTLLoader.loadMaterials(tokens[1]);

                        if(parsedMaterials == null){
                            materialManager = MaterialManager.generateDefaultMaterial();
                            useMaterial = false;
                        }
                    }
                    break;

                case"usemtl": // map material to current face index
                    if(useMaterial) {
                        materialManager.setMaterial(faceIndex,
                                parsedMaterials.get(tokens[1]));
                    }
                    break;

                case"v": // geometrix vertex
                    Vector3f vec3 = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    vertices.add(vec3);
                    break;

                case"vt": // texture coord
                    Vector2f vec2 = new Vector2f(
                            Float.parseFloat(tokens[1])-1,
                            Float.parseFloat(tokens[2])-1);
                    textures.add(vec2);
                    break;

                case"vn": // vertex normal
                    Vector3f vec3Norm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                    normals.add(vec3Norm);
                    break;

                case "f": // faces

                    Face tri_face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(tri_face);
                    faceIndex++;

                    if(tokens.length == 5){ // quad -> tri conversion
                        Face quad_patch = new Face(tokens[1], tokens[3], tokens[4]);
                        faces.add(quad_patch);
                        faceIndex++;
                    }

                    break;

                default:
                    break;

            }
        }
        if(materialManager.getMaterialCount()==0){
            materialManager.setMaterial(0, new Material());
        }

        return reorderLists(id, vertices, textures, normals, faces, materialManager);
    }



    // Convert the lists into arrays usable by the mesh constructor, then return the mesh
    private static Mesh3D reorderLists(String id, List<Vector3f> posList, List<Vector2f> textCoordList,
                                       List<Vector3f> normList, List<Face> facesList, MaterialManager materialManager){

        List<Integer> indices = new ArrayList<>();

        // import position vectors into array
        float[] posArr = new float[posList.size()*3]; // 3 floats per vertex
        for(int i = 0; i<posList.size(); i++){
            posArr[i*3] = posList.get(i).x;
            posArr[i*3+1] = posList.get(i).y;
            posArr[i*3+2] = posList.get(i).z;
        }

        // declare norms and texture
        float[] texCoordArr = new float[posList.size()*2];
        float[] normArr = new float[posList.size()*3];

        // use face data to construct lists
        for(Face face: facesList){
            IndexGroup[] faceVertexIndices = face.getFaceVertexIndices();
            for(IndexGroup idx: faceVertexIndices){
                processFaceVertex(idx, textCoordList, normList,
                        indices, texCoordArr, normArr);
            }
        }

        // finally, convert indices list to array
        int [] indicesArr = new int[indices.size()];
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        return new Mesh3D(id, posArr, texCoordArr, indicesArr, normArr, materialManager);
    }

    private static void processFaceVertex(IndexGroup indices,
                        List<Vector2f> textureCoordList, List<Vector3f> normList,
                        List<Integer> indicesList, float[] textureCoordArr, float[] normArr){

        int posIndex = indices.indexPos;
        indicesList.add(posIndex);

        if(indices.indexTextCoord >= 0){
            Vector2f textCoord = textureCoordList.get(indices.indexTextCoord);
            textureCoordArr[posIndex*2] = textCoord.x;
            textureCoordArr[posIndex*2+1] = 1 - textCoord.y;
        }
        if(indices.indexVecNorm >= 0){
            Vector3f vecNorm = normList.get(indices.indexVecNorm);
            normArr[posIndex*3] = vecNorm.x;
            normArr[posIndex*3+1] = vecNorm.y;
            normArr[posIndex*3+2] = vecNorm.z;
        }
    }

    protected static class Face{

        private IndexGroup[] indexGroup;

        public Face(String ind1, String ind2, String ind3){
            indexGroup = new IndexGroup[3];
            indexGroup[0] = parseIndex(ind1);
            indexGroup[1] = parseIndex(ind2);
            indexGroup[2] = parseIndex(ind3);
        }

        private IndexGroup parseIndex(String line){
            IndexGroup idx = new IndexGroup();

            String[] tokens = line.split("/");
            int length = tokens.length;
            idx.indexPos = Integer.parseInt(tokens[0])-1;

            if(length > 1){
                String textCoord = tokens[1];
                idx.indexTextCoord = textCoord.length() > 0 ?
                        Integer.parseInt(textCoord)-1: IndexGroup.NO_VAL;
                if(length > 2){
                    idx.indexVecNorm = Integer.parseInt(tokens[2])-1;
                }
            }
            return idx;
        }

        public IndexGroup[] getFaceVertexIndices(){
            return indexGroup;
        }



    }

    protected static class IndexGroup {

        public static final int NO_VAL = -1;
        public int indexPos, indexTextCoord, indexVecNorm;

        public IndexGroup(){
            indexPos = NO_VAL;
            indexTextCoord = NO_VAL;
            indexVecNorm = NO_VAL;
        }
    }


}
