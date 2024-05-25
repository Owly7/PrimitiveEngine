package com.github.owly7.engine;

import com.github.owly7.engine.graphic.ShaderProgram;
import com.github.owly7.engine.graphic.Sprite;
import com.github.owly7.engine.util.Input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL33.*;

public class Application implements ApplicationListener {
    private ShaderProgram shaderProgram;
    private Sprite sprite;

    @Override
    public void create() {
        shaderProgram = ShaderProgram.create(
                "/shader/core.vsh",
                "/shader/core.fsh"
        );

        sprite = new Sprite("/texture/image.png");
    }

    @Override
    public void update(float deltaTime) {
        if (Input.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(Main.INSTANCE.getWindow().getId(), true);
        }
    }

    @Override
    public void render() {
        glClearColor(0.2f, 0.3f, 0.5f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        sprite.draw(shaderProgram);
    }

    @Override
    public void dispose() {
        sprite.dispose();
        shaderProgram.dispose();
    }
}
