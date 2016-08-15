package com.kevin.graphics;

import com.kevin.Game;
import com.kevin.asset.Material;
import com.kevin.asset.Mesh;
import com.kevin.scene.Entity;
import com.kevin.scene.Scene;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2015-12-15.
 */

//Takes a whole scene, and renders every entity in it.
public class MeshRenderer {

    private int vaoId;
    private int vboId;

    private int iboId;

    private Game game;

    HashMap<Mesh, IBOPosition> iboPositions;

    //a list of all the entities to render this frame
    ArrayList<Entity> toRender;

    ShaderProgram program;

    public MeshRenderer(Game game) {
        this.game = game;
        iboPositions = new HashMap<>();
        toRender = new ArrayList<>();
        program = game.getAssetManager().getMaterialManager().
                getShaderManager().get("DiffuseShading");
    }

    //Caches all the given meshes to a VBO and IBO. To be called on scene initialization
    public void uploadMeshes(ArrayList<Mesh> meshes) {
        //clear the position map
        iboPositions.clear();
        int numVertices = 0;
        int numIndices = 0;
        //for every mesh
        for (Mesh mesh : meshes) {
            //add its position to the map
            IBOPosition position = new IBOPosition(numIndices, mesh.getIndices().length, numVertices);
            iboPositions.put(mesh, position);
            //update the counters
            numVertices += mesh.getVertices().length / Vertex.VERTEX_FLOATS;
            numIndices += mesh.getIndices().length;
        }
        //initialize buffers for the vertex and index data
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(numVertices * Vertex.VERTEX_FLOATS);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(numIndices);
        //upload the data from each mesh to the buffers
        for (Mesh mesh : meshes) {
            vertexBuffer.put(mesh.getVertices());
            indexBuffer.put(mesh.getIndices());
        }
        //prepare the buffers for reading
        vertexBuffer.flip();
        indexBuffer.flip();
        //create the VBO and IBO from both buffers
        createVBO(vertexBuffer);
        createIBO(indexBuffer);
        //create the VAO
        createVAO();
    }

    public void drawEntity(Entity entity) {
        toRender.add(entity);
    }

    public void render(Scene scene) {
        //sort the entities by material
        for (int i = 0; i < toRender.size(); i++) {
            for (int j = 0; j < toRender.size() - 1; j++) {
                if (!toRender.get(i).getAsset().getMaterial().equals(toRender.get(j).getAsset().getMaterial())) {
                    Entity temp = toRender.get(i);
                    toRender.set(i, toRender.get(j));
                    toRender.set(j, temp);

                }
            }
        }
        //bind the VAO
        GL30.glBindVertexArray(vaoId);
        //upload the camera's position to the shader
        program.uploadVec3Uniform("camPos", scene.getCamera().pos);
        //upload the active texture index
        program.uploadIntUniform("tex", 0);
        //upload the light data
        loadLight(program, scene.getLight());
        Material lastMat = null;
        for (Entity entity : toRender) {
            //load the material if it changed
            if (entity.getAsset().getMaterial() != lastMat) {
                loadMaterial(entity.getAsset().getMaterial());
                lastMat = entity.getAsset().getMaterial();
            }
            loadMVP(program, entity.getModelMat(), scene.getCamera());
            //get the entity's mesh's position in the IBO
            IBOPosition iboPos = iboPositions.get(entity.getAsset().getMesh());
            //Draw the mesh. Multiply the iboOffset by 4, because there are 4 bytes in an unsigned int, and the offset is in bytes.
            GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, iboPos.indexCount, GL11.GL_UNSIGNED_INT, iboPos.iboOffset * 4, iboPos.baseVertex);
        }
        //rebind the vao to 0 for cleanup.
        GL30.glBindVertexArray(0);
        //clear the render array
        toRender.clear();
    }

    private void loadLight(ShaderProgram program, Light light) {
        program.uploadVec3Uniform("light.pos", light.pos);
        program.uploadVec3Uniform("light.color", light.color);
        program.uploadFloatUniform("light.attenuationFac", light.attenuationFac);
        program.uploadFloatUniform("light.ambientFac", light.ambientFac);
    }

    private void loadMVP(ShaderProgram program, Matrix4f modelMat, Camera3D camera) {
        //multiply it by the camera's VP matrix
        program.uploadMat4Uniform("MVP", new Matrix4f().mul(camera.getVP()).mul(modelMat));
    }

    private void loadMaterial(Material material) {
        //activate the material's shader
        material.getProgram().use();
        //bind the material's texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getTexture().getId());
        //upload any other uniforms
        for (Map.Entry uniform : material.getUniformValues().entrySet()) {
            uploadUniform(material.getProgram(), (String)uniform.getKey(), (UniformValue)uniform.getValue());
        }
    }

    private void uploadUniform(ShaderProgram program, String name, UniformValue uniform) {
        //call the proper upload method based on the uniformvalue's type
        switch (uniform.type) {
            case VEC3:
                program.uploadVec3Uniform(name, uniform.getVector3fValue());
                break;
            case FLOAT:
                program.uploadFloatUniform(name, uniform.getFloatValue());
        }
    }

    private void createVBO(FloatBuffer vertexes) {
        if (vboId != 0) {
            //delete and reset the id
            GL15.glDeleteBuffers(vboId);
            vboId = 0;
        }
        //create a new vbo
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexes, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void createIBO(IntBuffer indexes) {
        if (iboId != 0) {
            //delete and reset
            GL15.glDeleteBuffers(iboId);
        }
        //create a new ibo
        iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexes, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void createVAO() {
        if (vaoId != 0) {
            //delete and reset
            GL30.glDeleteVertexArrays(vaoId);
            vaoId = 0;
        }
        //create a new vao
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
        //bind the vertex and index buffers
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        //set up the vertex attributes
        //position attrib
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.VERTEX_BYTES, Vertex.POS_OFFSET);
        //color attrib
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, Vertex.VERTEX_BYTES, Vertex.COLOR_OFFSET);
        //normal attrib
        GL20.glEnableVertexAttribArray(2);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, Vertex.VERTEX_BYTES, Vertex.NORMAL_OFFSET);
        //UV attrib
        GL20.glEnableVertexAttribArray(3);
        GL20.glVertexAttribPointer(3, 2, GL11.GL_FLOAT, false, Vertex.VERTEX_BYTES, Vertex.UV_OFFSET);
        //unbind the vao and the buffers
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

}
