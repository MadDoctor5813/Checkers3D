package com.kevin.scene;

import com.kevin.Game;
import com.kevin.graphics.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Kevin on 2015-12-19.
 */

//Represents one scene. Holds the scene's camera, the entities inside it, and its light.
public class Scene {

    //Represents the scene's state, i.e, whether or not we're playing a game, or showing the victory screen
    SceneState state = SceneState.INGAME;

    public Game getGame() {
        return game;
    }

    protected Game game;

    public Light getLight() {
        return light;
    }
    protected Light light;

    public Camera3D getCamera() {
        return camera;
    }
    protected Camera3D camera;
    protected MeshRenderer meshRenderer;
    protected DebugRenderer debugRenderer;
    protected FullScreenRenderer fullScreenRenderer;

    protected long lastFrameTime;

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    protected ArrayList<Entity> entities;

    public Scene(Game game) {
        this.game = game;
        entities = new ArrayList<>();
        this.meshRenderer = new MeshRenderer(game);
        this.meshRenderer.uploadMeshes(game.getAssetManager().getMeshManager().getMeshList());
        this.debugRenderer = new DebugRenderer();
        this.fullScreenRenderer = new FullScreenRenderer(game);
    }

    //called every frame to update everything inside this scene
    public void update() {
        camera.update();
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public void switchState(SceneState state) {
        this.state = state;
    }

    //called every frame, and, wait for it, renders the scene
    public void render() {
        long start = System.currentTimeMillis();
        for (Entity entity : entities) {
            entity.render(this);
        }
        meshRenderer.render(this);
    }

    //adds a light to the scene. For now, only one light is supported.
    //May or may not include multiple lights depending on need.
    public void addLight(Vector3f pos, Vector3f color, float attenuationFac, float ambientFac) {
        light = new Light(pos, color, attenuationFac, ambientFac);
    }

}
