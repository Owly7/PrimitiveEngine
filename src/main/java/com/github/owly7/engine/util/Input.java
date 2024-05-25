package com.github.owly7.engine.util;

import com.github.owly7.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input {
    private static Input INSTANCE;
    public final Window window;

    private Input(Window window) {
        this.window = window;
        INSTANCE = this;
    }

    public static void init(Window window) {
        if (INSTANCE == null)
            INSTANCE = new Input(window);
    }

    public static Input getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Input is not initialized!");
        return INSTANCE;
    }

    public static boolean isKeyPressed(int key) {
        return glfwGetKey(getInstance().window.getId(), key) == GLFW_PRESS;
    }
}
