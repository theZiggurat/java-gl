package v1.engine.entity;

import v1.engine.draw.Texture;
import v1.engine.draw.mesh.Mesh3D;
import v1.engine.util.loaders.ImageLoader;
import v1.engine.util.loaders.OBJLoader;

public class SkyBox extends StaticEntity {

    public SkyBox(String modelFile, String textureFile) throws Exception {
        super();
        Mesh3D skyBoxMesh = OBJLoader.loadMesh("skyMesh", modelFile);
        Texture skyBoxTexture = ImageLoader.loadImage(textureFile);
        //skyBoxMesh.setMaterial(new Material());
        setMesh3D(skyBoxMesh);
        setPos(0,0,0);
    }
}
