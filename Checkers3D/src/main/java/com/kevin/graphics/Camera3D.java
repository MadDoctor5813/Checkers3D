package com.kevin.graphics;

import com.kevin.Game;
import com.kevin.physic.Ray;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Kevin on 2015-12-17.
 */


//The Camera3D class, is unsurprisingly a camera. It holds the position of the camera, the point
//it's supposed to look at, and the matrixes that allow for the math.
public class Camera3D {

    Game game;

    Vector3f pos;
    Vector3f lookTarget;
    float targetDist = 20;
    float targetYaw = 0;
    float targetPitch = 45;

    final float zNear = 0.01f;
    final float zFar = 100f;

    int[] viewport;

    float ROTATE_SENSITIVITY = 0.1f;
    float ZOOM_SENSITIVITY = 2f;

    final static Vector3f UP = new Vector3f(0, 1, 0);

    private final int FOV = 45;

    boolean updateMatrix = false;

    Matrix4f projection;
    Matrix4f view;

    public Matrix4f getVP() {
        return VP;
    }
    Matrix4f VP;
    Matrix4f VPInv;

    public Camera3D(Game game, Vector3f pos, Vector3f lookTarget) {
        projection = new Matrix4f();
        view = new Matrix4f();
        VP = new Matrix4f();
        VPInv = new Matrix4f();
        viewport = new int[] { 0, 0, game.getWidth(), game.getHeight() };
        this.pos = pos;
        this.game = game;
        this.lookTarget = lookTarget;
        makeProjection();
        makeView();
        makeVP();
    }

    public void update() {
        if (updateMatrix) {
            makeView();
            makeProjection();
            makeVP();
            updateMatrix = false;
        }
    }

    public Ray unprojectMouse(Vector2f screen) {
        Vector3f near = new Vector3f();
        Vector3f far = new Vector3f();
        VPInv.unprojectInv(screen.x, game.getHeight() - screen.y, 0, viewport, near);
        VPInv.unprojectInv(screen.x, game.getHeight() - screen.y, 1, viewport, far);
        Vector3f dir = new Vector3f(far).sub(near).normalize();
        return new Ray(near, dir);
    }

    public void arcballRotate(Vector2f rotations) {
        //use negative rotations or else they rotate the wrong way
        targetYaw += -rotations.x * ROTATE_SENSITIVITY;
        targetPitch += -rotations.y * ROTATE_SENSITIVITY;
        //cap the yaw and pitch to reasonable values
        targetYaw = targetYaw % 360;
        if (targetPitch > 85) {
            targetPitch = 85;
        }
        else if (targetPitch < 10) {
            targetPitch = 10;
        }
        updateMatrix = true;
    }

    public void zoom(float zoom) {
        targetDist += zoom * ZOOM_SENSITIVITY;
        updateMatrix = true;
    }

    public void makeVP() {
        //reset the VP matrix
        VP.identity();
        //multiply it by the projection and the view (matrix mults have to go in reverse)
        VP.mul(projection).mul(view);
        //also save an inverted copy
        VP.invert(VPInv);
    }

    private void makeProjection() {
        //set up perspective matrix with FOV and window width and height
        projection.identity();
        projection.perspective((float)Math.toRadians(FOV), (float)game.getWidth() / (float)game.getHeight(), zNear, zFar);
    }

    private void makeView() {
        //calculate the proper position from distance, pitch and yaw
        pos.x = targetDist * (float)Math.cos(Math.toRadians((double)targetYaw));
        pos.z = targetDist * (float)Math.sin(Math.toRadians((double)targetYaw));
        pos.y = targetDist * (float)Math.sin(Math.toRadians((double)targetPitch));
        //reset the view matrix
        view.identity();
        view.setLookAt(pos, lookTarget, UP);
    }

}
