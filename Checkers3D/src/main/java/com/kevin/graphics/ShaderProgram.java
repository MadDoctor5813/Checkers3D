package com.kevin.graphics;

import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Path;

/**
 * Created by Kevin on 2015-12-07.
 */

//Stores a vertex shader and its matching fragment shader, and compiles and uploads them.
public class ShaderProgram {

    int vertId;
    int fragId;
    int progId;

    //this buffer is used for uploading matrixes to the shader. It is cleared every use
    FloatBuffer matrixBuffer;

    public ShaderProgram(Path vertPath, Path fragPath) {
        vertId = loadVertShader(vertPath);
        fragId = loadFragShader(fragPath);
        progId = linkProgram();
        matrixBuffer = BufferUtils.createFloatBuffer(16);
    }

    public void use() {
        GL20.glUseProgram(progId);
    }

    public void unuse() {
        GL20.glUseProgram(0);
    }

    public void uploadFloatUniform(String name, float value) {
        int loc = GL20.glGetUniformLocation(progId, name);
        GL20.glUniform1f(loc, value);
    }

    public void uploadVec3Uniform(String name, Vector3f value) {
        int loc = GL20.glGetUniformLocation(progId, name);
        GL20.glUniform3f(loc, value.x, value.y, value.z);
    }

    public void uploadMat4Uniform(String name, Matrix4f value) {
        int loc = GL20.glGetUniformLocation(progId, name);
        value.get(matrixBuffer);
        GL20.glUniformMatrix4fv(loc, false, matrixBuffer);
    }

    public void uploadIntUniform(String name, int value) {
        int loc = GL20.glGetUniformLocation(progId, name);
        GL20.glUniform1i(loc, value);
    }

    private int loadVertShader(Path path) {
        //create the vertex shader
        int id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        //load the given path into a string
        InputStream shaderStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        String shaderText = null;
        try {
            shaderText = IOUtils.toString(shaderStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //compile the shader
        GL20.glShaderSource(id, shaderText);
        GL20.glCompileShader(id);
        //check for errors
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            System.out.println("Vertex shader compile error. Info log:");
            System.out.println(GL20.glGetShaderInfoLog(id));
        }
        return id;
    }

    private int loadFragShader(Path path) {
        //create the vertex shader
        int id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        InputStream shaderStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        String shaderText = null;
        //load the given path into a string
        try {
            shaderText = IOUtils.toString(shaderStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //compile the shader
        GL20.glShaderSource(id, shaderText);
        GL20.glCompileShader(id);
        //check for errors
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            System.out.println("Fragment shader compile error. Info log:");
            System.out.println(GL20.glGetShaderInfoLog(id));
        }
        return id;
    }

    private int linkProgram() {
        //create a shader program
        int id = GL20.glCreateProgram();
        //attach the vertex and fragment shaders
        GL20.glAttachShader(id, vertId);
        GL20.glAttachShader(id, fragId);
        //link the program together
        GL20.glLinkProgram(id);
        //check for errors
        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            System.out.println("Program link error. Info log:");
            System.out.println(GL20.glGetProgramInfoLog(id));
        }
        return id;
    }
}
