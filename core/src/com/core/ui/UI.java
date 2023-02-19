package com.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.core.climate.Climate;
import com.core.clock.GameClock;
import com.core.player.PlayerInventory;

public class UI {

    private ScreenViewport viewport;
    private int resX, resY, gameWidth, gameHeight;

    private Skin skin;

    private Stage stage;
    private HorizontalGroup topUI;
    private Table topTable, sideTable;

    private PlayerInventory inventory;
    private Climate climate;
    private GameClock clock;

    private Label timeLabel, fundsLabel;
    private ProgressBar impactBar;
    private SelectBox extractionSelect;

    public UI(ScreenViewport viewport, int resX, int resY, int gameWidth, int gameHeight, PlayerInventory inventory, Climate climate, GameClock clock) {
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

        sideTable.setBounds(gameWidth, 0, resX - gameWidth, resY);

        topUI = new HorizontalGroup();
        topUI.setBounds(0, resY - (resY - gameHeight), resX, resY - gameHeight);
        topUI.addActor(topTable);

        stage.addActor(topUI);
        stage.addActor(sideTable);

        populateTopTable();
        populateSideTable();
        populateExtractionsSelection();

    }

    public void update() {
        updateTime();
        updateFunds();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void populateTopTable() {
        populateTime();
        populateFunds();
        populateClimate();
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
        topTable.add(timeLabel).fillX().uniformX();
        topTable.row().pad(5, 0, 5, 0);
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
        topTable.add(fundsLabel).fillX().uniformX();
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
    }

    public void populateClimate() {
        impactBar = climate.getImpactBar();
        topUI.addActor(impactBar);
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
}

 /*
        //add buttons to table
        table.add(resumeGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(saveGame).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();
        Example from Hannah
         */
