package com.kevin.manager;

import com.kevin.asset.Asset;
import com.kevin.util.DebugLogger;
import com.kevin.util.JARFileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-21.
 */

//Loads assets from resources/assets, and when doing this, loads all other needed resources
//Loads from custom made .asset files, also conveniently described in the project docs
public class AssetManager {

    HashMap<String, Asset> assets;
    String rootDir;

    public MaterialManager getMaterialManager() {
        return materialManager;
    }
    MaterialManager materialManager;

    public MeshManager getMeshManager() {
        return meshManager;
    }
    MeshManager meshManager;

    public AssetManager(String rootDir) {
        assets = new HashMap<>();
        this.rootDir = rootDir;
        materialManager = new MaterialManager("materials/");
        meshManager = new MeshManager("meshes/");
    }

    public Asset getAsset(String name) {
        //load an asset based on name and put it into the map if it isn't already loaded.
        if (!assets.containsKey(name)) {
            try {
                assets.put(name, load(JARFileSystem.getPath(rootDir + name)));
                DebugLogger.log("Loaded asset " + name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return assets.get(name);
    }

    public void loadAll() {
        //convenience method for loading every asset in one directory, usually for scene init.
        for (Path path : JARFileSystem.getAllPathsIn(rootDir)) {
            if (!Files.isDirectory(path)) {
                getAsset(path.getFileName().toString());
            }
        }
    }

    private Asset load(Path path) throws IOException {
        //load the asset file into a reader
        InputStream assetStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        BufferedReader assetReader = new BufferedReader(new InputStreamReader(assetStream));
        String materialName = assetReader.readLine();
        String meshName = assetReader.readLine();
        //create a new asset based on the file
        return new Asset(materialManager.get(materialName), meshManager.get(meshName));
    }



}
