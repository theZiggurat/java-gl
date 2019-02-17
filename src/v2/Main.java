package v2;

import v2.engine.system.EngineCore;
import v2.instances.PBRTest;
import v2.instances.PBRTest2;

public class Main {

    public static void main(String... args){
        try {
            EngineCore engine = new EngineCore();
            engine.start();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
