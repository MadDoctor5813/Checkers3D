package com.kevin.graphics;

/**
 * Created by Kevin on 2015-12-15.
 */

//Stores the location of a mesh's vertex data in the global VBO.
public class IBOPosition {

    //the number of indexes until the mesh's data begins
    public int iboOffset;
    //the number of indexes that make up the vertex data
    public int indexCount;
    //a integer to be added to every index to account for the fact that other vertexes
    //may come before this one.
    public int baseVertex;

    public IBOPosition(int iboOffset, int indexCount, int baseVertex) {
        this.iboOffset = iboOffset;
        this.indexCount = indexCount;
        this.baseVertex = baseVertex;
    }
}
