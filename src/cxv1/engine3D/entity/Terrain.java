package cxv1.engine3D.entity;

import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.draw.mesh.HeightMapMesh;
import cxv1.engine3D.util.ShaderUtil;
import cxv1.engine3D.util.loaders.TextureLoader;
import cxv1.engine3D.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Terrain {

    private final Entity[] entities;
    private int blocksPerRow;
    Texture grass, stone;
    private HeightMapMesh og;

    public Terrain(int blocksPerRow, float scale, float minY, float maxY,
           String heightMapFile, String grassTexture, String stoneTexture, int textInc) throws Exception {
        this.blocksPerRow = blocksPerRow;
        entities = new Entity[blocksPerRow*blocksPerRow];

        og = new HeightMapMesh(minY, maxY, heightMapFile, textInc);

        grass = TextureLoader.loadTexture(grassTexture);
        stone = TextureLoader.loadTexture(stoneTexture);

        for(int row = 0; row < blocksPerRow; row++){
            for(int col = 0; col < blocksPerRow; col++){
                float xDisplacement = (col - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getZLength();

                Entity terrainBlock = new StaticEntity(og.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPos(xDisplacement, 0, zDisplacement);
                entities[row*blocksPerRow+col] = terrainBlock;
            }
        }
    }

    public void render(ShaderUtil terrainShader, Transformation transformation, Matrix4f viewMatrix){

        for(int row = 0; row < blocksPerRow; row++){
            for(int col = 0; col < blocksPerRow; col++) {
                Matrix4f modelViewMatrix = transformation.getModelViewMatrix(entities[row*blocksPerRow+col], viewMatrix);
                terrainShader.setUniform("modelViewMatrix", modelViewMatrix);
                entities[row*blocksPerRow+col].getMesh().render();
            }
        }
    }

    public float getHeight(Vector3f pos){

        return og.getHeight(pos);
    }

    public Entity[] getEntities(){
        return entities;
    }

    public Texture getGrass() {
        return grass;
    }

    public void setGrass(Texture grass) {
        this.grass = grass;
    }

    public Texture getStone() {
        return stone;
    }

    public void setStone(Texture stone) {
        this.stone = stone;
    }
}