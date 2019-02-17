package v2.engine.gldata.vbo;

import v2.engine.utils.AssimpLoader;

public class Meshs {

    /** TODO:
     *         Rework into primitive generator
     */


    public final static Mesh3D sphere = AssimpLoader.loadMeshGroup("res/models/sphere.obj").get(0);
    public final static Mesh3D quad = AssimpLoader.loadMeshGroup("res/models/quad.obj").get(0);
}
