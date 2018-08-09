package v2.engine.scene;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModuleNode extends Node {

    @Getter
    private Map<ModuleType, Module> modules;

    public ModuleNode(){
        super();
        modules = new HashMap<>();
    }

    public void update(){
        super.update();
        modules.values().forEach(e->e.update());
    }

    public void render(){
        if(modules.containsKey(ModuleType.RENDER_MODULE)){
            modules.get(ModuleType.RENDER_MODULE).render();
        }
        super.render();
    }

    public void cleanup(){
        super.cleanup();
        modules.values().forEach(e->e.cleanup());
    }

    public void addModule(ModuleType type, Module module){
        modules.put(type, module);
    }

}
