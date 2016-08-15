package com.kevin.graphics;

/**
 * Created by Kevin on 2015-12-13.
 */
public class Vertex {

    /*
    This class just holds various offsets and constants.
    The basic structure of the vertex is:
    3 floats (vertex positions)
    4 floats (vertex colors)
    3 floats (vertex normals)
    2 floats (UV coordinates)
     */

    public static final int FLOAT_BYTES = Float.SIZE / 8;

    public static final int POS_FLOATS = 3;
    public static final int COLOR_FLOATS = 4;
    public static final int NORMAL_FLOATS = 3;
    public static final int UV_FLOATS = 2;

    public static final int POS_BYTES = FLOAT_BYTES * POS_FLOATS;
    public static final int COLOR_BYTES = FLOAT_BYTES * COLOR_FLOATS;
    public static final int NORMAL_BYTES = FLOAT_BYTES * NORMAL_FLOATS;
    public static final int UV_BYTES = FLOAT_BYTES * UV_FLOATS;

    public static final int POS_OFFSET_FLOATS = 0;
    public static final int COLOR_OFFSET_FLOATS = POS_FLOATS;
    public static final int NORMAL_OFFSET_FLOATS = POS_FLOATS + COLOR_FLOATS;
    public static final int UV_OFFSET_FLOATS = POS_FLOATS + COLOR_FLOATS + NORMAL_FLOATS;

    public static final int POS_OFFSET = 0;
    public static final int COLOR_OFFSET = POS_BYTES;
    public static final int NORMAL_OFFSET = POS_BYTES + COLOR_BYTES;
    public static final int UV_OFFSET = POS_BYTES + COLOR_BYTES + NORMAL_BYTES;

    public static final int VERTEX_BYTES = POS_BYTES + COLOR_BYTES + NORMAL_BYTES + UV_BYTES;
    public static final int VERTEX_FLOATS = POS_FLOATS + COLOR_FLOATS + NORMAL_FLOATS + UV_FLOATS;


}
