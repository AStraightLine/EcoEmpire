package com.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.core.map.grid.MapGrid;
import com.core.map.grid.TileActor;
import com.core.map.location.Location;
import com.core.map.offset.Offset;
import com.core.map.offset.offsets.*;
import com.core.map.offset.offsets.Tree;
import com.core.map.resource.Resource;
import com.core.player.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.core.Const.*;

public class UI {

    private FitViewport viewport;
    private int resX, resY, gameWidth, gameHeight;

    private Skin skin;
    private Stage stage;
    private HorizontalGroup topUI;
    private VerticalGroup sideUI;
    private Table topTable, sideTable;

    private MapGrid grid;
    private PlayerInventory inventory;
    private Climate climate;
    private GameClock clock;

    private Label timeLabel, fundsLabel, climateLabel, expectedFundsChange, expectedClimateChange, climateText, fundsText, errorMessage, title, subTitle, totalOCost, totalOImpact, totalOMain;
    private ProgressBar impactBar;
    private SelectBox extractionSelect, offsetSelect, currentOffsets;
    private TextButton offsets, addOffset, manageOffsets;
    private Group uiGroup = new Group();
    private float heightBound;
    private float widthBound;
    private int selectedOffset, selectedOType;
    private CarbonCapture cc = new CarbonCapture();
    private ClimateResearch cr = new ClimateResearch();
    private InfrastructureInvestment ii = new InfrastructureInvestment();
    private Lobby l = new Lobby();
    private SolarGeoengineering sg = new SolarGeoengineering();
    private TransportInvestment ti = new TransportInvestment();
    private Tree t = new Tree();
    private Offset[] oOptions = {cc, cr, ii, l, sg, ti, t};


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

        topUI = new HorizontalGroup();
        topUI.setBounds(0, 0, resX, resY);
        topUI.addActor(topTable);
        topUI.align(Align.topLeft);
        topUI.space(10);

        sideUI = new VerticalGroup();
        sideUI.setBounds(gameWidth, 0, resX - gameWidth, gameHeight);
        //sideTable.setBounds(gameWidth, 0, resX - gameWidth, resY - topUI.getHeight());
        sideUI.top().left();
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

        //topTable.debug();
        //topUI.debug();
        //sideUI.debug();
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
        populateOffsets();
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
                String extractResource = (String)extractionSelect.getSelected();
                extractResource = extractResource.toUpperCase();

