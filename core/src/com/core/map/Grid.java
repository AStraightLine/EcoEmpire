package com.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.Random;

public class Grid extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture[] textures;
    private Tile[][] grid;
    private Rectangle[][] grid2;
    private int rows;
    private int columns;

    private Tile selectedTile;

    public Grid(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        this.textures = new Texture[3];
    }

    public void create ()
    {
        batch = new SpriteBatch();
        initialiseTextures();

        grid = new Tile[rows][columns];
        grid2 = new Rectangle[rows][columns];

        int middleX = (int) (Math.floor(columns/2 * 100) / 100);
        int middleY = (int) (Math.floor(rows/2 * 100) / 100);

        float squareWidth = Gdx.graphics.getWidth() / columns;
        float squareHeight = Gdx.graphics.getHeight() / rows;

        Random r = new Random();

        for(int i=0; i<rows; i++)
        {
            for(int j=0; j<columns; j++)
            {
                int tType = r.nextInt(3); //Generate noise map, each tile is assigned a type of either 0 or 1, current 67% chance for land
                if (tType > 1)
                {
                    tType = 1;
                }
                float x = j*squareWidth;
                float y = i*squareHeight;
                grid[i][j] = new Tile(x, y, squareWidth, squareHeight, tType);
            }
        }
        cellularAutomata(1); //Perform the algorithm once
        selectedTile = grid[middleX][middleY];
        calibrateWindow();
    }

    private Tile[][] copyTiles()
    {
        Tile[][] temp = new Tile[rows][columns];
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
            Tile[][] temp = copyTiles(); //Copy of the tilemap before any changes
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
                                    Tile t = temp[y + yOffset][x + xOffset];
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
                    Tile t = grid[y][x];
                    if (nearbyLandPercentage > 0.5)
                    {
                        t.setTileType(1);
                    }
                    else
                    {
                        t.setTileType(0);
                    }
                }
            }
        }
    }

    public void calibrateWindow()
    {
        float squareWidth = Gdx.graphics.getWidth() / columns;
        float squareHeight = Gdx.graphics.getHeight() / rows;

        for(int i=0; i<rows; i++)
        {
            for(int j=0; j<columns; j++)
            {
                float x = j*squareWidth;
                float y = i*squareHeight;
                grid2[i][j] = new Rectangle(x, y, squareWidth, squareHeight);
            }
        }
    }

    public void initialiseTextures()
    {
        textures[0] = new Texture(Gdx.files.internal("water.png"));
        textures[1] = new Texture(Gdx.files.internal("land.png"));
        textures[2] = new Texture(Gdx.files.internal("overlay.png"));
    }


    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for(int i=0; i<rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                Tile currentTile = grid[i][j];
                int type = currentTile.getType();

                batch.draw(textures[type], currentTile.x, currentTile.y, currentTile.width, currentTile.height);
                if(currentTile == selectedTile)
                {
                    batch.draw(textures[2], currentTile.x, currentTile.y, currentTile.width, currentTile.height);
                }
            }
        }

        if (Gdx.input.justTouched())
        {
            float clickX = Gdx.input.getX();
            float clickY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for(int i=0; i<rows; i++)
            {
                for(int j=0; j<columns; j++)
                {
                    Rectangle currentTile = grid2[i][j];
                    if(currentTile.contains(clickX, clickY))
                    {
                        selectedTile = grid[i][j];
                        System.out.println("\nRow: " + i + "\nColumn: " + j);
                    }
                }
            }
        }
        batch.end();
    }
}