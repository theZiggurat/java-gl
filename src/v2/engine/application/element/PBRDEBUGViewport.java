package v2.engine.application.element;

import v2.engine.application.ApplicationContext;
import v2.engine.application.event.mouse.MouseClickEvent;
import v2.engine.glapi.tex.TextureObject;
import v2.modules.pbr.PBRPipeline;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class PBRDEBUGViewport extends TextureViewport {

    private TextureObject[] refs = new TextureObject[5];
    private int index;

    private int borderSize = 4;

    public PBRDEBUGViewport(PBRPipeline pbr) {
        super(pbr.getSsaoPass().getTargetTexture());

        refs[0]= pbr.getPbrFBO().getPosition();
        refs[1]= pbr.getPbrFBO().getAlbedo();
        refs[2]= pbr.getPbrFBO().getNormal();
        refs[3]= pbr.getShadowFBO().getDepth();
        refs[4]= pbr.getSsaoPass().getTargetTexture();

        //mainPanel.setRounding(0,0,10,10);
        mainPanel.setBorderSize(borderSize);
        mainPanel.setDrawBorder(true);

        onEvent(e -> {
            if(e instanceof MouseClickEvent){
                MouseClickEvent m = (MouseClickEvent)e;
                if(mainPanel.getAbsoluteBox().isWithin(m.getScreenPos())){
                    if(m.getKey() == GLFW_MOUSE_BUTTON_1 && m.getAction() == MouseClickEvent.BUTTON_CLICK){
                        index = (index+1)%5;
                        mainPanel.setImageBuffer(refs[index]);
                    }

                }
            }
        });
    }
}
