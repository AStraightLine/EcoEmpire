package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.core.Const;
import com.core.audio.GameSound;
import com.core.climate.Climate;
import com.core.map.extract.Extractor;
import com.core.map.location.Location;
import com.core.map.mapgen.MapGen;
import com.core.map.offset.Offset;
import com.core.map.offset.offsets.Tree;
import com.core.player.PlayerInventory;
import com.core.ui.UI;

import java.util.Random;

public class MapGrid {
    private Random rand = new Random();

    private final Stage stage;
    private Table table = new Table();
    private final Texture[] textures;
    private TileActor[][] grid;
    private int rows;
    private int columns;
    private TileActor selectedTile;
    private InputMultiplexer inputMultiplexer;
    private PlayerInventory inventory;
    private Climate climate;
    private UI ui;

    private int gameWidth, gameHeight;


    public MapGrid(int rows, int columns, Stage stage, int gameWidth, int gameHeight, InputMultiplexer inputMultiplexer, Climate climate, PlayerInventory inventory, UI ui) {
        this.stage = stage;
        this.rows = rows;
        this.columns = columns;
        this.textures = new Texture[8];
        this.inputMultiplexer = inputMultiplexer;
        this.climate = climate;
        this.inventory = inventory;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.ui = ui;
    }

    public void create() {
        inputMultiplexer.addProcessor(stage);
        table.setBounds(0, 0, gameWidth, gameHeight);
        table.left().bottom();
        table.setDebug(false);

        initialiseTextures();

        grid = new TileActor[columns][rows];

        Group tiles = new Group();

        Random r = new Random();

        for (int i = 0; i < columns; i++) {
            table.row().expand().fill();
            for (int j = 0; j < rows; j++) {
                int tType = r.nextInt(3); //Generate noise map, each tile is assigned a type of either 0 or 1, current 67% chance for land
                if (tType > 1) {
                    tType = 1;
                }
                final TileActor tile = new TileActor(i, j, tType, climate, this);

                tile.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Actor clicked at row: " + tile.getRow() + ", col: " + tile.getColumn());
                        //tile.drawExtractor();
                        selectedTile.deselectTile();
                        selectedTile = tile;
                        selectedTile = selectedTile.getParentTile();
                        selectedTile.selectTile();
                        ui.handleTileSelection(selectedTile);
                        GameSound.playTileSelectSound();
                        return true;
                    }
                });

