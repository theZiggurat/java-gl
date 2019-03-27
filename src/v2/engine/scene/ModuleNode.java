package v2.engine.scene;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModuleNode extends Node {

    @Getter
    private Map<RenderType, Module> modules;

    public ModuleNode(){
        super();
        modules = new HashMap<>();

    }

    public void update(){
        super.update();
        modules.values().forEach(e->e.update());
    }

    public void render(RenderType type){
        if(modules.containsKey(type)){
            modules.get(type).render();
        }
        super.render(type);
    }

    public void render(RenderType type, Condition condition) {
        if(modules.containsKey(type)){
            modules.get(type).render();
        }
        super.render(type, condition);
    }

    public void cleanup(){
        super.cleanup();
        modules.values().forEach(e->e.cleanup());
    }

    public void addModule(RenderType type, Module module){
        module.setParent(this);
        modules.put(type, module);
    }

}
