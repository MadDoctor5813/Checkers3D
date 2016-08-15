package com.kevin.asset;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL21;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * Created by Kevin on 2015-12-18.
 */

//Loads texture data, uploads it into the GPU, and keeps an OpenGL binding to access it later.
public class Texture {

    public int getId() {
        return id;
    }

    private int id = 0;

    public Texture(Path path) {
        uploadTexture(loadDecoder(path));
    }

    //Uses the given PNG decoder to upload the image to the GPU.
    private void uploadTexture(PNGDecoder image) {
        //create a buffer to hold the texture data
        ByteBuffer buf = BufferUtils.createByteBuffer(4 * image.getHeight() * image.getWidth());
        try {
            //multiply by 4 because 4 bytes per pixel
            image.decode(buf, 4 * image.getWidth(), PNGDecoder.Format.RGBA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buf.flip();
        //generate the texture
        id = GL11.glGenTextures();
        //bind it
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        //set up texture paramaters, like filtering and texture coord wrapping
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        //upload the texture to the GPU
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL21.GL_SRGB_ALPHA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
        //unbind the texture to clean up
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    //loads a PNG decoder from the given path.
    private PNGDecoder loadDecoder(Path path) {
        InputStream imgStream = this.getClass().getClassLoader().getResourceAsStream(path.toString());
        try {
            PNGDecoder decoder = new PNGDecoder(imgStream);
            return decoder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