                grid[i][j] = tile;
                table.add(grid[i][j]).width(0).height(0).align(Align.topRight).fill().expand();
            }

        }
        MapGen mg = new MapGen(grid, rows, columns, textures);
        TileActor[][] newGrid;
        newGrid = mg.cellularAutomata(270); //Perform the algorithm 200 times (270 previously) 250 also
        newGrid = mg.beachGen(newGrid);
        newGrid = mg.refineWater(newGrid, 3);
        grid = newGrid;
        selectedTile = grid[0][0];
        selectedTile.selectTile();

        tiles.addActor(table);
        stage.addActor(tiles);
        tiles.setZIndex(0);
        inventory.setFunds(100000);
        for(int i=0; i<columns; i++)
        {
            for(int j=0; j<rows; j++)
            {
                Random rn = new Random();
                int answer = rn.nextInt(8) + 1;
                if(answer == 1)
                {
                    selectedTile = grid[i][j];
                    addTree();
                }
            }
        }
        inventory.setClimateImpact(0);
        inventory.setFunds(100);
    }

    public void addTree()
    {
        TileActor tile = this.getSelectedTile();
        Location location = tile.getLocation();
        if(!(inventory.getFunds() >= 2.5) || location.hasOffset()==true)
        {
            return;
        }

        boolean possible = this.tryTree(tile, Const.treeY, Const.treeX);

        Tree tree = new Tree();


        location.setOffset(tree);
        location.setHasOffset(true);
        inventory.addOffset(tree);
        ui.handleTileSelection(tile);

    }



    public boolean checkTileTypes(TileActor tile, int y, int x, boolean treeCheck)
    {
        int row = tile.getRow();
        int column = tile.getColumn();
        int tileCheck = 1;
        if(!treeCheck)
        {
            tileCheck = tile.getTileType();
        }

        for(int i=0; i<x; i++)
        {
            for(int j=0; j<y; j++)
            {
                if(column-i < 0 || row-j < 0)
                {
                    return false;
                }
                else
                {
                    if(grid[column-i][row-j].getTileType() != tileCheck)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean tryTree(TileActor treeTile, int y, int x)
    {
        if(checkTileTypes(treeTile, x, y, true))
        {
            boolean allow = checkAvailability(treeTile, x, y);
            if(allow)
            {
                setUnavailable(treeTile, x, y);
                treeTile.initTree(textures[3]);
            }
            return true;
        }
        return false;
    }
    public boolean checkAvailability(TileActor tile, int y, int x)
    {
        int row = tile.getRow();
        int column = tile.getColumn();

        for(int i=0; i<x; i++)
        {
            for(int j=0; j<y; j++)
            {
                if(column-i < 0 || row-j < 0)
                {
                    return false;
                }
                else
                {
                    if(grid[column-i][row-j].isUnavailable())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public boolean setUnavailable(TileActor tile, int y, int x)
    {
        int row = tile.getRow();
        int column = tile.getColumn();

        for(int i=0; i<x; i++)
        {
            for(int j=0; j<y; j++)
            {
                if(!(column-i < 0 || row-j < 0))
                {
                    grid[column-i][row-j].setUnavailable(tile);
                    grid[column-i][row-j].getLocation().setSearched(false);
                }
            }
        }
        return true;
    }

    public boolean setAvailable(TileActor tile, int y, int x)
    {
        int row = tile.getRow();
        int column = tile.getColumn();

        for(int i=0; i<y; i++)
        {
            for(int j=0; j<x; j++)
            {
                if(!(column-i < 0 || row-j < 0))
                {
                    grid[column-i][row-j].setAvailable();
                }
            }
        }
        return true;
    }

    public boolean addExtractor(Location location, String resource, String texturePath) {
        // Can later be updated to use the Location build Extraction function and getExtractionTexture / An extraction menu once it's in place to select which Resource
        // you want to extract.
        boolean complete = false;

        boolean sameType = checkTileTypes(selectedTile, 4, 4, false);
        boolean available = checkAvailability(selectedTile, 4, 4);

        Texture texture = new Texture(Gdx.files.internal(texturePath));

        if (!location.getExtracting()) {

        }

        if(sameType && available && !location.getExtracting())
        {
            Extractor extractor = location.buildExtractor(resource);
            location.setExtracting(true);
            location.setExtractingResource(resource);
            location.setExtractionTexture(texture);
            complete = selectedTile.drawExtractor(texture);
            if(complete)
            {
                setUnavailable(selectedTile, 4, 4);
            }
        } else return false;
        return complete;
    }
    public void deleteExtractor(PlayerInventory playerInventory)
    {
        Location location = selectedTile.getLocation();
        if(selectedTile.returnIsTree())
        {
            deleteTree(selectedTile);

            return;
        }


        if(location.getExtracting())
        {
            Extractor extractor = location.getExtractor();

            setAvailable(selectedTile, 4, 4);

            location.setExtracting(false);
            playerInventory.removeExtractor(extractor);
            selectedTile.removeExtractor();
        }
    }
    public void deleteTree(TileActor tile)
    {
        double removalCost = Math.round(((0.25 + (0.5 - 0.25) * rand.nextDouble())) * 100.0) / 100.0;

        if (inventory.getFunds() >= removalCost) {
            setAvailable(tile, Const.treeY, Const.treeX);
            tile.removeTree();
            if (tile.getLocation().hasOffset()) {
                System.out.println("CACA");
                inventory.removeOffset(tile.getLocation().getOffset());
            }
            inventory.charge(removalCost);
            climate.singleClimateImpact(0.125 + (0.25 - 0.125) * rand.nextDouble());
        } // ELSE can't afford to remove tree
    }
    public void initialiseTextures()
    {
        textures[0] = new Texture(Gdx.files.internal("water.png"));
        textures[1] = new Texture(Gdx.files.internal("land.png"));
        textures[2] = new Texture(Gdx.files.internal("sand.png"));
        textures[3] = new Texture(Gdx.files.internal("tree.png"));
        textures[4] = new Texture(Gdx.files.internal("water_2.png"));
    }
    public TileActor getSelectedTile() {
        return this.selectedTile;
    }
}
