package com.github.owly7.engine.graphic.shape;

import com.github.owly7.engine.graphic.ShaderProgram;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.*;

public class Rectangle {
    private final int vertexArrayObject;
    private final int vertexBufferObject;
    private final int elementBufferObject;

    public Rectangle() {
        float[] vertices = {
                -0.5f, -0.5f, 0f, 0f, 0f,
                0.5f, -0.5f, 0f, 1f, 0f,
                0.5f, 0.5f, 0f, 1f, 1f,
                -0.5f, 0.5f, 0f, 0f, 1f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        vertexArrayObject = glGenVertexArrays();
        glBindVertexArray(vertexArrayObject);

        vertexBufferObject = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);

        elementBufferObject = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObject);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer pVertices = stack.callocFloat(vertices.length);
            pVertices.put(vertices).flip();
            glBufferData(GL_ARRAY_BUFFER, pVertices, GL_STATIC_DRAW);

            IntBuffer pIndices = stack.callocInt(indices.length);
            pIndices.put(indices).flip();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, pIndices, GL_STATIC_DRAW);
        }

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void draw(ShaderProgram shaderProgram) {
        shaderProgram.use();
        glBindVertexArray(vertexArrayObject);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void dispose() {
        glDeleteVertexArrays(vertexArrayObject);
        glDeleteBuffers(vertexBufferObject);
        glDeleteBuffers(elementBufferObject);
    }
}
