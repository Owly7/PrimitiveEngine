package com.github.owly7.engine.graphic;

import com.github.owly7.engine.graphic.shape.Rectangle;

public class Sprite {
    private final Texture texture;
    private final Rectangle shape;

    public Sprite(String path) {
        texture = Texture.create(path);
        shape = new Rectangle();
    }

    public void draw(ShaderProgram shaderProgram) {
        texture.bind();
        shape.draw(shaderProgram);
    }

    public void dispose() {
        texture.dispose();
        shape.dispose();
    }
}
