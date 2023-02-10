package com.core;

import com.badlogic.gdx.*;
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
import com.core.audio.GameSound;
import com.core.clock.GameClock;
import com.core.map.grid.MapGrid;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.player.PlayerInventory;

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
    private PlayerInventory playerInventory;

    private double startingFunds = 100.0;

    public GameScreen(OrthographicCamera camera, int resolutionX, int resolutionY) {
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth() / 2, Boot.INSTANCE.getScreenHeight() / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.world = new World(new Vector2(0, 0), false);
        this.playerInventory = new PlayerInventory(startingFunds);
        this.gameClock = new GameClock();


        GameSound.startBackgroundMusic(0.1F);

        this.viewport = new FitViewport(resolutionX, resolutionY, camera);

        stage = new Stage(viewport);
        this.grid = new MapGrid(350, 350, stage, inputMultiplexer);
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

        // Start of search and Extract functions
        // Search:
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            TileActor tile = grid.getSelectedTile();
            Location location = tile.getLocation();

            if (!location.getSearched()) {
                if (playerInventory.getFunds() >= location.getSearchCost())
                    playerInventory.charge(location.getSearchCost());
                    location.setSearched(true);

                /**
                 * Only here to demonstrate what the Resource profile looks like, I.E. the data to display somehow to the user.
                 *
                 * System.out.println("Search Cost: " + location.getSearchCost())
                 * System.out.println("Coal - Value: " + location.getCoal().getValue() + " Quantity: " + location.getCoal().getQuantity() + " Impact: " + location.getCoal().getImpact() + " Stability: " + location.getCoal().getStability() + " Extraction Cost: " + location.getCoal().getExtractionCost());
                 * System.out.println("Gas - Value: " + location.getGas().getValue() + " Quantity: " + location.getGas().getQuantity() + " Impact: " + location.getGas().getImpact() + " Stability: " + location.getGas().getStability() + " Extraction Cost: " + location.getGas().getExtractionCost());
                 * System.out.println("Oil - Value: " + location.getOil().getValue() + " Quantity: " + location.getOil().getQuantity() + " Impact: " + location.getOil().getImpact() + " Stability: " + location.getOil().getStability() + " Extraction Cost: " + location.getOil().getExtractionCost());
                 * System.out.println("Solar - Value: " + location.getSolar().getValue() + " Quantity: " + location.getSolar().getQuantity() + " Impact: " + location.getSolar().getImpact() + " Stability: " + location.getSolar().getStability() + " Extraction Cost: " + location.getSolar().getExtractionCost());
                 * System.out.println("Wind - Value: " + location.getWind().getValue() + " Quantity: " + location.getWind().getQuantity() + " Impact: " + location.getWind().getImpact() + " Stability: " + location.getWind().getStability() + " Extraction Cost: " + location.getWind().getExtractionCost());
                 * System.out.println("Hydro - Value: " + location.getHydro().getValue() + " Quantity: " + location.getHydro().getQuantity() + " Impact: " + location.getHydro().getImpact() + " Stability: " + location.getHydro().getStability() + " Extraction Cost: " + location.getHydro().getExtractionCost());
                 * System.out.println("Geothermal - Value: " + location.getGeothermal().getValue() + " Quantity: " + location.getGeothermal().getQuantity() + " Impact: " + location.getGeothermal().getImpact() + " Stability: " + location.getGeothermal().getStability() + " Extraction Cost: " + location.getGeothermal().getExtractionCost());
                 * System.out.println("Nuclear - Value: " + location.getNuclear().getValue() + " Quantity: " + location.getNuclear().getQuantity() + " Impact: " + location.getNuclear().getImpact() + " Stability: " + location.getNuclear().getStability() + " Extraction Cost: " + location.getNuclear().getExtractionCost());
                 */
            }

            System.out.println("Funds: " + playerInventory.getFunds());
        }

        // Add Oil extractor
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            TileActor tile = grid.getSelectedTile();
            Location location = tile.getLocation();
            String type = location.getType();

            if (location.getSearched() && playerInventory.getFunds() >= location.getOil().getExtractionCost()) {
                if (type == Const.water) {
                    grid.addExtractor(location, Const.oil, "sea-rig.png");
                } else {
                    grid.addExtractor(location, Const.oil, "land-rig.png");
                }
                playerInventory.addExtractor(location.getExtractor(), location.getOil().getExtractionCost());
            }

            System.out.println("Funds: " + playerInventory.getFunds());
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            System.exit(0);
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

        stage.draw();
        stage.act(Gdx.graphics.getDeltaTime());

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
