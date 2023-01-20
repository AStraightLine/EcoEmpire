package com.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

//Testing commiting
public class MainMenuScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    public MainMenuScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
    }

    public void update() {
        this.camera.update();

        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.end();

        // For debugging purposes:
        //this.box2DDebugRenderer.render(world, camera.combined.scl(Const.PPM)); // Will need to add our PPM or ignore it if not applicable (I.E. remove the argument)
    }
}
