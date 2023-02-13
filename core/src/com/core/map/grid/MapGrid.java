package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
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
        this.textures = new Texture[4];
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

        //float actorWidth = viewport.getScreenWidth() / rows;
        //float actorHeight = viewport.getScreenHeight() / columns;

        System.out.println(actorWidth);
        System.out.println(actorHeight);
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

                tile.setX(actorWidth);
                tile.setPosition(countX, countY);
                table.add(tile).width(0).height(0).align(Align.topRight).fill().expand();
                countX+= actorWidth;
            }
            countX = 0;
            countY+= actorHeight;
        }
        MapGen mg = new MapGen(grid, rows, columns, textures);
        TileActor[][] newGrid;
        newGrid = mg.cellularAutomata(270); //Perform the algorithm 270 times
        newGrid = mg.beachGen();
        grid = newGrid;
        selectedTile = grid[0][0];
        selectedTile.selectTile();
        stage.addActor(table);
    }


    public boolean checkTileTypes()
    {
        int row = selectedTile.getRow();
        int column = selectedTile.getColumn();

        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(column-j < 0 || row-i < 0)
                {
                    return false;
                }
                else
                {
                    if(grid[row-i][column-j].getTileType() != selectedTile.getTileType())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkAvailability()
    {
        int row = selectedTile.getRow();
        int column = selectedTile.getColumn();

        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(column-j < 0 || row-i < 0)
                {
                    return false;
                }
                else
                {
                    if(grid[row-i][column-j].isUnavailable())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean setAvailability()
    {
        int row = selectedTile.getRow();
        int column = selectedTile.getColumn();

        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(column-j < 0 || row-i < 0)
                {
                    continue;
                }
                else
                {
                    grid[row-i][column-j].setUnavailable(selectedTile);
                }
            }
        }
        return true;
    }
    public boolean unsetAvailability()
    {
        int row = selectedTile.getRow();
        int column = selectedTile.getColumn();

        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                if(column-j < 0 || row-i < 0)
                {
                    continue;
                }
                else
                {
                    grid[row-i][column-j].setAvailable();
                }
            }
        }
        return true;
    }


    public boolean addExtractor(Location location, String resource, String texturePath) {
        // Can later be updated to use the Location build Extraction function and getExtractionTexture / An extraction menu once it's in place to select which Resource
        // you want to extract.
        boolean complete = false;

        boolean sameType = checkTileTypes();
        boolean available = checkAvailability();

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
                setAvailability();
                selectedTile.setAsParent();
            }
        } else return false;
        return complete;
    }
    public void deleteExtractor(PlayerInventory playerInventory)
    {
        Location location = selectedTile.getLocation();
        Extractor extractor = location.getExtractor();
        System.out.println("X");
        unsetAvailability();

        location.setExtracting(false);
        playerInventory.removeExtractor(extractor);
        selectedTile.removeExtractor();
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
    }

    public TileActor getSelectedTile() {
        return this.selectedTile;
    }
}
