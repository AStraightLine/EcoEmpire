package com.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.core.audio.GameSound;
import com.core.clock.GameClock;
import com.core.map.MapGrid;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Stage stage;
    private FitViewport viewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private World world;
    private GameClock gameClock;
    private MapGrid grid;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private SpriteBatch hudBatch;
    private GameSound gameSound;

    public GameScreen(OrthographicCamera camera, int resolutionX, int resolutionY) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.world = new World(new Vector2(0, 0), false);
        this.gameClock = new GameClock();
        this.gameSound = new GameSound();

        this.gameSound.startBackgroundMusic(0.5F);

        this.viewport = new FitViewport(resolutionX, resolutionY, camera);

        stage = new Stage(viewport);
        this.grid = new MapGrid(80, 80, stage, inputMultiplexer);

        CameraInputs camImp = new CameraInputs(camera, inputMultiplexer, viewport);
        camImp.create();
        grid.create();
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    public void resize(int width, int height)
    {
        System.out.println("resized window");
        viewport.update(width, height);
    }



    public void update() {
        world.step(1 / 60f, 6, 2);

        this.camera.update();

        // Start of controls section:
        // Pause:
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            gameClock.handlePause();
            // Add additional functionality such as restricting access to gameplay options as they're implemented here:
        }
        // Increase game speed:
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) { // Right arrow key
            gameClock.incTimeMod();
        }
        // Decrease game speed:
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) { // Right arrow key
            gameClock.decTimeMod();
        }
        // End of controls section.

        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.begin();

        grid.addExtractor();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.end();

        hudBatch.begin();
        drawTime(hudBatch, gameClock.getTimeElapsedInSeconds(), Boot.INSTANCE.getScreenWidth() - 86, Boot.INSTANCE.getScreenHeight() - 36);
        hudBatch.end();

        // For debugging purposes:
        this.box2DDebugRenderer.render(world, camera.combined.scl(Const.PPM));
    }

    private void drawTime(SpriteBatch batch, float timeElapsedInSeconds, float x, float y) {
        int hours = Math.round(timeElapsedInSeconds) / 3600;
        int minutes = Math.round((timeElapsedInSeconds) % 3600) / 60;
        int seconds = Math.round(timeElapsedInSeconds) % 60;
        String timeElapsed = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        font.draw(batch, timeElapsed, x, y);
    }


}
