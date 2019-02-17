package v2.engine.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

public class Transform <T extends Transform> {

    /**
     * Any class that inherets this class will get the following functions:
     *  * Position, rotation, and scaling in space
     *  * Chainable methods to change place and orientation
     *  * Model and view matrix creation
     */

    /** Data fields **/
    @Setter(AccessLevel.PROTECTED) protected Vector3f translation;
    @Setter(AccessLevel.PROTECTED) protected Vector3f rotation;
    @Setter(AccessLevel.PROTECTED) protected Vector3f scaling;

    public Transform(){
        translation = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scaling = new Vector3f(1,1,1);
    }


    /** TRANSLATION **/      /** -------------------------------------------- **/

    public <T extends Transform> T translate(Vector3f translation){
        setTranslation(getTranslation().add(translation));
        return (T) this;
    }

    public <T extends  Transform> T translate(float x, float y, float z){
        return translate(new Vector3f(x,y,z));
    }

    public <T extends Transform> T translateTo(Vector3f position){
        translation = new Vector3f(position);
        return (T) this;
    }

    public <T extends Transform> T translateTo(float x, float y, float z){
        return translateTo(new Vector3f(x, y, z));
    }

    public Vector3f getTranslation(){
        return new Vector3f(translation);
    }

    /** ROTATION **/      /** -------------------------------------------- **/

    public <T extends Transform> T rotate(Vector3f rotation){
        setRotation(getRotation().add(rotation));
        return (T) this;
    }

    public <T extends Transform> T rotate(float i, float j, float k){
        return rotate(new Vector3f(i, j, k));
    }

    public <T extends Transform>T rotateTo(Vector3f f){
        setRotation(new Vector3f(f));
        return (T) this;
    }

    public <T extends Transform>T rotateTo(float i, float j, float k){
        return rotateTo(new Vector3f(i, j, k));
    }
    public <T extends Transform>T rotateAround(Vector3f axis, double degrees){
        return (T) this;
    }

    public Vector3f getRotation(){
        return new Vector3f(rotation);
    }

    /** Scaling **/      /** -------------------------------------------- **/

    public <T extends Transform> T scale(Vector3f scalar){
        setScaling(getScaling().mul(scalar));
        return (T) this;
    }

    public <T extends Transform> T scale(float mx, float my, float mz){
        return scale(new Vector3f(mx, my, mz));
    }

    public <T extends Transform> T scale(float s){
        return scale(s,s,s);
    }

    public <T extends Transform<T>> T scaleTo(Vector3f scaleFactor){
        setScaling(scaleFactor);
        return (T) this;
    }

    public <T extends Transform<T>> T scaleTo(float x, float y, float z){
        return scaleTo(new Vector3f(x, y, z));
    }

    public <T extends Transform<T>> T scaleTo(float s){
        return scaleTo(s,s,s);
    }

    public Vector3f getScaling(){
        return new Vector3f(scaling);
    }

}
