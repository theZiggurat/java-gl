package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Node {

    @Getter
    @Setter
    private Node parent;
    @Getter
    private List<Node> children;
    @Getter
    @Setter
    private Transform worldTransform;
    @Getter
    @Setter
    private Transform localTransform;

    public Node() {
        this.worldTransform = new Transform();
        this.localTransform = new Transform();
        this.children = new ArrayList<>();
    }

    public Node(Node _parent) {
        this();
        this.parent = _parent;
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
            getWorldTransform().setRotation(getWorldTransform().getRotation().add(getParent().getWorldTransform().getRotation()));
            getWorldTransform().setTranslation(getWorldTransform().getTranslation().add(getParent().getWorldTransform().getTranslation()));
            getWorldTransform().setScaling(getWorldTransform().getScaling().mul(getParent().getWorldTransform().getScaling()));
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

    public void scale(Vector3f scale) {
        worldTransform.setScaling(scale);
    }

    public void scale(float scale) {
        worldTransform.setScaling(new Vector3f(scale, scale, scale));
    }

}

