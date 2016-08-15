package com.kevin.asset;

import com.kevin.graphics.Vertex;
import org.joml.Vector3f;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Created by 532259 on 12/8/2015.
 */
//Stores vertex, e.g, positions, colors, texture coords, normals.
//Loaded from resources/meshes, and uses a custom data format, .jmdl. The format is provided in the project folder.
public class Mesh {

    private float[] vertices;
    private int[] indices;


    public Mesh(Path path) {
        loadMesh(path);
    }

    private void loadMesh(Path path) {
        //open the file
        InputStream modelStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        try (DataInputStream reader = new DataInputStream(modelStream)) {
            //read the file header
            byte[] header = new byte[4];
            reader.read(header);
            String headerStr = new String(header, "UTF-8");
            if (!headerStr.equals("JMDL")) {
                System.out.printf("Model load error for %s. Invalid file header.", path.toString());
                return;
            }
            int numVertices = reader.readInt();
            //initialize the vertex array
            vertices = new float[numVertices * Vertex.VERTEX_FLOATS];
            //load in all vertex data
            for (int i = 0; i < numVertices * Vertex.VERTEX_FLOATS; i++ ) {
                vertices[i] = reader.readFloat();
            }
            //read in index data
            int numIndices = reader.readInt();
            indices = new int[numIndices];
            for (int i = 0; i < numIndices; i++) {
               indices[i] = reader.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }
}
