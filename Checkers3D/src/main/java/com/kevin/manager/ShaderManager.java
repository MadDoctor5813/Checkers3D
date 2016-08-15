package com.kevin.manager;

import com.kevin.graphics.ShaderProgram;
import com.kevin.util.DebugLogger;
import com.kevin.util.JARFileSystem;

import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-19.
 */

//Loads ShaderPrograms from one name. Since vertex and fragment shaders generally have the same name,
//One name can be used to load both.
public class ShaderManager {

    HashMap<String, ShaderProgram> programs;
    String rootDir;

    public ShaderManager(String rootDir) {
        programs = new HashMap<>();
        this.rootDir = rootDir;
    }

    public ShaderProgram get(String name) {
        if (!programs.containsKey(name)) {
            //append proper file extensions to load vertex and fragment shaders.
            programs.put(name, new ShaderProgram(JARFileSystem.getPath(rootDir + name + ".vert"), JARFileSystem.getPath(rootDir + name + ".frag")));
            DebugLogger.log("Loaded shader: " + name);
        }
        return programs.get(name);
    }

}
