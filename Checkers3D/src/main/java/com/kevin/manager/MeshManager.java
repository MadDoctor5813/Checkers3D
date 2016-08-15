package com.kevin.manager;

import com.kevin.asset.Mesh;
import com.kevin.util.DebugLogger;
import com.kevin.util.JARFileSystem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-13.
 */

//Loads meshes from a custom .jmdl file, described in the project docs
public class MeshManager {

    String rootDir;

    private HashMap<String, Mesh> meshes;

    public Mesh get(String name) {
        //load if we haven't already
        if (!meshes.containsKey(name)) {
            meshes.put(name, new Mesh(JARFileSystem.getPath(rootDir + name)));
            DebugLogger.log("Loaded mesh: " + name);
        }
        return meshes.get(name);
    }

    public ArrayList<Mesh> getMeshList() {
        return new ArrayList<>(meshes.values());
    }

    public MeshManager(String rootDir) {
        this.rootDir = rootDir;
        //init mesh list
        meshes = new HashMap<>();

    }


}
