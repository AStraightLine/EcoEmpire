package com.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.core.audio.GameSound;
import com.core.map.mapgen.MapGen;

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

    public MapGrid (int rows, int columns, Stage stage, InputMultiplexer inputMultiplexer) {
        this.stage = stage;
        this.rows = rows;
        this.columns = columns;
        this.textures = new Texture[4];
        this.inputMultiplexer = inputMultiplexer;
    }

    public void create ()
    {
        inputMultiplexer.addProcessor(stage);
        table.setFillParent(true);
        table.left().bottom();
        table.setDebug(false);

        initialiseTextures();

        grid = new TileActor[rows][columns];

        actorWidth = Gdx.graphics.getWidth() / rows;
        actorHeight = Gdx.graphics.getHeight() /  columns;

        int middleX = (int) (Math.floor(columns/2 * 100) / 100);
        int middleY = (int) (Math.floor(rows/2 * 100) / 100);

        Random r = new Random();

        for(int i=0; i<rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                int tType = r.nextInt(3); //Generate noise map, each tile is assigned a type of either 0 or 1, current 67% chance for land
                if (tType > 1)
                {
                    tType = 1;
                }
                final TileActor tile = new TileActor(i, j, tType, textures[tType]);

                tile.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Actor clicked at row: " + tile.getRow() + ", col: " + tile.getColumn());
                        //tile.drawExtractor();
                        selectedTile.deselectTile();
                        selectedTile = tile;
                        selectedTile.selectTile();
                        GameSound.playTileSelectSound();

                        return true;
                    }
                });

                grid[i][j] = tile;

                table.add(tile).width(actorWidth).height(actorHeight);
            }
            table.row();
        }
        MapGen mg = new MapGen(grid, rows, columns, textures);
        TileActor[][] newGrid;
        newGrid = mg.cellularAutomata(r.nextInt(10) + 1); //Perform the algorithm a random number of times between 1 and 10
        newGrid = mg.beachGen();
        grid = newGrid;
        selectedTile = grid[middleX][middleY];
        selectedTile.selectTile();
        stage.addActor(table);
    }

    public boolean addExtractor()
    {
        // Can later be updated to use the Location build Extraction function and getExtractionTexture / An extraction menu once it's in place to select which Resource
        // you want to extract.
        boolean complete = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.O))
        {
            complete = selectedTile.drawExtractor();
        }
        return complete;
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
        textures[3] = new Texture(Gdx.files.internal("overlay.png"));
    }
}
