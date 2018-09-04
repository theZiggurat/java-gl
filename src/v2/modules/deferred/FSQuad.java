package v2.modules.deferred;

import lombok.Getter;
import lombok.Setter;
import v2.engine.gldata.TextureObject;
import v2.engine.gldata.VertexBufferObject;
import v2.engine.javadata.MeshData;
import v2.engine.system.ShaderProgram;

public class FSQuad {

    @Setter @Getter private TextureObject screenTexture;
    private ShaderProgram fsShader;
    private VertexBufferObject quad;

    public FSQuad() {
        fsShader = new FSShaderProgram();
        quad = new VertexBufferObject(MeshData.loadMesh("res/models/quad.obj"));
    }

    public void render(){
        fsShader.bind();
        fsShader.updateUniforms(screenTexture);
        quad.render();
        fsShader.unbind();
    }
}
