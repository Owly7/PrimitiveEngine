package com.github.owly7.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private long id;
    private String title;
    private int width;
    private int height;

    public Window(String title, int width, int height) {
        glfwInit();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == NULL) {
            throw new RuntimeException("Error! Window not created");
        }

        glfwSetFramebufferSizeCallback(id, this::resizeCallback);

        glfwMakeContextCurrent(id);
        glfwSwapInterval(1);
    }

    public void show() {
        glfwShowWindow(id);
    }

    public long getId() {
        return id;
    }

    public void resizeCallback(long id, int width, int height) {
        glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;

        Main.INSTANCE.getApp().render();
        glfwSwapBuffers(id);
    }

    public void dispose() {
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);
        glfwTerminate();
    }
}
