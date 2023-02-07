package com.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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

    public MapGrid (int rows, int columns, Stage stage, InputMultiplexer inputMultiplexer) {
        this.stage = stage;
        this.rows = rows;
        this.columns = columns;
        this.textures = new Texture[3];
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

        int actorWidth = Gdx.graphics.getWidth() / rows;
        int actorHeight = Gdx.graphics.getHeight() /  columns;

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

                        selectedTile.deselectTile();
                        selectedTile = tile;
                        selectedTile.selectTile();

                        return true;
                    }
                });

                grid[i][j] = tile;

                table.add(tile).width(actorWidth).height(actorHeight);
            }
            table.row();
        }
        cellularAutomata(1); //Perform the algorithm once
        selectedTile = grid[middleX][middleY];
        selectedTile.selectTile();

        stage.addActor(table);
    }

    public void initialiseTextures()
    {
        textures[0] = new Texture(Gdx.files.internal("water.png"));
        textures[1] = new Texture(Gdx.files.internal("land.png"));
        textures[2] = new Texture(Gdx.files.internal("overlay.png"));
    }

    private TileActor[][] copyTiles()
    {
        TileActor[][] temp = new TileActor[rows][columns];
        for (int y = 0; y < rows; y++)
        {
            for (int x = 0; x < columns; x++)
            {
                temp[y][x] = grid[y][x];
            }
        }
        return temp;
    }

    private void cellularAutomata(int iterations) //Map generation algorithm
    {
        for (int i = 0; i < iterations; i++)
        {
            TileActor[][] temp = copyTiles(); //Copy of the tilemap before any changes
            for (int y = 0; y < rows; y++)
            {
                for (int x = 0; x < columns; x++)
                {
                    float max = 8; //Max amount of land tiles around a single tile
                    float nearbyLand = 0;
                    {
                        for (int yOffset = -1; yOffset < 2; yOffset++)
                        {
                            for (int xOffset = -1; xOffset < 2; xOffset++)
                            {
                                if (x + xOffset < 0 || x + xOffset >= columns || y + yOffset < 0 || y + yOffset >= rows)
                                {
                                    max--;
                                }
                                else if (xOffset == 0 && yOffset == 0)
                                {

                                }
                                else
                                {
                                    TileActor t = temp[y + yOffset][x + xOffset];
                                    int tType = t.getTileType();
                                    if (tType == 1)
                                    {
                                        nearbyLand++;
                                    }
                                }
                            }
                        }
                    }
                    float nearbyLandPercentage = nearbyLand / max;
                    TileActor t = grid[y][x];
                    if (nearbyLandPercentage > 0.5)
                    {
                        t.setTileType(1, textures[1]);
                    }
                    else
                    {
                        t.setTileType(0, textures[0]);
                    }
                }
            }
        }
    }
}
