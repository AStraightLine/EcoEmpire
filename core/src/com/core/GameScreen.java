package com.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
    public final static float HEIGHT = 100;
    public final static float WIDTH = 16 * HEIGHT / 9;

    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.world = new World(new Vector2(0, 0), false);
        this.gameClock = new GameClock();


        this.viewport = new FitViewport(1280, 720, camera);

        stage = new Stage(viewport);
        this.grid = new MapGrid(40, 40, stage);

        grid.create();

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

        batch.begin();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        drawTime(batch, gameClock.getTimeElapsedInSeconds(), Boot.INSTANCE.getScreenWidth() - 86, Boot.INSTANCE.getScreenHeight() - 36);

        batch.end();

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
