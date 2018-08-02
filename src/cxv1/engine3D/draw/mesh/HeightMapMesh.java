package cxv1.engine3D.draw.mesh;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.draw.MaterialManager;
import cxv1.engine3D.util.Utils;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class HeightMapMesh {

    private static final int MAX_COLOR = 255*255*255;
    private static final float STARTX = -.05f;
    private static final float STARTZ = -.1f;
    private float minY, maxY;
    private float[][] terrainHeight;
    private int width, height;

    private float incx, incz;

    private Mesh3D mesh;

    public HeightMapMesh (float minY, float maxY, String heightMapFile,
                  int textInc) throws Exception{

        this.maxY = maxY;
        this.minY = minY;

        ByteBuffer buf = getMap(heightMapFile);

        incx = getXLength()/(width-1);
        incz = getZLength()/(height-1);

        terrainHeight = new float[height][width];

        List<Float> posList = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){

                //System.out.println(row + " " + col);

                float elevation = getHeight(col, row, width, buf);

                posList.add(STARTX + col*incx);
                posList.add(elevation);
                posList.add(STARTZ + row*incz);

                terrainHeight[col][row] = elevation;

                textureCoords.add((float) textInc * (float) col / (float) width);
                textureCoords.add((float) textInc * (float) row / (float) height);

                if(col < width -1 && row < height -1){
                    indices.add(row*width+col);
                    indices.add((row+1)*width+col);
                    indices.add(row*width+col+1);

                    indices.add(row*width+col+1);
                    indices.add((row+1)*width+col);
                    indices.add((row+1)*width+col+1);

                }
            }
        }

        float[] posArr = Utils.listToArray(posList);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        float[] textCoordsArr = Utils.listToArray(textureCoords);
        float[] normalsArr = calcNormals(posArr, width, height);
        MaterialManager mats = new MaterialManager();
        this.mesh = new Mesh3D("terrrain", posArr, textCoordsArr, indicesArr, normalsArr, mats);
        this.mesh.setMaterial(new Material());

    }

    private ByteBuffer getMap(String hieghtMapFile) throws Exception {

        PNGDecoder decoder = new PNGDecoder(getClass().getResourceAsStream(
                "/res/textures/"+hieghtMapFile));
        this.height = decoder.getHeight();
        this.width = decoder.getWidth();
        ByteBuffer buf = ByteBuffer.allocateDirect(
                4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();
        return buf;
    }

    private float[] calcNormals(float[] posArr, int width, int height){

        Vector3f v0 = new Vector3f();
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();
        Vector3f v3 = new Vector3f();
        Vector3f v4 = new Vector3f();
        Vector3f v12 = new Vector3f();
        Vector3f v23 = new Vector3f();
        Vector3f v34 = new Vector3f();
        Vector3f v41 = new Vector3f();

        List<Float> normals = new ArrayList<>();
        Vector3f normal = new Vector3f();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (row > 0 && row < height - 1 && col > 0 && col < width - 1) {
                    int i0 = row * width * 3 + col * 3;
                    v0.x = posArr[i0];
                    v0.y = posArr[i0 + 1];
                    v0.z = posArr[i0 + 2];

                    int i1 = row * width * 3 + (col - 1) * 3;
                    v1.x = posArr[i1];
                    v1.y = posArr[i1 + 1];
                    v1.z = posArr[i1 + 2];
                    v1 = v1.sub(v0);

                    int i2 = (row + 1) * width * 3 + col * 3;
                    v2.x = posArr[i2];
                    v2.y = posArr[i2 + 1];
                    v2.z = posArr[i2 + 2];
                    v2 = v2.sub(v0);

                    int i3 = (row) * width * 3 + (col + 1) * 3;
                    v3.x = posArr[i3];
                    v3.y = posArr[i3 + 1];
                    v3.z = posArr[i3 + 2];
                    v3 = v3.sub(v0);

                    int i4 = (row - 1) * width * 3 + col * 3;
                    v4.x = posArr[i4];
                    v4.y = posArr[i4 + 1];
                    v4.z = posArr[i4 + 2];
                    v4 = v4.sub(v0);

                    v1.cross(v2, v12);
                    v12.normalize();

                    v2.cross(v3, v23);
                    v23.normalize();

                    v3.cross(v4, v34);
                    v34.normalize();

                    v4.cross(v1, v41);
                    v41.normalize();

                    normal = v12.add(v23).add(v34).add(v41);
                    normal.normalize();
                } else {
                    normal.x = 0;
                    normal.y = 1;
                    normal.z = 0;
                }
                normal.normalize();
                normals.add(normal.x);
                normals.add(normal.y);
                normals.add(normal.z);
            }
        }
        return Utils.listToArray(normals);
    }

    private float getHeight(int x, int z, int width, ByteBuffer buffer){
        byte r = buffer.get(x*4+0+z*4*width);
        byte g = buffer.get(x*4+1+z*4*width);
        byte b = buffer.get(x*4+2+z*4*width);
        byte a = buffer.get(x*4+3+z*4*width);

        int argb = ((0xFF & a) << 24 | ((0xFF & r) << 16) |
                (0xFF & g) << 8) | (0xFF & b);
        return this.minY + Math.abs(this.maxY - this.minY) * ((float) argb/ (float) MAX_COLOR);
    }

    public float getHeight(Vector3f pos){
        Vector3f location = new Vector3f(pos);

        location.mul(0.001f);

        float x = ((location.x - STARTX)/incx)%width;
        float z = ((location.z - STARTZ)/incz)%height;

        while(x<0){
            x+=width;
        }
        while(z<0){
            z+=height;
        }

        // linear interpolation

        int lowX = (int)x;
        int highX = lowX+1;
        int lowZ = (int)z;
        int highZ = lowZ+1;

        if(highX>=width||highZ>=height){
            return terrainHeight[lowX][lowZ];
        }

        float r1 = ((highX-x)*terrainHeight[lowX][lowZ])+((x-lowX)*terrainHeight[highX][lowZ]);
        float r2 = ((highX-x)*terrainHeight[lowX][highZ])+((x-lowX)*terrainHeight[highX][highZ]);

        return (((highZ-z)*r1)+((z-lowZ)*r2));
    }

    public Mesh3D getMesh(){
        return mesh;
    }

    public static float getXLength(){
        return Math.abs(-STARTX*2);
    }

    public static float getZLength(){
        return Math.abs(-STARTZ*2);
    }

    public float getIncx() {
        return incx;
    }

    public void setIncx(float incx) {
        this.incx = incx;
    }

    public float getIncz() {
        return incz;
    }

    public void setIncz(float incz) {
        this.incz = incz;
    }

    public float[][] getTerrainHeight(){
        return terrainHeight;
    }
}
