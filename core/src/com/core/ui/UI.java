package com.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.core.climate.Climate;
import com.core.clock.GameClock;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.player.PlayerInventory;

public class UI {

    private ScreenViewport viewport;
    private int resX, resY, gameWidth, gameHeight;

    private Skin skin;

    private Stage stage;
    private HorizontalGroup topUI;
    private VerticalGroup sideUI;
    private Table topTable, sideTable;

    private PlayerInventory inventory;
    private Climate climate;
    private GameClock clock;

    private Label timeLabel, fundsLabel, climateLabel, expectedFundsChange, expectedClimateChange;
    private ProgressBar impactBar;
    private SelectBox extractionSelect;
    private Group uiGroup = new Group();

    public UI(ScreenViewport viewport, int resX, int resY, int gameWidth, int gameHeight, PlayerInventory inventory, Climate climate, GameClock clock, InputMultiplexer inputMultiplexer) {
        this.viewport = viewport;
        this.resX = resX;
        this.resY = resY;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.inventory = inventory;
        this.climate = climate;
        this.clock = clock;

        skin = new Skin();
        skin.addRegions(new TextureAtlas("default/skin/uiskin.atlas"));
        skin.load(Gdx.files.internal("default/skin/uiskin.json"));

        stage = new Stage(viewport);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        topTable = new Table(skin);
        sideTable = new Table(skin);



        topUI = new HorizontalGroup();
        topUI.setBounds(0, resY - (resY - gameHeight), resX, resY - gameHeight);
        topUI.addActor(topTable);

        sideUI = new VerticalGroup();
        sideUI.setBounds(gameWidth, 0, resX - gameWidth, resY - topUI.getHeight());
        sideTable.setBounds(gameWidth, 0, resX - gameWidth, resY - topUI.getHeight());
        sideUI.addActor(sideTable);

        uiGroup.addActor(sideUI);
        uiGroup.addActor(topUI);
        uiGroup.setZIndex(1);

        stage.addActor(uiGroup);


        inputMultiplexer.addProcessor(stage);

        populateTopTable();
        populateSideTable();

    }

    public void update() {
        updateTime();
        updateFunds();
        updateClimate();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void populateTopTable() {
        populateTime();
        populateClimate();
        topTable.row().pad(5, 0, 5, 0);
        populateFunds();
        populateExtractionsSelection();
    }

    public void populateSideTable() {

    }

    public void populateTime() {
        float timeElapsed = clock.getTimeElapsedInSeconds();
        int hours = Math.round(timeElapsed) / 3600;
        int minutes = Math.round((timeElapsed) % 3600) / 60;
        int seconds = Math.round(timeElapsed) % 60;
        String timeElapsedString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        timeLabel = new Label(timeElapsedString, skin);
        timeLabel.setFontScale((float)1.25);
        topTable.add(timeLabel).width(100).fillX();
    }

    public void updateTime() {
        float timeElapsed = clock.getTimeElapsedInSeconds();
        int hours = Math.round(timeElapsed) / 3600;
        int minutes = Math.round((timeElapsed) % 3600) / 60;
        int seconds = Math.round(timeElapsed) % 60;
        String timeElapsedString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        timeLabel.setText(timeElapsedString);
    }

    public void populateFunds() {
        double funds = inventory.getFunds();
        String fundsString = "";

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
        } else {
            fundsString = String.format("$%,.2f", funds);
        }

        fundsLabel = new Label(fundsString, skin);
        fundsLabel.setFontScale((float)1.25);
        topTable.add(fundsLabel).width(100).fillX();

        funds = inventory.getIncome();

        if (funds < 0) {
            fundsString = String.format("-£%,.2f", -funds);
        } else {
            fundsString = String.format("+£%,.2f", funds);
        }

        expectedFundsChange = new Label(fundsString, skin);
        expectedFundsChange.setFontScale((float)1.25);
        topTable.add(expectedFundsChange).width(100).fillX();
    }

    public void updateFunds() {
        double funds = inventory.getFunds();
        String fundsString = "";

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
        } else {
            fundsString = String.format("$%,.2f", funds);
        }

        fundsLabel.setText(fundsString);

        funds = inventory.getIncome();

        if (funds < 0) {
            fundsString = String.format("-£%,.2f", -funds);
        } else {
            fundsString = String.format("+£%,.2f", funds);
        }

        expectedFundsChange.setText(fundsString);
    }

    public void populateClimate() {
        Label placeholder = new Label("", skin);
        topTable.add(placeholder).width(100).fillX();
        impactBar = climate.getImpactBar();
        topTable.add(impactBar).width(500);

        double climateHealth = climate.getClimateHealth();
        String climateString = String.format("%,.2f", climateHealth);
        climateLabel = new Label(climateString + "%", skin);
        climateLabel.setFontScale((float)1.25);
        topTable.add(climateLabel).width(100).fillX();

        double climateChange = inventory.getClimateImpact();

        String expectedChange = "";

        if (climateChange < 0) { //Only displays user influenced climate change (no natural increase/decrease)
            expectedChange = String.format("+%,.2f", -climateChange); //Negative climate change actually means a climate increase (All climate impacts coded using positive numbers, so a bigger value means more climate decrease)
        } else if (climateChange == 0) {
            expectedChange = String.format("+%,.2f", climateChange);
        } else {
            expectedChange = String.format("-%,.2f", climateChange);
        }

        expectedClimateChange = new Label(expectedChange + "%", skin);
        expectedClimateChange.setFontScale((float)1.25);
        topTable.add(expectedClimateChange).width(100).fillX();
    }

    public void updateClimate() {
        double climateHealth = climate.getClimateHealth();
        String climateString = String.format("%,.2f", climateHealth);
        climateLabel.setText(climateString + "%");

        double climateChange = inventory.getClimateImpact();

        String expectedChange = "";

        if (climateChange < 0) { //Only displays user influenced climate change (no natural increase/decrease)
            expectedChange = String.format("+%,.2f", -climateChange); //Negative climate change actually means a climate increase (All climate impacts coded using positive numbers, so a bigger value means more climate decrease)
        } else if (climateChange == 0) {
            expectedChange = String.format("+%,.2f", climateChange);
        } else {
            expectedChange = String.format("-%,.2f", climateChange);
        }

        expectedClimateChange.setText(expectedChange + "%");
    }

    public void populateExtractionsSelection() {
        extractionSelect = new SelectBox<>(skin);

        String[] extractions = {"Extractions", "Coal", "Gas", "Geothermal", "Hydro", "Nuclear", "Oil", "Solar", "Wind"};
        extractionSelect.setItems(extractions);
        extractionSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        topUI.addActor(extractionSelect);
    }

    public void handleTileSelection(TileActor selected) {
        sideTable.reset();

        Location location = selected.getLocation();

        if (location.getSearched()) {

        } else if (!location.getSearched()) {
            Label header = new Label("You have not searched this tile.", skin);
            Label subText = new Label("", skin);
            String searchText = String.format("Press 'S' to search for $%,.2f", location.getSearchCost());
            subText.setText(searchText);

            sideTable.add(header).pad(10).row();
            sideTable.add(subText).pad(10).row();
        }
    }

    public void clearTableActors(Table t) {
        for (Actor a : t.getStage().getActors()) {
            a.remove();
        }
    }
}
