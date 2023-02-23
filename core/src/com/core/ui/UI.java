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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.core.Const;
import com.core.climate.Climate;
import com.core.clock.GameClock;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.map.resource.Resource;
import com.core.player.PlayerInventory;

import static com.core.Const.baseHealth;

public class UI {

    private FitViewport viewport;
    private int resX, resY, gameWidth, gameHeight;

    private Skin skin;
    private Stage stage;
    private HorizontalGroup topUI;
    private VerticalGroup sideUI;
    private Table topTable, sideTable;

    private PlayerInventory inventory;
    private Climate climate;
    private GameClock clock;

    private Label timeLabel, fundsLabel, climateLabel, expectedFundsChange, expectedClimateChange, climateText, fundsText;
    private ProgressBar impactBar;
    private SelectBox extractionSelect;
    private Group uiGroup = new Group();
    private float heightBound;
    private float widthBound;

    public UI(FitViewport viewport, int resX, int resY, int gameWidth, int gameHeight, PlayerInventory inventory, Climate climate, GameClock clock, InputMultiplexer inputMultiplexer) {
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
        topTable.setDebug(false);


        topUI = new HorizontalGroup();
        topUI.setBounds(0, 0, resX, resY);
        topUI.addActor(topTable);
        topUI.align(Align.topLeft);

        sideUI = new VerticalGroup();
        sideUI.setBounds(0, 0, resX, resY);
        //sideTable.setBounds(gameWidth, 0, resX - gameWidth, resY - topUI.getHeight());
        sideUI.top().right();
        sideUI.addActor(sideTable);

        uiGroup.addActor(sideUI);
        uiGroup.addActor(topUI);
        uiGroup.setZIndex(1);

        stage.addActor(uiGroup);

        heightBound = resY*0.06f/2;
        widthBound = resX*0.13f/2;

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
        populateClimate();
        topTable.row().pad(0, 0, 5, 0);
        populateFunds();
        populateExtractionsSelection();
        populateTime();
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
        Table topRight = new Table();
        topRight.add(timeLabel).expand().top().right().prefWidth(1000);
        topUI.addActor(topRight);
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
        fundsText = new Label("Funds: ", skin);
        fundsText.setFontScale((float)1.25);
        topTable.add(fundsText).prefWidth(100).fillX();
        double funds = inventory.getFunds();
        String fundsString = "";
        fundsLabel = new Label(fundsString, skin);

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
            fundsLabel.setColor(1, 0, 0, 1);
        } else {
            fundsString = String.format("$%,.2f", funds);
            fundsLabel.setColor(1, 1, 1, 1);
        }

        fundsLabel.setText(fundsString);
        fundsLabel.setFontScale((float)1.25);

