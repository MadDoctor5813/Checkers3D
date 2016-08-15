package com.kevin.manager;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

/**
 * Created by 532259 on 1/14/2016.
 */

//Translates events from GLFW into a state we can use
public class InputManager {

    //the game window's id
    long wndId;

    //maps that indicate whether or not a mouse button or keyboard key is down
    HashMap<Integer, Boolean> mouseMap;
    HashMap<Integer, Boolean> keyMap;

    public Vector2f getMousePos() {
        return mousePos;
    }

    Vector2f mousePos;
    Vector2f lastMousePos;

    public Vector2f getDelta() {
        return delta;
    }

    //how much the mouse moved since last frame
    Vector2f delta;

    //how much the scroll wheel scrolled vertically last frame
    public float getYScroll() {
        return YScroll;
    }

    //don't use horizontal scrolling, but GLFW provides it, so its there for completeness
    public float getXScroll() {
        return XScroll;
    }

    float YScroll;
    float XScroll;

    public InputManager(long wndId) {
        this.wndId = wndId;
        mouseMap = new HashMap<>();
        keyMap = new HashMap<>();
        mousePos = new Vector2f();
        lastMousePos = new Vector2f();
        delta = new Vector2f();
    }

    public void update() {
        //reset the scroll per frame
        YScroll = 0;
        XScroll = 0;
    }

    public boolean isKeyDown(int key) {
        if (keyMap.containsKey(key)) {
            return keyMap.get(key);
        }
        else {
            return false;
        }
    }

    public boolean isMouseButtonDown(int button) {
        if (mouseMap.containsKey(button)) {
            return mouseMap.get(button);
        }
        else {
            return false;
        }
    }

    //handle key events
    public void onKeyEvent(int key, int scancode, int action, int mods) {
        //special handler here for the exit key
        if (action == GLFW.GLFW_PRESS && key == GLFW.GLFW_KEY_ESCAPE) {
            GLFW.glfwSetWindowShouldClose(wndId, GLFW.GLFW_TRUE);
        }
        //if its pressed, set it to true, if its released, the key is no longer down,
        //therefore false
        if (action == GLFW.GLFW_PRESS) {
            keyMap.put(key, true);
        }
        else if (action == GLFW.GLFW_RELEASE) {
            keyMap.put(key, false);
        }
    }

    //handle events relating to cursor movement
    public void onCursorPosEvent(float x, float y) {
        delta.zero();
        //update the delta
        Vector2f.sub(lastMousePos, mousePos, delta);
        //update the last pos
        lastMousePos.set(mousePos);
        //update the current pos
        mousePos.set(x, y);
    }

    public void onMouseScrollEvent(double x, double y) {
        YScroll = (float)y;
        XScroll = (float)x;
    }

    //handle mouse click events
    public void onMouseClickEvent(int button, int action, int mods) {
        if (action == GLFW.GLFW_PRESS) {
            mouseMap.put(button, true);
        }
        else if (action == GLFW.GLFW_RELEASE) {
            mouseMap.put(button, false);
        }
    }
}
