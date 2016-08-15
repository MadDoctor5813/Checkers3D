package com.kevin.graphics;

import org.joml.Vector3f;

/**
 * Created by Kevin on 2015-12-19.
 */

//Stores the non-essential (i.e, not the transform matrix or light parameters) uniforms for any shader.
//It can have multiple types based on what is specified in the material file.
public class UniformValue {

    //stores the type of the value to be uploaded later
    public enum Type {
        FLOAT,
        VEC3
    }

    Type type;
    //store the value as a string so we can convert later on demand
    String value;

    public UniformValue(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Type typeStrToType(String typeStr) {
        //convert type strings to the type enum
        if (typeStr.equals("float")) {
            return Type.FLOAT;
        }
        if (typeStr.equals("vec3")) {
            return Type.VEC3;
        }
        return null;
    }

    public float getFloatValue() {
        //if we're a float
        if (this.type == Type.FLOAT) {
            //return a float
            return Float.parseFloat(value);
        }
        else {
            //else complain to the caller.
            throw new UnsupportedOperationException("Cannot access float value of non float uniform value.");
        }
    }

    public Vector3f getVector3fValue() {
        // if we're a float
        if (this.type == Type.VEC3) {
            //retrieve component array
            String[] components = value.split(",");
            //return a float
            return new Vector3f(Float.parseFloat(components[0]), Float.parseFloat(components[1]), Float.parseFloat(components[2]));
        }
        else {
            //else complain to the caller
            throw new UnsupportedOperationException("Cannot access vec3 value of non vec3 uniform value.");
        }
    }

}
