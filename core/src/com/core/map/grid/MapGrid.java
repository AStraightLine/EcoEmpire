package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.core.audio.GameSound;
import com.core.map.extract.Extractor;
import com.core.map.location.Location;
import com.core.map.mapgen.MapGen;
import com.core.player.PlayerInventory;

import java.util.Random;

public class MapGrid {

    private final Stage stage;
    private Table table = new Table();
    private final Texture[] textures;
    private TileActor[][] grid;
    private int rows;
    private int columns;
    private TileActor selectedTile;
    private InputMultiplexer inputMultiplexer;
    private float actorWidth;
    private float actorHeight;
    private FitViewport viewport;

    public MapGrid(int rows, int columns, Stage stage, InputMultiplexer inputMultiplexer, FitViewport viewport) {
        this.stage = stage;
        this.rows = rows;
        this.columns = columns;
        this.textures = new Texture[8];
        this.inputMultiplexer = inputMultiplexer;
        this.viewport = viewport;
    }

    public void create() {
        inputMultiplexer.addProcessor(stage);
        table.setFillParent(true);
        table.left().bottom();
        table.setDebug(false);

        initialiseTextures();

        grid = new TileActor[columns][rows];

        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        actorWidth = width / columns;
        actorHeight = height / rows;


        Random r = new Random();

        float countX = 0;
        float countY = 0;


        for (int i = 0; i < columns; i++) {
            table.row().expand().fill();
            for (int j = 0; j < rows; j++) {
                int tType = r.nextInt(3); //Generate noise map, each tile is assigned a type of either 0 or 1, current 67% chance for land
                if (tType > 1) {
                    tType = 1;
                }
                final TileActor tile = new TileActor(i, j, tType);



                tile.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Actor clicked at row: " + tile.getRow() + ", col: " + tile.getColumn());
                        //tile.drawExtractor();
                        selectedTile.deselectTile();
                        selectedTile = tile;

                        selectedTile = selectedTile.getParentTile();

                        selectedTile.selectTile();

                        GameSound.playTileSelectSound();

                        return true;
                    }
                });

                grid[i][j] = tile;

                table.add(tile).width(0).height(0).align(Align.topRight).fill().expand();
            }

        }
        MapGen mg = new MapGen(grid, rows, columns, textures);
        TileActor[][] newGrid;
        newGrid = mg.cellularAutomata(270); //Perform the algorithm 270 times
        newGrid = mg.beachGen();
        grid = newGrid;
        selectedTile = grid[0][0];
        selectedTile.selectTile();
        stage.addActor(table);

        TileActor tileTemp;

        for(int i=0; i<columns; i++)
        {
            for(int j=0; j<rows; j++)
            {
                tileTemp = grid[i][j];


                treeCheck(tileTemp);

            }
        }
    }

    public boolean checkTileTypes(TileActor tile, int checkRadius, boolean treeCheck)
    {
        int row = tile.getRow();
        int column = tile.getColumn();
        int tileCheck = 1;
        if(!treeCheck)
        {
            tileCheck = tile.getTileType();
        }

        for(int i=0; i<checkRadius; i++)
        {
            for(int j=0; j<checkRadius; j++)
            {
                if(column-j < 0 || row-i < 0)
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
    public boolean treeCheck(TileActor treeTile)
    {
        System.out.println("X");
        if(checkTileTypes(treeTile,4, true))
        {
            System.out.println("Y");
            boolean allow = checkAvailability(treeTile, 4);
            if(allow)
            {
                System.out.println("Z");
                setAvailability(treeTile, 4);
                treeTile.initTree(textures[3]);
                treeTile.setAsParent();
            }
            return true;
        }


        return false;
    }

    public boolean checkAvailability(TileActor tile, int radius)
    {
        int row = tile.getRow();
        int column = tile.getColumn();

        for(int i=0; i<radius; i++)
        {
            for(int j=0; j<radius; j++)
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

    public boolean setAvailability(TileActor tile, int radius)
    {
        int row = tile.getRow();
        int column = tile.getColumn();

        for(int i=0; i<radius; i++)
        {
            for(int j=0; j<radius; j++)
            {
                if(column-i < 0 || row-j < 0)
                {
                    continue;
                }
                else
                {
                    grid[column-i][row-j].setUnavailable(tile);
                }
            }
        }
        return true;
    }
    public boolean unsetAvailability(int radius)
    {
        int row = selectedTile.getRow();
        int column = selectedTile.getColumn();

        for(int i=0; i<radius; i++)
        {
            for(int j=0; j<radius; j++)
            {
                if(column-i < 0 || row-j < 0)
                {
                    continue;
                }
                else
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

        boolean sameType = checkTileTypes(selectedTile, 4, false);
        boolean available = checkAvailability(selectedTile, 4);

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
                setAvailability(selectedTile, 4);
                selectedTile.setAsParent();
            }
        } else return false;
        return complete;
    }
    public void deleteExtractor(PlayerInventory playerInventory)
    {
        if(selectedTile.returnIsTree())
        {
            deleteTree();
        }


        Location location = selectedTile.getLocation();
        Extractor extractor = location.getExtractor();

        unsetAvailability(4);

        location.setExtracting(false);
        playerInventory.removeExtractor(extractor);
        selectedTile.removeExtractor();
    }
    public void deleteTree()
    {
        unsetAvailability(3);
        selectedTile.removeTree();
    }

    public float getActorWidthTotal()
    {
        return actorWidth*columns;
    }
    public float getActorHeightTotal()
    {
        return actorHeight*rows;
    }
    public void initialiseTextures()
    {
        textures[0] = new Texture(Gdx.files.internal("water.png"));
        textures[1] = new Texture(Gdx.files.internal("land.png"));
        textures[2] = new Texture(Gdx.files.internal("sand.png"));
        textures[3] = new Texture(Gdx.files.internal("tree.png"));
    }

    public TileActor getSelectedTile() {
        return this.selectedTile;
    }
}
