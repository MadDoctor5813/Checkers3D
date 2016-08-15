package com.kevin.physic;

import org.joml.Vector3f;

/**
 * Created by 532259 on 1/18/2016.
 */

//Represents a ray in worldspace, defined by a origiin pos, and a normalized direction
public class Ray {

    public final Vector3f pos;
    public final Vector3f dir;
    //this vector is 1 / dir. It comes in handy for fast collision testing, so it's
    //being precalculated
    public final Vector3f dirInv;

    public Ray(Vector3f pos, Vector3f dir) {
        this.pos = new Vector3f(pos);
        this.dir = new Vector3f(dir);
        dirInv = new Vector3f(1 / dir.x, 1/ dir.y, 1 / dir.z);
    }

}
