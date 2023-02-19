package com.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.core.climate.Climate;
import com.core.clock.GameClock;
import com.core.map.grid.MapGrid;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.map.offset.offsets.Lobby;
import com.core.map.offset.offsets.Tree;
import com.core.player.PlayerInventory;
import com.core.ui.UI;

import static com.core.Const.baseHealth;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Stage stage;
    private ExtendViewport viewport;
    private ScreenViewport uiViewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private World world;
    private GameClock gameClock;
    private MapGrid grid;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private SpriteBatch hudBatch;
    private Stage hudStage;
    private PlayerInventory playerInventory;
    private Climate climate;
    private CameraInputs camImp;
    private Skin skin = new Skin();
    private ProgressBar impactBar;

    private UI ui;
    private int gameWidth, gameHeight;

    private double startingFunds = 100.0;

    public GameScreen(OrthographicCamera camera, int resolutionX, int resolutionY) {
        Rectangle levelBounds = new Rectangle(0, 0, 800, 600);
        int gameWidth = (resolutionX / 15) * 13, gameHeight = (resolutionY / 15) * 14;

        this.camera = camera;
        this.camera.position.set(new Vector3(gameWidth / 2, gameHeight / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
        this.hudBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.world = new World(new Vector2(0, 0), false);
        this.playerInventory = new PlayerInventory(startingFunds);

        skin.addRegions(new TextureAtlas("biological-attack/skin/biological-attack-ui.atlas"));
        skin.load(Gdx.files.internal("biological-attack/skin/biological-attack-ui.json"));
        impactBar = new ProgressBar(0.0f, 1000, 0.01f, false, skin);
        impactBar.setValue(100);
        impactBar.setAnimateDuration(1);

        this.climate = new Climate(impactBar);
        this.gameClock = new GameClock(playerInventory, climate);

        //GameSound.startBackgroundMusic(0.1F);

        this.viewport = new ExtendViewport(gameWidth, gameHeight, camera);
        stage = new Stage(viewport);
        this.grid = new MapGrid(416, 252, stage, gameWidth, gameHeight, inputMultiplexer, climate, playerInventory); //384, 360 previously //320 216 good
        this.camImp = new CameraInputs(camera, inputMultiplexer, viewport);
        camImp.create();

        grid.create();
        Gdx.input.setInputProcessor(inputMultiplexer);


        this.hudStage = new Stage();
        hudStage.addActor(impactBar);



        uiViewport = new ScreenViewport();
        this.ui = new UI(uiViewport, resolutionX, resolutionY, gameWidth, gameHeight, playerInventory, climate, gameClock, inputMultiplexer);
    }

    public void reactivateGameInputs() {Gdx.input.setInputProcessor(inputMultiplexer);} //Needed to allow the player to use game inputs after a pause

    public void resize(int width, int height)
    {
        System.out.println("resized window");
        viewport.update(width/15*13, height/15*14);
    }
    public CameraInputs getCamInp()
    {
        return camImp;
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Boot.INSTANCE.displayMenu();
        }

        // ALL CONTROLS IN HERE:
        // IF THE GAME IS PAUSED THEY SHOULDN'T OPERATE:
        if (!gameClock.getPauseState()) {
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
                String path;
                Boolean built = false;
                if (location.getSearched() && playerInventory.getFunds() >= location.getOil().getExtractionCost()) {
                    if (type == Const.water) {
                        path = "sea-rig.png";
                    } else {
                        path = "land-rig.png";
                    }
                    built = grid.addExtractor(location, Const.oil, path);
                    if (built) {
                        playerInventory.addExtractor(location.getExtractor(), location.getOil().getExtractionCost());
                    }
                }
            }

            // Coal Extractor
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                TileActor tile = grid.getSelectedTile();
                Location location = tile.getLocation();
                String type = location.getType();
                String path;
                Boolean built = false;

                if (location.getSearched() && playerInventory.getFunds() >= location.getCoal().getExtractionCost()) {
                    if (type == Const.water) {
                        path = "sea-coal-mine.png";
                    }
                    else {
                        path = "land-coal-mine.png";
                    }
                    built = grid.addExtractor(location, Const.coal, path);
                    if (built) {
                        playerInventory.addExtractor(location.getExtractor(), location.getCoal().getExtractionCost());
                    }
                }
            }

            //Gas extractor
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                TileActor tile = grid.getSelectedTile();
                Location location = tile.getLocation();
                String type = location.getType();
                String path;
                Boolean built = false;

                if (location.getSearched() && playerInventory.getFunds() >= location.getGas().getExtractionCost()) {
                    if (type == Const.water) {
                        path = "sea-gas.png";
                    }
                    else {
                        path = "land-gas.png";
                    }
                    built = grid.addExtractor(location, Const.gas, path);
                    if (built) {
                        playerInventory.addExtractor(location.getExtractor(), location.getGas().getExtractionCost());
                    }
                }
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                TileActor tile = grid.getSelectedTile();
                Location location = tile.getLocation();
                String type = location.getType();
                String path;
                Boolean built = false;
                if (location.getSearched() && playerInventory.getFunds() >= location.getNuclear().getExtractionCost()) {
                    if (type == Const.water) {
                        System.out.println("cant use nuclear on sea tile");
                        return;
                    }
                    else {
                        path = "nuclear.png";
                    }
                    built = grid.addExtractor(location, Const.nuclear, path);
                    if (built) {
                        playerInventory.addExtractor(location.getExtractor(), location.getNuclear().getExtractionCost());
                    }
                }
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                grid.deleteExtractor(playerInventory);
            }

            // Start of Offsets

            // Tree is a little different as it requires a tile to be selected first, not all offsets will follow this control pattern flow
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                TileActor tile = grid.getSelectedTile();
                Location location = tile.getLocation();
                boolean possible = grid.tryTree(tile, Const.treeY, Const.treeX);

                if (possible) {
                    Tree tree = new Tree();

                    if (playerInventory.getFunds() >= tree.getCost()) {
                        location.setOffset(tree);
                        location.setHasOffset(true);
                        playerInventory.addOffset(tree);
                    }
                }
            }

            // Lobby
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                Lobby lobby = new Lobby();
                if (playerInventory.getFunds() >= lobby.getCost()) {
                    playerInventory.addOffset(lobby);
                }
            }
        }
        // End of controls section.

        batch.setProjectionMatrix(camera.combined);
    }



    @Override
    public void render(float delta) {
        update();
        Gdx.gl.glClearColor(25/255f, 25/255f, 25/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        // Has its own batch (I think) so outside out begin/end
        uiViewport.apply();
        ui.update();

        batch.begin();

        viewport.apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        batch.end();

        hudBatch.begin();
        //drawTime(hudBatch, gameClock.getTimeElapsedInSeconds(), Boot.INSTANCE.getScreenWidth() - 86, Boot.INSTANCE.getScreenHeight() - 36);
        //drawFunds(hudBatch, playerInventory.getFunds(), 86, Boot.INSTANCE.getScreenHeight() - 36);
        //drawExpectedFundsChange(hudBatch, playerInventory.getIncome(), 200, Boot.INSTANCE.getScreenHeight() - 36);
        //drawClimate(hudBatch, climate.getClimateHealth(), 86, Boot.INSTANCE.getScreenHeight() - 56);
        //drawExpectedClimateChange(hudBatch, (playerInventory.getClimateImpact() / baseHealth) * 100, 200, Boot.INSTANCE.getScreenHeight() - 56);

        hudStage.act(Gdx.graphics.getDeltaTime());
        hudStage.draw();

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

    private void drawFunds(SpriteBatch batch, double funds, float x, float y) {
        String fundsString = "";

        if (funds < 0) {
            fundsString = String.format("-£%,.2f", -funds);
        } else {
            fundsString = String.format("£%,.2f", funds);
        }
        font.draw(batch, fundsString, x, y);
    }

    private void drawExpectedFundsChange(SpriteBatch batch, double funds, float x, float y) {
        String expected = "";

        if (funds < 0) {
            expected = String.format("-£%,.2f", -funds);
        } else {
            expected = String.format("+£%,.2f", funds);
        }

        font.draw(batch, expected, x, y);
    }

    private void drawClimate(SpriteBatch batch, double climateHealth, float x, float y) {
        String climateString = String.format("%,.2f", climateHealth);
        font.draw(batch, climateString, x, y);
    }

    private void drawExpectedClimateChange(SpriteBatch batch, double climateChange, float x, float y) {
        String expectedChange = "";

        if (climateChange < 0) { //Only displays user influenced climate change (no natural increase/decrease)
            expectedChange = String.format("+%,.2f", -climateChange); //Negative climate change actually means a climate increase (All climate impacts coded using positive numbers, so a bigger value means more climate decrease)
        } else if (climateChange == 0) {
            expectedChange = String.format("+%,.2f", climateChange);
        } else {
            expectedChange = String.format("-%,.2f", climateChange);
        }

        font.draw(batch, expectedChange, x, y);
    }

    public GameClock getGameClock() {
        return gameClock;
    }
}
