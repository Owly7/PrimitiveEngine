package com.github.owly7.engine;

public interface ApplicationListener {
    void create();

    void update(float deltaTime);

    void render();

    void dispose();
}
