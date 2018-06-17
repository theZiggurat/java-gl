package cxv1.engine3D;

import cxv1.engine3D.draw.Material;
import cxv1.engine3D.util.loaders.MTLLoader;

import java.util.Map;

public class TestMain {

    public static void main(String [] args) throws Exception{
        Map<String, Material> map = MTLLoader.loadMaterials("cube.mtl");
    }
}
