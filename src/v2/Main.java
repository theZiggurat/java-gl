package v2;

import v2.engine.system.EngineCore;
import v2.instances.PBRTest;

public class Main {

    public static void main(String... args){
        try {
            PBRTest engineInterface = new PBRTest();
            EngineCore engine = new EngineCore(engineInterface);
            engine.start();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
