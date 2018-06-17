package game;

import cxv1.engine3D.draw.Hud;
import cxv1.engine3D.entity.Entity;
import cxv1.engine3D.entity.TextEntity;
import cxv1.engine3D.util.Window;
import org.joml.Vector4f;

public class HUD implements Hud {

    private static final int FONT_COLS = 16,
                             FONT_ROWS = 16;

    private static final String FONT_TEXTURE = "Font.png";

    private final Entity[] entities;
    private final TextEntity statusTextEntity;

    public HUD(String statusText) throws Exception {
        this.statusTextEntity = new TextEntity(statusText, FONT_TEXTURE, FONT_COLS, FONT_ROWS);
        this.statusTextEntity.getMesh().getMaterial(0).setAmbientColor(new Vector4f(1,1,1,1));
        entities = new Entity[]{statusTextEntity};
    }

    public void setStatusText(String statusText){
        this.statusTextEntity.setText(statusText);
    }

    @Override
    public Entity[] getItems(){
        return entities;
    }

    public void updateSize(Window window){
        this.statusTextEntity.setPos(10f, window.getHeight() - 50f, 0);
    }

}
