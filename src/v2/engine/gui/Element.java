package v2.engine.gui;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class Element {

    @Getter
    private ArrayList<Element> children;
    @Getter @Setter private Element parent;

    protected Element(){
        children = new ArrayList<>();
    }

    public void render(){
        children.stream().forEach(e -> e.render());
    }

    public void handle(){
        children.stream().forEach(e -> e.handle());
    }

    public void cleanup(){
        children.stream().forEach(e -> e.cleanup());
    }
}
