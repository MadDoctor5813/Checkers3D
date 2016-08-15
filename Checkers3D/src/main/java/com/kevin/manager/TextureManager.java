package com.kevin.manager;

import com.kevin.asset.Texture;
import com.kevin.util.DebugLogger;
import com.kevin.util.JARFileSystem;

import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-18.
 */

//Loads textures from .png files, with the help of the PNGDecoder library.
public class TextureManager {

    HashMap<String, Texture> textures;

    String rootDir;

    public Texture get(String name) {
        //load it if it doesn't exist
        if (!textures.containsKey(name)) {
            textures.put(name, new Texture(JARFileSystem.getPath(rootDir + name)));
            DebugLogger.log("Loaded texture: " + name);
        }
        return textures.get(name);

    }

    public TextureManager(String rootDir) {
        this.rootDir = rootDir;
        textures = new HashMap<>();
    }

}
