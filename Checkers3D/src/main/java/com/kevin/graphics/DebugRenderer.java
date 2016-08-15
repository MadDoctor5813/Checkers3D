package com.kevin.graphics;

import com.kevin.physic.BoundingBox;
import com.kevin.physic.Ray;
import com.kevin.scene.Scene;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by 532259 on 1/19/2016.
 */
//This class draws lines on demand in world space to help visualize various game objects,
//primarily bounding boxes
public class DebugRenderer {

    private int vao;
    private int vbo;

    //a float array of all the vertexes to be drawn this frame in xyz format
    ArrayList<Float> vertexes;

    public DebugRenderer() {
        vertexes = new ArrayList<>();
        makeVbo();
        makeVao();
    }

    private void makeVbo() {
        vbo = GL15.glGenBuffers();
    }

    private void makeVao() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //only need the one position attribute here
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public void drawVertex(float x, float y, float z) {
        vertexes.add(x);
        vertexes.add(y);
        vertexes.add(z);
    }

    //convenience method to convert a box into vertexes. Not used in the final release
    public void drawBox(BoundingBox box) {
        drawVertex(box.min.x,box.min.y,box.max.z);
        drawVertex(box.max.x,box.min.y,box.max.z);
        drawVertex(box.max.x,box.max.y,box.max.z);
        drawVertex(box.min.x,box.max.y,box.max.z);
        
        drawVertex(box.max.x,box.min.y,box.max.z);
        drawVertex(box.max.x,box.min.y,box.min.z);
        drawVertex(box.max.x,box.max.y,box.min.z);
        drawVertex(box.max.x,box.max.y,box.max.z);

        drawVertex(box.min.x,box.max.y,box.max.z);
        drawVertex(box.max.x,box.max.y,box.max.z);
        drawVertex(box.max.x,box.max.y,box.min.z);
        drawVertex(box.min.x,box.max.y,box.min.z);

        drawVertex(box.min.x,box.min.y,box.min.z);
        drawVertex(box.min.x,box.max.y,box.min.z);
        drawVertex(box.max.x,box.max.y,box.min.z);
        drawVertex(box.max.x,box.min.y,box.min.z);

        drawVertex(box.min.x,box.min.y,box.min.z);
        drawVertex(box.max.x,box.min.y,box.min.z);
        drawVertex(box.max.x,box.min.y,box.max.z);
        drawVertex(box.min.x,box.min.y,box.max.z);

        drawVertex(box.min.x,box.min.y,box.min.z);
        drawVertex(box.min.x,box.min.y,box.max.z);
        drawVertex(box.min.x,box.max.y,box.max.z);
        drawVertex(box.min.x,box.max.y,box.min.z);
    }

    //convenience method to visualize a mouse ray
    public void drawRay(Ray ray, int length) {
        GL11.glLineWidth(10f);
        drawVertex(ray.pos.x, ray.pos.y, ray.pos.z);
        //calculate an end point based on length
        Vector3f endPoint = new Vector3f(ray.pos);
        endPoint.add(new Vector3f(ray.dir).mul(length));
        drawVertex(endPoint.x, endPoint.y, endPoint.z);
        GL11.glLineWidth(1f);
    }

    //renders all submitted vertexes
    public void render(Scene scene) {
        //grab the debug shader from the game
        ShaderProgram debugShader = scene.getGame().getAssetManager()
                .getMaterialManager().getShaderManager().get("DebugShading");
        debugShader.use();
        //upload just the view projection matrix, without the model matrix, because
        //all the vertexes are in worldspace
        debugShader.uploadMat4Uniform("VP", scene.getCamera().getVP());
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //upload all the vertexes into a FloatBuffer
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexes.size());
        for (Float data : vertexes) {
            vertexData.put(data);
        }
        vertexData.flip();
        //upload the data
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_DYNAMIC_DRAW);
        //bind the vbo and vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(vao);
        //set the line width
        GL11.glLineWidth(2.0f);
        //draw everything, divide by 3, because 3 floats in a vertex
        GL11.glDrawArrays(GL11.GL_LINES, 0, vertexes.size() / 3);
        //cleanup state
        GL30.glBindVertexArray(0);
        debugShader.unuse();
        vertexes.clear();
    }


}
