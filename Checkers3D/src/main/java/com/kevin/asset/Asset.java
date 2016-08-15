package com.kevin.asset;

/**
 * Created by Kevin on 2015-12-21.
 */

//Binds together a Material and a Mesh. Loaded from the resources/assets/ directory.
public class Asset {

    public Material getMaterial() {
        return material;
    }
    Material material;

    public Mesh getMesh() {
        return mesh;
    }
    Mesh mesh;

    public Asset(Material material, Mesh mesh) {
        this.material = material;
        this.mesh = mesh;
    }
}
