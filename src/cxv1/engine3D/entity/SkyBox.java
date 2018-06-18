package cxv1.engine3D.entity;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.draw.mesh.Mesh3D;
import cxv1.engine3D.util.loaders.OBJLoader;

public class SkyBox extends StaticEntity {

    public SkyBox(String modelFile, String textureFile) throws Exception {
        super();
        Mesh3D skyBoxMesh = OBJLoader.loadMesh("skyMesh", modelFile);
        Texture skyBoxTexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0.0f));
        setMesh3D(skyBoxMesh);
        setPos(0,0,0);
    }
}
