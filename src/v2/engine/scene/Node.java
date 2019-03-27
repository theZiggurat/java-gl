 package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import v2.engine.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

 public class Node extends Transform<Node> {

    @Getter private int UUID;

    private boolean activated = true;
    @Getter private boolean selected = false;
    @Setter private String debugName;

    @Getter private boolean localRotation, localTranslation, localScaling;

    @Getter @Setter private Node parent;
    @Getter private List<Node> children;

    public Node() {
        super();
        UUID = Utils.generateNewUUID();
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
        Arrays.stream(children).forEach(e -> e.setParent(this));
    }

    public Vector3f getWorldTranslation(){
        if(parent != null && !localTranslation) {
            return getTranslation().add(parent.getWorldTranslation());
        } else {
            return getTranslation();
        }
    }

    public Vector3f getWorldRotation(){
        if(parent != null && !localRotation) {
            return getRotation().add( parent.getWorldRotation());
        } else {
            return getRotation();
        }
    }

    public Vector3f getWorldScaling(){
        if(parent != null && !localScaling) {
            return getScaling().mul(parent.getWorldScaling());
        } else {
            return getScaling();
        }
    }

    public ArrayList<Node> collect(){
        ArrayList<Node> ret = new ArrayList<>();
        ret.add(this);
        for(Node child: children)
            ret.addAll(child.collect());
        return ret;
    }

    public Stream<Node> stream(){
        return collect().stream();
    }

    public Node localizeRotation(boolean _localize){
        localRotation = _localize;
        return this;
    }

    public Node localizeTranslation(boolean _localize){
        localTranslation = _localize;
        return this;
    }

    public Node localizeScaling(boolean _localize){
        localScaling = _localize;
        return this;
    }


    public void update() {
            children.forEach(child -> {
                if (child.isActivated()) child.update();
            });
    }

    public void render(RenderType type) {
        children.forEach(child -> {
            if (child.isActivated())
                child.render(type);
        });
    }

     public void render(RenderType type, Condition condition) {
         children.forEach(child -> {
             if (child.isActivated() && condition.isvalid(child))
                 child.render(type, condition);
         });
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