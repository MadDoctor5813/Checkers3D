package com.kevin.physic;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 * Created by 532259 on 1/15/2016.
 */

//Represents an axis aligned bounding box. Used to determine what piece or square the mouse clicked on
public class BoundingBox {

    public Vector3f max;
    public Vector3f min;

    public BoundingBox(Vector3f min, Vector3f max) {
        this.min = new Vector3f(min);
        this.max = new Vector3f(max);
    }

    //checks if a given ray collides with this bounding box, returns the distance if it did,
    //but returns negative 1 if it didn't
    public float rayCollision(Ray ray) {
        //Uses the slab method of collision
        double t1 = (min.x - ray.pos.x) * ray.dirInv.x;
        double t2 = (max.x - ray.pos.x ) * ray.dirInv.x;

        double tmin = Math.min(t1, t2);
        double tmax = Math.max(t1, t2);

        for (int i = 1; i < 3; ++i) {
            t1 = (min.get(i) - ray.pos.get(i)) * ray.dirInv.get(i);
            t2 = (max.get(i) - ray.pos.get(i)) * ray.dirInv.get(i);

            tmin = Math.max(tmin, Math.min(t1, t2));
            tmax = Math.min(tmax, Math.max(t1, t2));
        }

        if (tmax < Math.max(tmin, 0.0)) {
            return -1;
        }

        //we are inside the box
        else if (tmin < 0) {
            return (float) tmax;
        }
        else {
            return (float) tmin;
        }
    }

}
