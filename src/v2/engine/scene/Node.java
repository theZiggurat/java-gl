package v2.engine.scene;

import lombok.Getter;
import lombok.Setter;
import v1.engine.util.Transformation;

import java.util.ArrayList;
import java.util.List;

public class Node {

    @Getter @Setter
    private Node parent;
    private List<Node> children;
    private Transformation worldTransform;
    private Transformation localTransform;

    public Node(){
        this.worldTransform = new Transformation();
        this.localTransform = new Transformation();
        this.children = new ArrayList<>();
    }

    public Node(Node _parent){
        this();
        this.parent = _parent;
    }

    public void update(){
        for(Node child: children)
            child.update();
    }

    public void render(){
        for(Node child: children)
            child.update();
    }

    public void cleanup(){
        for(Node child: children)
            child.cleanup();
    }
}

