package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Node {

    @Getter @Setter private Node parent;
    @Getter private List<Node> children;
    @Getter @Setter private Transform worldTransform;
    @Getter @Setter private Transform localTransform;

    public Node(){
        this.worldTransform = new Transform();
        this.localTransform = new Transform();
        this.children = new ArrayList<>();
    }

    public Node(Node _parent){
        this();
        this.parent = _parent;
    }

    public void addChild(Node child){
        child.setParent(this);
        children.add(child);
    }

    public void update(){
        if(parent != null){
            getWorldTransform().setRotation(getWorldTransform().getRotation().add(getParent().getWorldTransform().getRotation()));
            getWorldTransform().setTranslation(getWorldTransform().getTranslation().add(getParent().getWorldTransform().getTranslation()));
            getWorldTransform().setScaling(getWorldTransform().getScaling().mul(getParent().getWorldTransform().getScaling()));
        }
        children.forEach(child->child.update());
    }

    public void render(){
        children.forEach(child->child.render());
    }

    public void cleanup() {
        children.forEach(child->child.cleanup());
    }
}

