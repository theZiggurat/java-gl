package v2;

import v2.engine.system.Core;

public class Main {

    public static void main(String... args){
        try {
            Core engine = new Core();
            engine.start();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
