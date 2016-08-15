package com.kevin.graphics;

import org.joml.Vector3f;

/**
 * Created by Kevin on 2015-12-21.
 */

//Stores the position, color, attenuation (light strength drop off over distance),
//and ambient lighting of a light
public class Light {

    public Vector3f pos;
    public Vector3f color;
    public float attenuationFac;
    public float ambientFac;

    public Light(Vector3f pos, Vector3f color, float attenuationFac, float ambientFac) {
        this.pos = pos;
        this.color = color;
        this.attenuationFac = attenuationFac;
        this.ambientFac = ambientFac;
    }
}
