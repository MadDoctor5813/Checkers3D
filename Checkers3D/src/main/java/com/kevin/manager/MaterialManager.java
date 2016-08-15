package com.kevin.manager;

import com.kevin.asset.Material;
import com.kevin.graphics.UniformValue;
import com.kevin.util.DebugLogger;
import com.kevin.util.JARFileSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2015-12-19.
 */

//Loads materials from .mat files, described in the project docs.
public class MaterialManager {

    HashMap<String, Material> materials;

    String rootDir;

    public ShaderManager getShaderManager() {
        return shaderManager;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    ShaderManager shaderManager;
    TextureManager textureManager;

    public MaterialManager(String rootDir) {
        materials = new HashMap<>();
        this.rootDir = rootDir;
        shaderManager = new ShaderManager("shaders/");
        textureManager = new TextureManager("textures/");
    }

    public Material get(String name) {
        //if we haven't already the loaded the named material, put it into the map
        if (!materials.containsKey(name)) {
            try {
                load(name);
                DebugLogger.log("Loaded material: " + name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return materials.get(name);
    }

    private void load(String name) throws IOException {
        //get the path to the material, and load it into a reader
        Path s = JARFileSystem.getPath(rootDir + name);
        InputStream matStream = this.getClass().getClassLoader().getResourceAsStream(JARFileSystem.getPath(rootDir + name).toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(matStream));
        //read in the program name and texture names
        String programName = reader.readLine();
        String textureName = reader.readLine();
        HashMap<String, UniformValue> uniformValues = new HashMap<>();
        //read in the other uniform values
        String line = null;
        while ((line = reader.readLine()) != null) {
            Map.Entry<String, UniformValue> valueEntry = loadUniformValue(line);
            uniformValues.put(valueEntry.getKey(), valueEntry.getValue());
        }
        materials.put(name, new Material(shaderManager.get(programName), textureManager.get(textureName), uniformValues));
    }

    private Map.Entry<String, UniformValue> loadUniformValue(String line) {
        //split into name and value
        String[] lineArr = line.split("=");
        //split the name into type and name
        String[] typeAndName = lineArr[0].split(" ");
        //parse out the data
        UniformValue.Type valueType = UniformValue.typeStrToType(typeAndName[0].trim());
        String valueName = typeAndName[1].trim();
        String value = lineArr[1].trim();
        //add the new value to the map
        return new AbstractMap.SimpleEntry<>(valueName, new UniformValue(valueType, value));
    }

}
