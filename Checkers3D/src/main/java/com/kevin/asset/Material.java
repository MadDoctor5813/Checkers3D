package com.kevin.asset;

import com.kevin.graphics.ShaderProgram;
import com.kevin.graphics.UniformValue;

import java.util.HashMap;

/**
 * Created by Kevin on 2015-12-19.
 */

//Binds together a reference to a ShaderProgram, the uniforms that are uploaded into it, and a texture.
//Loaded from the resources/materials folder
public class Material {

    public ShaderProgram getProgram() {
        return program;
    }
    ShaderProgram program;

    public Texture getTexture() {
        return texture;
    }
    Texture texture;

    public HashMap<String, UniformValue> getUniformValues() {
        return uniformValues;
    }
    HashMap<String, UniformValue> uniformValues;

    public Material(ShaderProgram program, Texture texture, HashMap<String, UniformValue> uniformValues) {
        this.program = program;
        this.texture = texture;
        this.uniformValues = uniformValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        if (program != null ? !program.equals(material.program) : material.program != null) return false;
        if (texture != null ? !texture.equals(material.texture) : material.texture != null) return false;
        return !(uniformValues != null ? !uniformValues.equals(material.uniformValues) : material.uniformValues != null);

    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (texture != null ? texture.hashCode() : 0);
        result = 31 * result + (uniformValues != null ? uniformValues.hashCode() : 0);
        return result;
    }
}
