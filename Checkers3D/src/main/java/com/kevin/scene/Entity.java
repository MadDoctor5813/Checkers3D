package com.kevin.scene;

import com.kevin.asset.Asset;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Kevin on 2015-12-17.
 */

//Represents one entity in the world.
public class Entity {

    //describes the entitys's transformation
    Vector3f pos;
    Vector3f scale;
    Vector3f rot;

    public Matrix4f getModelMat() {
        return modelMat;
    }

    //the matrix holds the entity's transformation in one convenient mathematical entity
    Matrix4f modelMat;

    Scene scene;

    boolean matUpdateNeeded = false;

    public Asset getAsset() {
        return asset;
    }
    Asset asset;

    public Entity(Scene scene, Vector3f pos, Vector3f scale, Vector3f rot, String assetStr) {
        this.scene = scene;
        modelMat = new Matrix4f();
        this.pos = pos;
        this.scale = scale;
        this.rot = rot;
        this.asset = scene.getGame().getAssetManager().getAsset(assetStr);
        calcModelMat();
    }

    public void update() {
        if (matUpdateNeeded) {
            calcModelMat();
            matUpdateNeeded = false;
        }
    }

    public void render(Scene scene) {
        scene.meshRenderer.drawEntity(this);
    }

    private void calcModelMat() {
        modelMat.identity();
        modelMat.translate(pos);
        modelMat.scale(scale);
        modelMat.rotateXYZ(rot.x, rot.y, rot.z);
    }
}
