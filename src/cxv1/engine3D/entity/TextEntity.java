package cxv1.engine3D.entity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.Mesh;
import cxv1.engine3D.draw.Texture;
import cxv1.engine3D.util.Utils;


public class TextEntity extends FixedObjectEntity{

    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;
    private String text;
    private final int cols, rows;

    public TextEntity(String text, String fontFile, int cols, int rows) throws Exception {
        super();
        this.text = text;
        this.cols = cols;
        this.rows = rows;
        Texture texture = new Texture(fontFile);
        this.setMesh(buildText(texture, cols, rows));
    }

    private Mesh buildText(Texture texture, int cols, int rows){
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();

        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();

        float tileWidth = (float) texture.getWidth() / (float) cols;
        float tileHieght = (float) texture.getHeight() / (float) rows;

        for(int i = 0; i< numChars; i++){
            byte curr = chars[i];
            int col = (curr-32) % cols;
            int row = (curr-32) % rows;

            // top left vertex
            positions.add((float) i*tileWidth);
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) col / (float) cols);
            textCoords.add((float) row / (float) rows);
            indices.add(i*VERTICES_PER_QUAD);

            // bottom left vertex
            positions.add((float) i*tileWidth);
            positions.add(tileHieght);
            positions.add(ZPOS);
            textCoords.add((float) col / (float) cols);
            textCoords.add((float) (row + 1) / (float) rows);
            indices.add(i*VERTICES_PER_QUAD+1);

            // bottom right vertex
            positions.add((float) i*tileWidth + tileWidth);
            positions.add(tileHieght);
            positions.add(ZPOS);
            textCoords.add((float) (col + 1) / (float) cols);
            textCoords.add((float) (row + 1) / (float) rows);
            indices.add(i*VERTICES_PER_QUAD+2);

            // top right vertex
            positions.add((float) i*tileWidth + tileWidth);
            positions.add(0.0f);
            positions.add(ZPOS);
            textCoords.add((float) (col + 1) / (float) cols);
            textCoords.add((float) row / (float) rows);
            indices.add(i*VERTICES_PER_QUAD+3);

            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh("text", posArr, textCoordsArr, indicesArr, normals);
        mesh.setMaterial(new Material(texture));
        return mesh;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.getMesh().deleteBuffers();
        this.setMesh(buildText(texture, cols, rows));
    }



}
