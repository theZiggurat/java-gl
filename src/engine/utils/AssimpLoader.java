package engine.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;
import engine.glapi.vbo.Mesh3D;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.assimp.Assimp.aiProcess_GenSmoothNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;

public class AssimpLoader {
    public static ArrayList<Mesh3D> loadMeshGroup(String filename){

        ArrayList<Mesh3D> ret = new ArrayList<>();

        AIScene scene = Assimp.aiImportFile(Utils.absolutePath(filename),
                aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices);

        for(int i = 0; i<scene.mNumMeshes(); i++){
            Mesh3D mesh = ai_mesh_convert(AIMesh.create(scene.mMeshes().get(i)));
            if(mesh!=null)
                ret.add(mesh);
        }

        return ret;
    }

    public static Mesh3D loadMesh(String filename){
        System.out.println("Loading mesh: " + filename);

        AIScene scene = Assimp.aiImportFile(Utils.absolutePath(filename),
                aiProcess_GenSmoothNormals);

        if(scene == null){
            System.err.println(Assimp.aiGetErrorString());
            System.exit(1);
        }

        if(scene.mNumMeshes()!=1){
            System.err.println("Too many meshes for loadMesh() to generate! Use loadMeshGroup() instead");
        }

        Mesh3D mesh = ai_mesh_convert(AIMesh.create(scene.mMeshes().get(0)));
        System.out.println("Finished loading: " + filename);
        return mesh;
    }

    private static Mesh3D ai_mesh_convert(AIMesh mesh){

        if(mesh==null)
            return null;

        Mesh3D ret = new Mesh3D();

        // POSITION //
        AIVector3D.Buffer aiVert = mesh.mVertices();
        while(aiVert.remaining()>0){
            AIVector3D pos = aiVert.get();
            ret.getPositions().add(new Vector3f(pos.x(), pos.y(), pos.z()));
        }

        // UV //
        AIVector3D.Buffer aiUV = mesh.mTextureCoords(0);
        if(aiUV != null){
            while(aiUV.remaining()>0){
                AIVector3D uv = aiUV.get();
                ret.getUVs().add(new Vector2f(uv.x(), uv.y()));
            }
        }

        AIVector3D.Buffer aiNorm = mesh.mNormals();
        if(aiNorm != null){
            while(aiNorm.remaining()>0){
                AIVector3D norm = aiNorm.get();
                ret.getNormals().add(new Vector3f(norm.x(), norm.y(), norm.z()));
            }
        }

        AIFace.Buffer aifaces = mesh.mFaces();
        while (aifaces.remaining() > 0) {
            AIFace aiface = aifaces.get();

            if (aiface.mNumIndices() == 3) {
                IntBuffer indicesBuffer = aiface.mIndices();
                ret.getIndices().add(indicesBuffer.get(0));
                ret.getIndices().add(indicesBuffer.get(1));
                ret.getIndices().add(indicesBuffer.get(2));
            }
            if (aiface.mNumIndices() == 4) {
                IntBuffer indicesBuffer = aiface.mIndices();
                ret.getIndices().add(indicesBuffer.get(0));
                ret.getIndices().add(indicesBuffer.get(1));
                ret.getIndices().add(indicesBuffer.get(2));
                ret.getIndices().add(indicesBuffer.get(0));
                ret.getIndices().add(indicesBuffer.get(1));
                ret.getIndices().add(indicesBuffer.get(3));
                ret.getIndices().add(indicesBuffer.get(1));
                ret.getIndices().add(indicesBuffer.get(2));
                ret.getIndices().add(indicesBuffer.get(3));
            }

        }

        ret.bind();
        return ret;

    }
}
