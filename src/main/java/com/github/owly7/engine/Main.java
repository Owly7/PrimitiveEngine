package com.github.owly7.engine;

import com.github.owly7.engine.util.Input;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {
    public static Main INSTANCE;
    private final Window window;
    private final ApplicationListener app;

    private Main(ApplicationListener app) {
        window = new Window("OpenGL - PrimitiveEngine", 512, 736);
        Input.init(window);
        this.app = app;
        INSTANCE = this;
    }

    public static void main(String[] args) {
        Configuration.STACK_SIZE.set(1024);
        new Main(new Application()).run();
    }

    public void run() {
        window.show();
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        app.create();

        long deltaTime;
        long lastTime = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window.getId())) {
            long currenTime = System.currentTimeMillis();
            deltaTime = currenTime - lastTime;
            lastTime = currenTime;

            app.update(deltaTime / 1000f);
            app.render();

            glfwSwapBuffers(window.getId());
            glfwPollEvents();
        }

        app.dispose();
        window.dispose();
    }

    public Window getWindow() {
        return window;
    }

    public ApplicationListener getApp() {
        return app;
    }
}
