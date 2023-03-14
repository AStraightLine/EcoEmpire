package com.core;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.core.climate.Climate;
import com.core.clock.GameClock;
import com.core.map.grid.MapGrid;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.map.offset.offsets.Lobby;
import com.core.map.offset.offsets.Tree;
import com.core.player.PlayerInventory;
import com.core.ui.UI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Stage stage;
    private FitViewport viewport;
    private FitViewport uiViewport;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private World world;
    private GameClock gameClock;
    private MapGrid grid;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private PlayerInventory playerInventory;
    private Climate climate;
    private CameraInputs camImp;
    private Skin skin = new Skin();
    private ProgressBar impactBar;
    private Sprite endScreen;
    private boolean end = false;
    private boolean drawActionCorrection = false;
    private SpriteBatch endBatch = new SpriteBatch();

    private SpriteBatch playerActionBatch = new SpriteBatch();
    private float startDisplayedTime;
    private Sprite buildBeforeSearch;
    private Sprite insufficientFundsSprite;


    private UI ui;
    private int gameWidth, gameHeight;

    private double startingFunds = 100.0;

    private Boolean insufficientFunds = false;

    public GameScreen(OrthographicCamera camera, int resolutionX, int resolutionY) {
        Rectangle levelBounds = new Rectangle(0, 0, 800, 600);
        int gameWidth = (int)(resolutionX-resolutionX*0.13f), gameHeight = (int)(resolutionY-resolutionY*0.06f);

        this.camera = camera;
        this.camera.position.set(new Vector3(gameWidth / 2, gameHeight / 2, 0));
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.batch = new SpriteBatch();
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

        uiViewport = new FitViewport(resolutionX, resolutionY);
        this.ui = new UI(uiViewport,this,resolutionX, resolutionY, gameWidth, gameHeight, playerInventory, climate, gameClock, inputMultiplexer);

        this.viewport = new FitViewport(gameWidth, gameHeight, camera);


        stage = new Stage(viewport);
        this.grid = new MapGrid(this,334, 203, stage, gameWidth, gameHeight, inputMultiplexer, climate, playerInventory, ui); //384, 360 previously //320 216 good //416 252
        this.camImp = new CameraInputs(camera, inputMultiplexer, viewport);
        camImp.create();

        grid.create();
        Gdx.input.setInputProcessor(inputMultiplexer);

        ui.setGrid(grid);
    }

    public void reactivateGameInputs() {Gdx.input.setInputProcessor(inputMultiplexer);} //Needed to allow the player to use game inputs after a pause

    public void resize(int width, int height)
    {
        System.out.println("resized window");
        viewport.update((int)(width-width*0.13f), (int)(height-height*0.06f));
        uiViewport.update(width, height);
        camImp.cameraBounds();
    }

    public void update() {
        world.step(1 / 60f, 6, 2);

        this.camera.update();

        if(climate.getClimateHealth() <= 0 || playerInventory.getFunds() < 0)
        {
            gameClock.handlePause();

            this.endScreen = new Sprite(new Texture("endScreen.png"));
            end = true;
        }

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
            Boot.INSTANCE.displayMenu(end);
        }

        // ALL CONTROLS IN HERE:
        // IF THE GAME IS PAUSED THEY SHOULDN'T OPERATE:
        if (!gameClock.getPauseState()) {
            // Start of search and Extract functions
            // Search:
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                TileActor tile = grid.getSelectedTile();
                Location location = tile.getLocation();

                if (!location.getSearched() && !grid.getSelectedTile().isUnavailable()) {
                    if (playerInventory.getFunds() >= location.getSearchCost()) {
                        playerInventory.charge(location.getSearchCost());
                        location.setSearched(true);
                        ui.handleTileSelection(tile); // Update sideUI to show resource details
                    }
                } else {
                    ui.handleTileSelection(tile);
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
                        ui.handleTileSelection(tile);
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
                        ui.handleTileSelection(tile);
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
                        ui.handleTileSelection(tile);
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
                        ui.handleTileSelection(tile);
                    }
                }
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                grid.deleteExtractor(playerInventory);
                ui.handleTileSelection(grid.getSelectedTile());
            }

            // Start of Offsets

            // Tree is a little different as it requires a tile to be selected first, not all offsets will follow this control pattern flow
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                Tree tree = new Tree();
                grid.addTree(tree);
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

        if(end == true)
        {
            endBatch.begin();
            endBatch.draw(endScreen, Gdx.graphics.getWidth()/2 - endScreen.getWidth()/2, Gdx.graphics.getHeight()/2 - endScreen.getHeight()/2);
            endBatch.end();


            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                Boot.INSTANCE.displayMenu(end);
            }

            return;
        }

        if(drawActionCorrection){
            playerActionBatch.begin();
            playerActionBatch.draw(buildBeforeSearch,Gdx.graphics.getWidth()/2 - buildBeforeSearch.getWidth()/2, Gdx.graphics.getHeight()/2 - buildBeforeSearch.getHeight()/2);
            playerActionBatch.end();
        }


        update();
        Gdx.gl.glClearColor(25/255f, 25/255f, 25/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();


        batch.begin();

        viewport.apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();




        batch.end();

        //drawTime(hudBatch, gameClock.getTimeElapsedInSeconds(), Boot.INSTANCE.getScreenWidth() - 86, Boot.INSTANCE.getScreenHeight() - 36);
        //drawFunds(hudBatch, playerInventory.getFunds(), 86, Boot.INSTANCE.getScreenHeight() - 36);
        //drawExpectedFundsChange(hudBatch, playerInventory.getIncome(), 200, Boot.INSTANCE.getScreenHeight() - 36);
        //drawClimate(hudBatch, climate.getClimateHealth(), 86, Boot.INSTANCE.getScreenHeight() - 56);
        //drawExpectedClimateChange(hudBatch, (playerInventory.getClimateImpact() / baseHealth) * 100, 200, Boot.INSTANCE.getScreenHeight() - 56);

        // Has its own batch (I think) so outside out begin/end
        uiViewport.apply();


        ui.update();

        if (this.insufficientFunds == true) {
            playerActionBatch.begin();
            playerActionBatch.draw(this.insufficientFundsSprite, Gdx.graphics.getWidth() / 2 - insufficientFundsSprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - insufficientFundsSprite.getHeight() / 2);
            playerActionBatch.end();
            if(this.gameClock.getTimeElapsedInSeconds() > (this.startDisplayedTime + Const.ON_SCREEN_TIME)){
                this.insufficientFunds = false;
            }
        }

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

    public void displayInsufficientFunds(){

        this.insufficientFundsSprite = new Sprite(new Texture("insufficient-funds.png"));
        this.insufficientFunds = true;
        this.startDisplayedTime = this.gameClock.getTimeElapsedInSeconds();
    }


    public GameClock getGameClock() {
        return gameClock;
    }
}
