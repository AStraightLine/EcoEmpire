package com.core;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
    }

}
