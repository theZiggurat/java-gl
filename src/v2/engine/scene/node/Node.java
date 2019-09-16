 package v2.engine.scene.node;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import v2.engine.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

 public class Node {

    @Getter private int UUID;

    @Getter @Setter private boolean activated = true;
    @Getter @Setter private boolean selected = false;
    @Getter @Setter private boolean hidden = false;

    @Setter private String debugName;

    @Getter @Setter private Node parent;
    @Getter private List<Node> children;

    @Getter public Transform transform;

    public Node() {
        super();
        UUID = Utils.generateNewUUID_3D();
        this.transform = new Transform();
        this.children = new ArrayList<>();
    }

    public Matrix4f getModelMatrix(){
        Matrix4f ret = new Matrix4f();
        Vector3f rotate = getWorldRotation();
        ret.identity().translate(getWorldTranslation())
                .scale(getWorldScaling())
                .rotateX((float)Math.toRadians(-rotate.x))
                .rotateY((float)Math.toRadians(-rotate.y))
                .rotateZ((float)Math.toRadians(-rotate.z));
        return ret;
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public String getName(){
        if(debugName == null)
            return this.getClass().getSimpleName() + "#" + UUID;
        else
            return debugName;

    }

    public void addChildren(Node... children){
        getChildren().addAll(Arrays.asList(children));
        for(Node child: children)
            child.setParent(this);
    }

    public Vector3f getWorldTranslation(){
        if(parent != null) {
            return transform.getTranslation().add(parent.getWorldTranslation());
        } else {
            return transform.getTranslation();
        }
    }

    public Vector3f getWorldRotation(){
        if(parent != null) {
            return transform.getRotation().add( parent.getWorldRotation());
        } else {
            return transform.getRotation();
        }
    }

    public Vector3f getWorldScaling(){
        if(parent != null) {
            return transform.getScaling().mul(parent.getWorldScaling());
        } else {
            return transform.getScaling();
        }
    }

    public ArrayList<Node> collect(){
        ArrayList<Node> ret = new ArrayList<>();
        ret.add(this);
        for(Node child: children)
            ret.addAll(child.collect());
        return ret;
    }

    public void update() {
        for(Node child: children)
            if (child.isActivated()) child.update();
    }

    public void render(RenderType type) {
        for(Node child: children)
            if (child.isActivated()&&!child.isHidden()) child.render(type);
    }

     public void render(RenderType type, Condition condition) {
         for(Node child: children) {
             if (child.isActivated() && condition.isvalid(child))
                 child.render(type, condition);
         }
     }

    public void cleanup() {
        children.forEach(child -> child.cleanup());
    }

    public boolean isActivated(){
        return activated;
    }

    public void activate(){
        activated = true;
    }

    public void deactivate(){
        activated = false;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        for(Node child: children) child.setSelected(selected);
    }

    @FunctionalInterface
    public interface Condition {
        boolean isvalid(Node node);
    }

}