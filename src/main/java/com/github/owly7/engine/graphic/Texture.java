package com.github.owly7.engine.graphic;

import com.github.owly7.engine.util.Image;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture {
    private final int id;
    private final int width;
    private final int height;
    private final int channels;

    public Texture(int id, int width, int height, int channels) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.channels = channels;
    }

    public static Texture create(String texture) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        int width, height, channels;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            byte[] image = Image.readImage(texture);
            ByteBuffer pImage = stack.malloc(image.length);
            pImage.put(image).flip();

            IntBuffer pWidth = stack.callocInt(1);
            IntBuffer pHeight = stack.callocInt(1);
            IntBuffer pNChannels = stack.callocInt(1);

            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer pLoadedData = STBImage.stbi_load_from_memory(pImage, pWidth, pHeight, pNChannels, 0);
            if (pLoadedData != null) {
                channels = pNChannels.get(0);
                int format = GL_RGB;
                if (channels > 3) {
                    format = GL_RGBA;
                }
                glTexImage2D(GL_TEXTURE_2D, 0, format, width = pWidth.get(0), height = pHeight.get(0), 0, format, GL_UNSIGNED_BYTE, pLoadedData);
                glGenerateMipmap(GL_TEXTURE_2D);
                STBImage.stbi_image_free(pLoadedData);
            } else {
                throw new RuntimeException("Failed to load texture: " + texture);
            }
        }

        glBindTexture(GL_TEXTURE_2D, 0);

        return new Texture(textureID, width, height, channels);
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void dispose() {
        glDeleteTextures(1);
    }

    public int getId() {
        return this.id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getChannels() {
        return this.channels;
    }
}
