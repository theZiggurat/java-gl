package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Node {

    @Getter @Setter
    private Node parent;
    @Getter
    private List<Node> children;
//    @Getter @Setter
//    private Transformation worldTransform;
//    @Getter @Setter
//    private Transformation localTransform;

    public Node(){
//        this.worldTransform = new Transformation();
//        this.localTransform = new Transformation();
        this.children = new ArrayList<>();
    }

    public Node(Node _parent){
        this();
        this.parent = _parent;
    }

    public void update(){
        children.forEach(child->child.update());
    }

    public void render(){
        children.forEach(child->child.render());
    }

    public void cleanup(){
        children.forEach(child->child.cleanup());
    }
}

