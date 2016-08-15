package com.kevin;

import com.kevin.util.DLLLoader;

//App entry point, very simple.
public class Main {

    public static void main(String[] args) {
        DLLLoader.load();
        Game game = new Game();
        game.run();
    }

}
