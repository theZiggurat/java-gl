package v1.engine.game;

import v1.engine.GameEngine;

public class Main {

    public static void main(String [] args){

        try {
            boolean vsync = false;
            TutGameLogic logic_mgr = new TutGameLogic();
            GameEngine game_mgr = new GameEngine("TUT", 1280, 720, vsync, logic_mgr);
            game_mgr.start();
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
