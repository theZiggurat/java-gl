package v2.engine.javadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import v2.engine.system.StaticLoader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MeshData {

    @Setter @Getter private float positions[];
    @Setter @Getter private float uv[];
    @Setter @Getter private float normals[];
    @Setter @Getter private int indices[];

    public FloatBuffer posBuffer(){
        return StaticBuffer.floatBuffer(positions);
    }

    public FloatBuffer uvBuffer(){
        return StaticBuffer.floatBuffer(uv);
    }

    public FloatBuffer normalBuffer(){
        return StaticBuffer.floatBuffer(normals);
    }

    public IntBuffer indexBuffer(){
        return StaticBuffer.intBuffer(indices);
    }



    public static MeshData loadMesh(String filename) {
        List<String> meshLines;
        try {
            meshLines = StaticLoader.readAllLines(filename);
        }   catch(Exception e){
            System.err.println("Mesh " + filename + " not found");
            return null;
        }
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for(String line: meshLines){
            String[] tokens = line.split("\\s+");

            switch(tokens[0]){

                case"mtllib":
                    break;

                case"usemtl":
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
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
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

                    if(tokens.length == 5){ // gui -> tri conversion
                        Face quad_patch = new Face(tokens[1], tokens[3], tokens[4]);
                        faces.add(quad_patch);
                    }

                    break;

                default:
                    break;

            }
        }
       try {
           return reorderLists(vertices, textures, normals, faces);
       } finally {
            System.out.println("Model loaded: " + filename);
       }
    }


    /**
     * From face data in obj file, constructs indices list that will comprise of
     * the javadata.
     * @param posList list of 3d coords
     * @param textCoordList list of uv coords
     * @param normList list of normal vectors
     * @param facesList list of face objects (3 indices)
     * @return
     */
    private static MeshData reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList,
                                       List<Vector3f> normList, List<Face> facesList){

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
        int [] indicesArr;
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return new MeshData(posArr, texCoordArr, normArr, indicesArr);
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
