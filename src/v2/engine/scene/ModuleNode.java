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
        if(modules.containsKey(ModuleType.RENDER_MODULE_SCENE)){
            modules.get(ModuleType.RENDER_MODULE_SCENE).render();
        }
        super.render();
    }

    public void renderWireframe(){
        if(modules.containsKey(ModuleType.RENDER_MODULE_WIREFRAME)){
            modules.get(ModuleType.RENDER_MODULE_WIREFRAME).render();
        }
        super.render();
    }

    public void renderShadow(){
        if(modules.containsKey(ModuleType.RENDER_MODULE_SHADOW)){
            modules.get(ModuleType.RENDER_MODULE_SHADOW).render();
        }
        super.render();
    }

    public void renderOverlay(){
        if(modules.containsKey(ModuleType.RENDER_MODULE_OVERLAY)){
            modules.get(ModuleType.RENDER_MODULE_OVERLAY).render();
        }
        super.render();
    }

    public void cleanup(){
        super.cleanup();
        modules.values().forEach(e->e.cleanup());
    }

    public void addModule(ModuleType type, Module module){
        module.setParent(this);
        modules.put(type, module);
    }

}
