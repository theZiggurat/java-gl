package cxv1.engine3D.draw;

import cxv1.engine3D.entity.Entity;

public interface Hud {

    Entity[] getItems();

    default void cleanup(){
        Entity[] entities = getItems();
        for(Entity e: entities){
            e.getMesh().cleanup();
        }
    }


}
