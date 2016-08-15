package com.kevin.graphics;

import com.kevin.Game;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Kevin on 2016-01-23.
 */
//This class just renders a texture to the whole screen, on demand. Only used for the ending victory screen
public class FullScreenRenderer {

    int vao;
    int vbo;

    ShaderProgram guiProgram;

    private Game game;

    public FullScreenRenderer(Game game) {
        this.game = game;
        guiProgram = game.getAssetManager().getMaterialManager().getShaderManager().get("GUIShading");
        initVbo();
        initVao();
    }

    public void render(String texture) {
        guiProgram.use();
        //disable face culling based on the direction it faces
        GL11.glDisable(GL11.GL_CULL_FACE);
        //bind the given texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, game.getAssetManager().getMaterialManager().getTextureManager().get(texture).getId());
        //upload texture unit 0 to the shader
        guiProgram.uploadIntUniform("tex", 0);
        GL30.glBindVertexArray(vao);
        //draw 4 vertexes
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(0);
        //re enable face culling for the rest of the code
        GL11.glEnable(GL11.GL_CULL_FACE);
        //un use the program
        guiProgram.unuse();

    }

    private void initVbo() {
        vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //5 floats per vertex * 4 vertexes
        FloatBuffer data = BufferUtils.createFloatBuffer(5 * 4);
        //hard code in the vertexes, because we'll be rendering full screen every time
        //vert 1
        data.put(new float[] { -1f, -1f, 0f, 0f, 1f});
        //vert 2
        data.put(new float[] { 1f, -1f, 0f, 1f, 1f});
        //vert 3
        data.put(new float[] { -1f, 1f, 0f, 0f, 0f});
        //vert 4
        data.put(new float[] { 1f, 1f, 0f, 1f, 0f});
        data.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void initVao() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        //set up position attrib
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 5, 0);
        //set up UV coord attrib
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

}
