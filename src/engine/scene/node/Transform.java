package engine.scene.node;

import lombok.AccessLevel;
import lombok.Setter;
import org.joml.Vector3f;

public class Transform {

    /**
     * Any class that inherets this class will get the following functions:
     *  * Position, rotation, and scaling in space
     *  * Chainable methods to change place and orientation
     *  * Model and view matrix creation
     */

    /** Data fields **/
    @Setter protected Vector3f translation;
    @Setter protected Vector3f rotation;
    @Setter protected Vector3f scaling;

    public Transform(){
        translation = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
        scaling = new Vector3f(1,1,1);
    }


    /** TRANSLATION **/      /** -------------------------------------------- **/

    public Transform translate(Vector3f translation){
        setTranslation(getTranslation().add(translation));
        return this;
    }

    public Transform translate(float x, float y, float z){
        return translate(new Vector3f(x,y,z));
    }

    public Transform translateTo(Vector3f position){
        translation = new Vector3f(position);
        return this;
    }

    public Transform translateTo(float x, float y, float z){
        return translateTo(new Vector3f(x, y, z));
    }

    public Vector3f getTranslation(){
        return new Vector3f(translation);
    }

    /** ROTATION **/      /** -------------------------------------------- **/

    public Transform rotate(Vector3f rotation){
        setRotation(getRotation().add(rotation));
        return this;
    }

    public Transform rotate(float i, float j, float k){
        return rotate(new Vector3f(i, j, k));
    }

    public Transform rotateTo(Vector3f f){
        setRotation(new Vector3f(f));
        return this;
    }

    public Transform rotateTo(float i, float j, float k){
        return rotateTo(new Vector3f(i, j, k));
    }
    public Transform rotateAround(Vector3f axis, double degrees){
        // TODO
        return this;
    }

    public Vector3f getRotation(){
        return new Vector3f(rotation);
    }

    /** Scaling **/      /** -------------------------------------------- **/

    public Transform scale(Vector3f scalar){
        setScaling(getScaling().mul(scalar));
        return this;
    }

    public Transform scale(float mx, float my, float mz){
        return scale(new Vector3f(mx, my, mz));
    }

    public Transform scale(float s){
        return scale(s,s,s);
    }

    public Transform scaleTo(Vector3f scaleFactor){
        setScaling(scaleFactor);
        return this;
    }

    public Transform scaleTo(float x, float y, float z){
        return scaleTo(new Vector3f(x, y, z));
    }

    public Transform scaleTo(float s){
        return scaleTo(s,s,s);
    }

    public Vector3f getScaling(){
        return new Vector3f(scaling);
    }

}
