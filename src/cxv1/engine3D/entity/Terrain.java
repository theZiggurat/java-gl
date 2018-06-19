package cxv1.engine3D.entity;

import cxv1.engine3D.draw.mesh.HeightMapMesh;

public class Terrain {

    private final Entity[] entities;

    public Terrain(int blocksPerRow, float scale, float minY, float maxY,
           String heightMapFile, String textureFile, int textInc) throws Exception {

        entities = new Entity[blocksPerRow*blocksPerRow];

        HeightMapMesh heightMapMesh = new HeightMapMesh(minY, maxY, heightMapFile, textureFile, textInc);

        for(int row = 0; row < blocksPerRow; row++){
            for(int col = 0; col < blocksPerRow; col++){
                float xDisplacement = (col - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) blocksPerRow - 1)/ (float)2)*scale*HeightMapMesh.getZLength();

                Entity terrainBlock = new StaticEntity(heightMapMesh.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPos(xDisplacement, 0, zDisplacement);
                entities[row*blocksPerRow+col] = terrainBlock;
            }
        }
    }

    public Entity[] getEntities(){
        return entities;
    }
}