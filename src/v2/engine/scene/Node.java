package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node extends Transform<Node> {

    @Getter
    @Setter
    private Node parent;
    @Getter
    private List<Node> children;

    public Node() {
        super();
        this.children = new ArrayList<>();
    }

    public Node(Node _parent) {
        this();
        this.parent = _parent;
    }

    public Matrix4f getModelMatrix(){
        Matrix4f ret = new Matrix4f();
        ret.identity().translate(getTranslation())
                .rotateX((float)Math.toRadians(-rotation.x))
                .rotateY((float)Math.toRadians(-rotation.y))
                .rotateZ((float)Math.toRadians(-rotation.z))
                .scale(getScaling());
        return ret;
    }

    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    public void addChildren(Node... children){
        getChildren().addAll(Arrays.asList(children));
        Arrays.stream(children).forEach(e -> e.setParent(this));
    }

    public void update() {
        if (parent != null) {
//            getWorldTransform().setRotation(getWorldTransform().getRotation().add(getParent().getWorldTransform().getRotation()));
//            getWorldTransform().setTranslation(getWorldTransform().getTranslation().add(getParent().getWorldTransform().getTranslation()));
//            getWorldTransform().setScaling(getWorldTransform().getScaling().mul(getParent().getWorldTransform().getScaling()));
              scaleTo(getScaling().add(parent.getScaling()));
              rotateTo(getRotation().add(parent.getRotation()));
              translateTo(getTranslation().add(parent.getTranslation()));
        }
        children.forEach(child -> child.update());
    }

    public void render() {
        children.forEach(child -> child.render());
    }

    public void renderWireframe() {
        children.forEach(child -> child.renderWireframe());
    }

    public void cleanup() {
        children.forEach(child -> child.cleanup());
    }

}

