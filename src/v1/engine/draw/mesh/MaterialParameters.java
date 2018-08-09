package v1.engine.draw.mesh;

import org.joml.Vector4f;

public class MaterialParameters {
    
    public static MaterialParameter generateDefault(int _parameterType){
        
        MaterialParameter ret = new MaterialParameter(_parameterType);
        
        switch (_parameterType){
            case MaterialParameter.DIFFUSE:
                ret.setData(new Vector4f(1,1,1,1));
                break;
            case MaterialParameter.AMBIENT:
                ret.setData(new Vector4f(.5f, .5f, .5f, 1));
                break;
            case MaterialParameter.SPECULAR:
                ret.setData(new Vector4f(1,1,1,1));
                break;
            case MaterialParameter.EMISSIVE:
                ret.setData(1f);
                break;
            case MaterialParameter.NORMAL:
                ret.setData(new Vector4f(0,0,0,0));
                break;
            case MaterialParameter.SPECULAR_EXPONENT:
                ret.setData(1f);
                break;
            case MaterialParameter.OPTICAL_DENSITY:
                ret.setData(1f);
                break;
            case MaterialParameter.DISSOLVE:
                ret.setData(1f);
                break;
            default:
                return null;
                
        }

        return ret;
    }
}
