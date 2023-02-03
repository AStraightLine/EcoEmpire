package com.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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

        for(int i=0; i<rows; i++)
        {
            for(int j=0; j<columns; j++)
            {
                float x = j*squareWidth;
                float y = i*squareHeight;
                grid[i][j] = new Tile(x, y, squareWidth, squareHeight, 1);
            }
        }
        selectedTile = grid[middleX][middleY];
        calibrateWindow();
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
        textures[0] = new Texture(Gdx.files.internal("core/src/water.png"));
        textures[1] = new Texture(Gdx.files.internal("core/src/land.png"));
        textures[2] = new Texture(Gdx.files.internal("core/src/overlay.png"));
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