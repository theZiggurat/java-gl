package v2;

import v2.engine.application.ApplicationContext;
import v2.engine.system.Core;

public class Main {

    public static void main(String... args){

        if(args.length >= 1){
            ApplicationContext.setUI_DEBUG_MODE(Boolean.valueOf(args[0]));
        }

        try {
            Core engine = new Core();
            engine.start();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