                handleExtractSelection(extractResource);
            }
        });

        topUI.addActor(extractionSelect);
    }

    private void handleExtractSelection(String resource) {
        TileActor tile = grid.getSelectedTile();
        Location location = tile.getLocation();
        String path;
        Boolean built = false;
        double price = getResourcePrice(resource, location);
        boolean buildable = checkBuildable(resource, location);

        if (location.getSearched() && inventory.getFunds() >= price) {
            if (buildable) {
                path = getPath(resource, location.getType());
                if (path != "") {
                    built = grid.addExtractor(location, resource, path);

                    if (built) {
                        inventory.addExtractor(location.getExtractor(), price);
                        handleTileSelection(tile);
                    }
                }
            }
        }
    }

    private double getResourcePrice(String resource, Location location) {
        switch (resource) {
            case Const.coal:
                return location.getCoal().getExtractionCost();
            case Const.gas:
                return location.getGas().getExtractionCost();
            case Const.nuclear:
                return location.getNuclear().getExtractionCost();
            case Const.oil:
                return location.getOil().getExtractionCost();
            case Const.solar:
                return location.getSolar().getExtractionCost();
            case Const.wind:
                return location.getWind().getExtractionCost();
            case Const.hydro:
                return location.getHydro().getExtractionCost();
            case Const.geothermal:
                return location.getGeothermal().getExtractionCost();
        }

        return Const.infinity;
    }

    private boolean checkBuildable(String resource, Location location) {
        String type = location.getType();

        switch (resource) {
            case Const.coal:
            case Const.geothermal:
            case Const.hydro:
            case Const.wind:
            case Const.gas:
            case Const.oil:
                return true;
            case Const.nuclear:
            case Const.solar:
                if (type == Const.land) {
                    return true;
                } else return false;
        }

        return false;
    }

    public void populateOffsets() {
        offsets = new TextButton("Add Offsets", skin); //Button to bring up offsets UI
        offsetSelect = new SelectBox<>(skin);
        String[] temp = {"Select offset", "Carbon Capture", "Climate Research", "Infrastructure Investment", "Lobby", "Solar Geoengineering", "Transport Investment", "Tree"};
        offsetSelect.setItems(temp);
        manageOffsets = new TextButton("Current offsets", skin);
        offsetSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedOffset = offsetSelect.getSelectedIndex(); //Change the selected offset
            }
        });
        addOffset = new TextButton("Add offset", skin);
        addOffset.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                commitOffset(selectedOffset); //When user clicks add offset button, add the offset to the inventory
            }
        });
        offsets.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                offsetDetails(); //Output offset details
            }
        });
        manageOffsets = new TextButton("Manage Offsets", skin);
        manageOffsets.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                manageOffsets();
            }
        });
        currentOffsets = new SelectBox<>(skin);
        topUI.addActor(offsets);
        topUI.addActor(manageOffsets);
    }

    //Output offset details to side UI
    private void offsetDetails() {
        sideTable.reset();
        title = new Label("Offsets", skin);
        errorMessage = new Label("", skin);
        sideTable.add(title).row();
        String fontColour = "";
        for (int i = 0; i < Const.offsetNames.length; i++) {
            Label o = new Label(offsetNames[i], skin);
            if (inventory.getFunds() < oOptions[i].getCost()) {
                fontColour = "RED";
            } else {
                fontColour = "";
            }
            Label effect = new Label(String.format("Effect: %,.2f\nCost: [" + fontColour + "]$%,.2f[]\nMaintenance cost: $%,.2f", oOptions[i].getEffect(), oOptions[i].getCost(), oOptions[i].getMaintenance()), skin);
            effect.setFontScale((float) 0.85);
            sideTable.add(o).row();
            sideTable.add(effect).expand().left().pad(10).row();
        }
        sideTable.add(offsetSelect).padLeft(10).row();
        sideTable.add(addOffset).pad(10).row();
        sideTable.add(errorMessage);
    }

    private void commitOffset(int offset) {
        if (offset == 0) {
            errorMessage.setText("Please select an offset");
            return;
        }
        else if (offset == 1 && inventory.getFunds() >= cc.getCost()) { //Carbon capture
            inventory.addOffset(cc);
        }
        else if (offset == 2 && inventory.getFunds() >= cr.getCost()) { //Climate research
            inventory.addOffset(cr);
        }
        else if (offset == 3 && inventory.getFunds() >= ii.getCost()) { //Infrastructure investment
            inventory.addOffset(ii);
        }
        else if (offset == 4 && inventory.getFunds() >= l.getCost()) { //Lobby
            inventory.addOffset(l);
        }
        else if (offset == 5 && inventory.getFunds() >= sg.getCost()) { //Solar Geoengineering
            inventory.addOffset(sg);
        }
        else if (offset == 6 && inventory.getFunds() >= ti.getCost()) { //Transport Investment
            inventory.addOffset(ti);
        }
        else if (offset == 7 && inventory.getFunds() >= t.getCost()) { //Tree
            boolean added = grid.addTree(t);
            if (added == false) {
                errorMessage.setText("Cannot place tree here");
                return;
            }
        }
        else {
            return;
        }
        cc = new CarbonCapture(); //Reroll offsets once an offset has been selected
        cr = new ClimateResearch();
        ii = new InfrastructureInvestment();
        l = new Lobby();
        sg = new SolarGeoengineering();
        ti = new TransportInvestment();
        t = new Tree();
        Offset[] temp = {cc, cr, ii, l, sg, ti, t};
        oOptions = temp;
        offsetDetails();
    }

    private void manageOffsets() {
        sideTable.reset();
        title = new Label("Manage offsets", skin);
        final String[] oNames = {"Offset type", "Carbon Capture", "Climate Research", "Infrastructure Investment", "Lobby", "Solar Geoengineering", "Transport Investment"};
        currentOffsets.setItems(oNames);
        currentOffsets.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedOType = currentOffsets.getSelectedIndex();
                oTypeDetails(selectedOType);
            }
        });
        subTitle = new Label("", skin);
        totalOCost = new Label("", skin);
        totalOImpact = new Label("", skin);
        totalOMain = new Label("", skin);
        sideTable.add(title).row();
        sideTable.add(currentOffsets).pad(10).left().row();
        sideTable.add(subTitle).row();
        sideTable.add(totalOImpact).pad(10).left().row();
        sideTable.add(totalOCost).pad(10).left().row();
        sideTable.add(totalOMain).pad(10).left().row();
    }

    private void oTypeDetails(int o) {
        double cost = 0, impact = 0, maintenance = 0;
        int counter = 1;
        ArrayList<Offset> cOffsets = inventory.getOffsets();
        ArrayList<Offset> offsetsOfType = new ArrayList<>();
        ArrayList<String> tempNames = new ArrayList<>();
        Class c = Location.class; //Placeholder, need to assign something at start
        if (o == 1) {
            subTitle.setText("Carbon Capture");
            c = CarbonCapture.class;
        }
        else if (o == 2) {
            subTitle.setText("Climate Research");
            c = ClimateResearch.class;
        }
        else if (o == 3) {
            subTitle.setText("Infrastructure Investment");
            c = InfrastructureInvestment.class;
        }
        else if (o == 4) {
            subTitle.setText("Lobby");
            c = Lobby.class;
        }
        else if (o == 5) {
            subTitle.setText("Solar Geoengineering");
            c = SolarGeoengineering.class;
        }
        else if (o == 6) {
            subTitle.setText("Transport Investment");
            c = TransportInvestment.class;
        }
        for (int i = 0; i < cOffsets.size(); i++) {
            System.out.println(cOffsets.get(i).getClass());
            if (cOffsets.get(i).getClass() == c) {
                cost = cost + cOffsets.get(i).getCost();
                impact = impact + cOffsets.get(i).getEffect();
                maintenance = maintenance + cOffsets.get(i).getMaintenance();
                offsetsOfType.add(cOffsets.get(i));
                tempNames.add(Integer.toString(counter));
                counter++;
            }
        }
        totalOCost.setText(String.format("Total cost: $%,.2f", cost));
        totalOImpact.setText(String.format("Total impact: %,.2f", impact));
        totalOMain.setText(String.format("Total maintenance cost: $%,.2f", maintenance));
    }

    private String getPath(String resource, String type) {
        String path = "";
        ArrayList possiblePaths = Const.paths.get(resource);
        if (possiblePaths.size() == 1) {
            return (String)possiblePaths.get(0);
        } else if (possiblePaths.size() > 1) {
            if (type == Const.land) {
                return (String)possiblePaths.get(0);
            } else if (type == Const.water) {
                return (String)possiblePaths.get(1);
            }
        }
        return path;
    }

    public void handleTileSelection(TileActor selected) {
        sideTable.reset();
        String fontColour;
        Double funds = inventory.getFunds();
        //sideTable.debug();
        Location location = selected.getLocation();

        if (!location.getSearched() && !location.getExtracting()) {
            // Handle Tree case
            if (selected.isUnavailable()) {
                Label treeHeader = new Label("Forested", skin);
                Label treeDetails = new Label("Removal will come at a cost", skin);
                Label treeSubDetails = new Label("", skin);

                //.setFontScale((float)0.8);
                //treeSubDetails.setFontScale((float)0.8);

                sideTable.add(treeHeader).expand().pad(10).row();
                sideTable.add(treeDetails).expand().left().pad(10).row(); //.expand().direction() aligns an actor to a direction, can combine directions

                if (location.hasOffset()) { // Tree is an offset, not a tree spawned by world gen
                    // ASSUMING ITS ONLY POSSIBLE TO BUILD TREE TYPE FOR NOW
                    treeSubDetails.setText(String.format("Lose %,.2f counter impact", location.getOffset().getEffect()));
                } else {
                    treeSubDetails.setText("But no climate impact");
                }
                sideTable.add(treeSubDetails).expand().left().pad(10).row();
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

                if (funds < resources[i].getExtractionCost()) {
                    fontColour = "RED";
                } else {
                    fontColour = "";
                }

                Label resourceHeader = new Label(String.format(Const.resourceNames[i] + " :[" + fontColour + "] $%,.2f[] to extract.", resources[i].getExtractionCost()), skin);
                Label resourceProDetails;

                if (resources[i].getQuantity() == Const.infinity) {
                    resourceProDetails = new Label(String.format("Value: $%,.2f ", resources[i].getValue()) + "\nQuantity: INF", skin);
                } else {
                    resourceProDetails = new Label(String.format("Value: $%,.2f ", resources[i].getValue()) + "\nQuantity: " + resources[i].getQuantity(), skin);
                }

                Label resourceConDetails = new Label(String.format("Impact: %,.2f\nStability: %d", resources[i].getImpact(), resources[i].getStability()), skin);

                resourceProDetails.setFontScale((float) 0.85);
                resourceConDetails.setFontScale((float) 0.85);

                sideTable.add(resourceHeader).pad(10).row();
                sideTable.add(resourceProDetails).expand().left().padLeft(10).row();
                sideTable.add(resourceConDetails).expand().left().padLeft(10).row();
            }
        } else if (location.getExtracting()) { // Location already has an extractor built.
            String resource = location.getExtractingResource();
            resource = resource.substring(0, 1) + resource.substring(1, resource.length()).toLowerCase();

            Label extractingHeader = new Label(String.format("Extracting " + resource + " for $%,.2f", location.getExtractor().getValue()), skin);
            Label extractionProDetails = new Label("", skin);
            Label extractionConDetails = new Label("", skin);

            extractionConDetails.setText(String.format("Impact: %,.2f\nStability: %d", location.getExtractor().getImpact(), location.getExtractor().getStability()));

            if (location.getExtractor().getQuantity() == Const.infinity) {
                extractionProDetails.setText("Infinite quantity");
            } else {
                extractionProDetails.setText(location.getExtractor().getQuantity() + " remaining");
            }

            extractionProDetails.setFontScale((float) 0.95);
            extractionConDetails.setFontScale((float) 0.95);

            sideTable.add(extractingHeader).pad(10).row();
            ;
            sideTable.add(extractionConDetails).expand().left().pad(10).row();
            ;
            sideTable.add(extractionProDetails).expand().left().pad(10).row();
            ;
        }
    }

    public void setGrid(MapGrid grid) {
        this.grid = grid;
    }
}
