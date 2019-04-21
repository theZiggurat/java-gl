package v2.modules.post.ssr;

import v2.engine.glapi.tex.TextureObject;
import v2.engine.system.Pipeline;


/** SREEN SPACE REFLECTION
 *
 *
 *
 */
public class SSR {

    private SSRShader ssrShader;

    private Pipeline pipeline;

    public SSR(Pipeline pipeline){
        this.pipeline = pipeline;
        ssrShader = new SSRShader();
    }

    public void compute(TextureObject worldPos, TextureObject worldNorm, TextureObject ao){
        ssrShader.compute(worldPos, worldNorm, ao, pipeline.getSceneBuffer());
    }

}
