package modules.post.ssr;

import engine.glapi.TextureObject;
import engine.system.Pipeline;


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
