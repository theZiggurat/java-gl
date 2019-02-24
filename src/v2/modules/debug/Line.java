package v2.modules.debug;

import v2.engine.gldata.vbo.Meshs;
import v2.engine.scene.ModuleNode;
import v2.engine.scene.ModuleType;
import v2.engine.scene.RenderModule;

public class Line extends ModuleNode {

    public Line(){
        super();

        RenderModule line = new RenderModule(
            GenericOverlayShaderProgram.instance(), Meshs.line
        );

        addModule(ModuleType.RENDER_MODULE_OVERLAY, line);
    }
}
