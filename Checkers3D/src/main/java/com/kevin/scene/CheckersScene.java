package com.kevin.scene;

import com.kevin.Game;
import com.kevin.graphics.Camera3D;
import com.kevin.physic.Ray;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.text.NumberFormat;

/**
 * Created by 532259 on 1/14/2016.
 */

//This scene stores the board and handles passing events to it. It also displays the victory
//screen when you win
public class CheckersScene extends Scene {

    BoardEntity board;

    //how long left to display the victory screen
    long victoryScreenTime = 2500;

    Vector2f mousePos;
    //we store this so we know the amount the mouse moved since last frame
    Vector2f lastMousePos;

    public CheckersScene(Game game) {
        super(game);
        lastMousePos = new Vector2f();
        mousePos = new Vector2f();
        //init the board
        board = new BoardEntity(this, new Vector3f(0), new Vector3f(1), new Vector3f(0));
        entities.add(board);
        //init the one light
        this.addLight(new Vector3f(0, 10, 0), new Vector3f(1f, 1f, 1f), 0.001f, 0.1f);
        //init the camera
        this.camera = new Camera3D(game, new Vector3f(0, 15, 15), new Vector3f(board.pos));
    }

    @Override
    public void render() {
        //if we're in game, just let the superclass render the board
        if (state == SceneState.INGAME) {
            super.render();
        }
        //if we're in the victory state, fullscreen render our victory splash screen
        else if (state == SceneState.VICTORY) {
            fullScreenRenderer.render("victory.png");
        }
    }

    @Override
    public void update() {
        if (state == SceneState.INGAME) {
            super.update();
            //rotate the camera based on the mouse movement if the right mouse button is pressed
            if (game.getInputManager().isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
                camera.arcballRotate(game.getInputManager().getDelta());
            }
            //zoom the camera based on mouse scrolling
            //negate the scroll to match every other application out there
            camera.zoom(-game.getInputManager().getYScroll());
            //get the mouse ray from the camera
            Ray ray = camera.unprojectMouse(game.getInputManager().getMousePos());
            //send it to the board
            if (game.getInputManager().isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                board.procMouseRay(ray);
            }
            if (game.getInputManager().isKeyDown(GLFW.GLFW_KEY_R)) {
                board.resetBoard();
            }
        }
        //if we're in the victory state
        else if (state == SceneState.VICTORY) {
            //deduct the game's last frame time from the victory screen time
            victoryScreenTime -= game.getLastFrameTime();
            //if the time is less than 0
            if (victoryScreenTime < 0) {
                //reset the time
                victoryScreenTime = 2500;
                //go back to the game
                switchState(SceneState.INGAME);
            }
        }
    }

}

