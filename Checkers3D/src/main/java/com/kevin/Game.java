package com.kevin;

import com.kevin.manager.AssetManager;
import com.kevin.manager.InputManager;
import com.kevin.scene.CheckersScene;
import com.kevin.scene.Scene;
import com.kevin.util.DebugLogger;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLCapabilities;

/**
 * Created by Kevin on 2015-12-07.
 */

//Holds data for the window and the global OpenGL context.
public class Game {

    long wndId;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    int width = 0;
    int height = 0;

    public long getLastFrameTime() {
        return lastFrameTime;
    }

    long lastFrameTime;

    public AssetManager getAssetManager() {
        return assetManager;
    }
    private AssetManager assetManager;

    public InputManager getInputManager() {
        return inputManager;
    }
    private InputManager inputManager;

    //references to event callbacks need to be held in the class, lest they be GC'd.
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback scrollCallback;

    CheckersScene scene;

    public Game() {
        initGLFW();
        wndId = makeWindow();
        initOpenGL();
        initSystems();
        GLFW.glfwShowWindow(wndId);
        scene = new CheckersScene(this);
    }

    public void run() {
        while (GLFW.glfwWindowShouldClose(wndId) == GLFW.GLFW_FALSE) {
            //calculate the frame time and save it
            long start = System.currentTimeMillis();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            inputManager.update();
            GLFW.glfwPollEvents();
            scene.update();
            scene.render();
            GLFW.glfwSwapBuffers(wndId);
            long end = System.currentTimeMillis();
            lastFrameTime = end - start;
        }
    }
    private void initGLFW() {
        //init windowing system, and check for init errors
        if (GLFW.glfwInit() != GLFW.GLFW_TRUE) {
            throw new IllegalStateException("Could not initialize GLFW");
        }
    }

    private long makeWindow() {
        //get the fullscreen width and height
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        width = vidMode.width();
        height = vidMode.height();
        //set up window parameters
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        //create the window, and return the id
        long id = GLFW.glfwCreateWindow(width, height, "Checkers 3D", 0, 0);
        return id;
    }

    private void initOpenGL() {
        //make the window's GL context current
        GLFW.glfwMakeContextCurrent(wndId);
        GL.createCapabilities();
        //Check to see if we have OpenGL 3.2
        if (!GL.getCapabilities().OpenGL32) {
            DebugLogger.log("Error, OpenGL 3.2 not installed. The game will most likely not work.");
        }
        else {
            DebugLogger.log("OpenGL 3.2 detected. All clear.");
        }
        //set the clear color to black
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Enable depth buffering and texturing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //set the active texture to texture0, no multi texturing for now.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    private void initSystems() {
        //load all assets
        assetManager = new AssetManager("assets/");
        assetManager.loadAll();
        //setup the input manager
        inputManager = new InputManager(wndId);
        //link the GLFW callbacks to the input manager
        GLFW.glfwSetKeyCallback(wndId, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                inputManager.onKeyEvent(key, scancode, action, mods);
            }
        });
        GLFW.glfwSetCursorPosCallback(wndId, cursorCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                inputManager.onCursorPosEvent((float)xpos, (float)ypos);
            }
        });
        GLFW.glfwSetMouseButtonCallback(wndId, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                inputManager.onMouseClickEvent(button, action, mods);
            }
        });
        GLFW.glfwSetScrollCallback(wndId, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                inputManager.onMouseScrollEvent(xoffset, yoffset);
            }
        });
    }

}
