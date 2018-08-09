package v1.engine.draw;

import v1.engine.draw.mesh.MaterialParameter;
import v1.engine.draw.mesh.MaterialParameters;
import org.joml.Vector4f;

public class Material {

    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private String name;
    private int illuminationModel = 2;
    private MaterialParameter parameters[] = new MaterialParameter[MaterialParameter.PARAMETER_TYPES];

    public Material(String _name) {
        this.name = _name;
    }

    public MaterialParameter getParameter(int parameterType) {

        if (parameterType > MaterialParameter.PARAMETER_TYPES - 1) {
            System.err.println("Material parameter out of bounds");
            return null;
        }
        if (parameters[parameterType] == null) {
            parameters[parameterType] = new MaterialParameter(parameterType);
        }
        return parameters[parameterType];
    }

    public void setDefault(int parameterType){
        parameters[parameterType] = MaterialParameters.generateDefault(parameterType);
    }

    public String getName(){
        return name;
    }

    public void setIlluminationModel(int _i){
        illuminationModel = _i;
    }
    public int getIlluminationModel(){
        return illuminationModel;
    }

}