        funds = inventory.getIncome();
        expectedFundsChange = new Label("", skin);

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
            expectedFundsChange.setColor(1, 0, 0, 1);
        } else {
            fundsString = String.format("+$%,.2f", funds);
            expectedFundsChange.setColor(0, 1, 0, 1);
        }
        if (funds == 0) {
            expectedFundsChange.setColor(1, 1, 0, 1);
        }

        expectedFundsChange.setText(fundsString);
        expectedFundsChange.setFontScale((float)1.25);

        Table fundsTable = new Table();
        fundsTable.add(fundsLabel).prefWidth(100).expand().left();
        fundsTable.add(expectedFundsChange).prefWidth(100).expand().left();
        topTable.add(fundsTable).expand().left();
    }

    public void updateFunds() {
        double funds = inventory.getFunds();
        String fundsString = "";

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
            fundsLabel.setColor(1, 0, 0, 1);
        } else {
            fundsString = String.format("$%,.2f", funds);
            fundsLabel.setColor(1, 1, 1, 1);
        }

        fundsLabel.setText(fundsString);

        funds = inventory.getIncome();

        if (funds < 0) {
            fundsString = String.format("-$%,.2f", -funds);
            expectedFundsChange.setColor(1, 0, 0, 1);

        } else {
            fundsString = String.format("+$%,.2f", funds);
            expectedFundsChange.setColor(0, 1, 0, 1);
        }
        if (funds == 0) {
            expectedFundsChange.setColor(1, 1, 0, 1);
        }

        expectedFundsChange.setText(fundsString);
    }

    public void populateClimate() {
        climateText = new Label("Climate: ", skin);
        climateText.setFontScale((float)1.25);
        topTable.add(climateText).prefWidth(100).fillX();
        impactBar = climate.getImpactBar();
        topTable.add(impactBar).prefWidth(500);

        double climateHealth = climate.getClimateHealth();
        String climateString = String.format("%,.2f", climateHealth);
        climateLabel = new Label(climateString + "%", skin);
        climateLabel.setFontScale((float)1.25);
        topTable.add(climateLabel).prefWidth(100).fillX();
        climateLabel.setColor((float) (1 - (climateHealth / 100)),(float) (climateHealth / 100), 0, 1); //Colour becomes more red as climate becomes lower
        impactBar.setColor((float) (1 - (climateHealth / 100)),(float) (climateHealth / 100), 0, 1);

        double climateImpact = (inventory.getClimateImpact() / baseHealth) * 100;

        String expectedChange = "";
        expectedClimateChange = new Label("", skin);

        if (climateImpact < 0) { //Only displays user influenced climate change (no natural increase/decrease)
            expectedChange = String.format("+%,.2f", -climateImpact); //Negative climate impact actually means a climate increase (All climate impacts coded using positive numbers, so a bigger value means more climate decrease)
            expectedClimateChange.setColor(0, 1, 0, 1);
        } else if (climateImpact == 0) {
            expectedChange = String.format("+%,.2f", climateImpact);
            expectedClimateChange.setColor(1, 1, 0, 1);
        } else {
            expectedChange = String.format("-%,.2f", climateImpact);
            expectedClimateChange.setColor(1, 0, 0, 1);
        }

        expectedClimateChange.setText(expectedChange + "%");
        expectedClimateChange.setFontScale((float)1.25);
        topTable.add(expectedClimateChange).prefWidth(100).fillX();
    }

    public void updateClimate() {
        double climateHealth = climate.getClimateHealth();
        String climateString = String.format("%,.2f", climateHealth);
        climateLabel.setText(climateString + "%");
        climateLabel.setColor((float) (1 - (climateHealth / 100)),(float) (climateHealth / 100), 0, 1); //Colour becomes more red as climate becomes lower
        impactBar.setColor((float) (1 - (climateHealth / 100)),(float) (climateHealth / 100), 0, 1);

        double climateImpact = (inventory.getClimateImpact() / baseHealth) * 100;

        String expectedChange = "";

        if (climateImpact < 0) { //Only displays user influenced climate change (no natural increase/decrease)
            expectedChange = String.format("+%,.2f", -climateImpact); //Negative climate impact actually means a climate increase (All climate impacts coded using positive numbers, so a bigger value means more climate decrease)
            expectedClimateChange.setColor(0, 1, 0, 1);
        } else if (climateImpact == 0) {
            expectedChange = String.format("+%,.2f", climateImpact);
            expectedClimateChange.setColor(1, 1, 0, 1);
        } else {
            expectedChange = String.format("-%,.2f", climateImpact);
            expectedClimateChange.setColor(1, 0, 0, 1);
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
        String fontColour;
        Double funds = inventory.getFunds();
        //sideTable.debug();
        Location location = selected.getLocation();

        if (!location.getSearched()) {
            // Handle Tree case
            if (selected.isUnavailable()) {
                Label treeHeader = new Label("Forested", skin);
                Label treeDetails = new Label("Removal will come at a cost", skin);
                Label treeSubDetails = new Label("", skin);

                treeDetails.setFontScale((float)0.8);
                treeSubDetails.setFontScale((float)0.8);

                sideTable.add(treeHeader).row();
                sideTable.add(treeDetails).expand().left().row(); //.expand().direction() aligns an actor to a direction, can combine directions

                if (location.hasOffset()) { // Tree is an offset, not a tree spawned by world gen
                    // ASSUMING ITS ONLY POSSIBLE TO BUILD TREE TYPE FOR NOW
                    treeSubDetails.setText(String.format("Lose %,.2f counter impact", location.getOffset().getEffect()));
                } else {
                    treeSubDetails.setText("But no climate impact");
                }
                sideTable.add(treeSubDetails).expand().left().row();
            } else { // NO TREE TO CLEAR
                Label header = new Label("You have not searched this tile.", skin);
                Label subText = new Label("", skin);
                if (funds < location.getSearchCost()) {
                    fontColour = "RED"; //Used to display cost in red if player cannot afford
                } else {
                    fontColour = "";
                }
                String searchText = String.format("Press 'S' to search for [" + fontColour + "]$%,.2f", location.getSearchCost()); //["Colour"] displays any text after the square brackets in chosen colour, "" causes default colour, use [] to end colour text wherever you want
                subText.setText(searchText);

                sideTable.add(header).pad(10).row();
                sideTable.add(subText).expand().left().pad(10).row();

            }
        } else if (location.getSearched() && !location.getExtracting()) { // Searched but no extractor built: show resource details
            for (int i = 0; i < Const.resourceNames.length; i++) {
                Resource[] resources = location.getResourcesArray(); // Array of resources following order in Const class.
                Label resourceHeader = new Label(String.format(Const.resourceNames[i] + " : $%,.2f to extract.", resources[i].getExtractionCost()), skin);
                Label resourceProDetails;

                if (funds < resources[i].getValue()) {
                    fontColour = "RED";
                } else {
                    fontColour = "";
                }

                if (resources[i].getQuantity() == Const.infinity) {
                    resourceProDetails = new Label(String.format("Value: [" + fontColour + "] $%,.2f []", resources[i].getValue()) + ", Quantity: INF", skin);
                } else {
                    resourceProDetails = new Label(String.format("Value: [" + fontColour + "] $%,.2f []", resources[i].getValue()) + ", Quantity: " + resources[i].getQuantity(), skin);
                }

                Label resourceConDetails = new Label(String.format("Impact: %,.2f, Stability: %d", resources[i].getImpact(), resources[i].getStability()), skin);

                resourceProDetails.setFontScale((float)0.75);
                resourceConDetails.setFontScale((float)0.75);

                sideTable.add(resourceHeader).pad(10).row();
                sideTable.add(resourceProDetails).expand().left().pad(10).row();
                sideTable.add(resourceConDetails).expand().left().pad(10).row();
            }
        } else if (location.getExtracting()) { // Location already has an extractor built.
            String resource = location.getExtractingResource();
            resource = resource.substring(0,1) + resource.substring(1, resource.length()).toLowerCase();

            Label extractingHeader = new Label(String.format("Extracting " + resource + " for $%,.2f", location.getExtractor().getValue()), skin);
            Label extractionProDetails = new Label("", skin);
            Label extractionConDetails = new Label("", skin);

            extractionConDetails.setText(String.format("%,.2f Impact, %d Stability", location.getExtractor().getImpact(), location.getExtractor().getStability()));

            if (location.getExtractor().getQuantity() == Const.infinity) {
                extractionProDetails.setText("Infinite quantity");
            } else {
                extractionProDetails.setText(location.getExtractor().getQuantity() + " remaining");
            }

            extractionProDetails.setFontScale((float)0.85);
            extractionConDetails.setFontScale((float)0.85);

            sideTable.add(extractingHeader).pad(10).row();;
            sideTable.add(extractionConDetails).expand().left().pad(10).row();;
            sideTable.add(extractionProDetails).expand().left().pad(10).row();;
        }
    }
}
