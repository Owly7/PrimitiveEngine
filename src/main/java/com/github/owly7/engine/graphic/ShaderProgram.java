package com.github.owly7.engine.graphic;

import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL33.*;

public class ShaderProgram {
    private final int id;

    public ShaderProgram(int id) {
        this.id = id;
    }

    public static ShaderProgram create(String... shaders) {
        if (shaders.length < 2) return null;

        int shaderProgramID = glCreateProgram();
        int[] shadersID = new int[shaders.length];
        for (int i = 0; i < shaders.length; i++) {
            boolean isFragmentShader = (i + 1) % 2 == 0;
            int shaderId = createShader(shaders[i], isFragmentShader ? GL_FRAGMENT_SHADER : GL_VERTEX_SHADER);
            glAttachShader(shaderProgramID, shaderId);

            shadersID[i] = shaderId;
        }
        glLinkProgram(shaderProgramID);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pSuccess = stack.callocInt(1);
            glGetProgramiv(shaderProgramID, GL_LINK_STATUS, pSuccess);
            if (pSuccess.get(0) != GL_TRUE) {
                throw new RuntimeException(glGetProgramInfoLog(shaderProgramID));
            }
        }

        for (int j : shadersID) {
            glDeleteShader(j);
        }

        return new ShaderProgram(shaderProgramID);
    }

    public static int createShader(String shader, int shaderType) {
        int shaderID = glCreateShader(shaderType);

        InputStream in = ShaderProgram.class.getResourceAsStream(shader);
        if (in != null && shaderID != 0) {
            try (
                    MemoryStack stack = MemoryStack.stackPush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in))
            ) {
                glShaderSource(
                        shaderID,
                        reader.lines().collect(Collectors.joining("\n"))
                );
                glCompileShader(shaderID);

                IntBuffer pSuccess = stack.callocInt(1);
                glGetShaderiv(shaderID, GL_COMPILE_STATUS, pSuccess);
                if (pSuccess.get(0) != GL_TRUE) {
                    throw new RuntimeException(glGetShaderInfoLog(shaderID));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return shaderID;
    }

    public void use() {
        glUseProgram(id);
    }

    public void setBoolean(String name, boolean value) {
        setInt(name, value ? 1 : 0);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(id, name), value);
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(id, name), value);
    }

    public int getId() {
        return id;
    }

    public void dispose() {
        glDeleteProgram(id);
    }
}
