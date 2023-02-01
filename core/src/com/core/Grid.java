package com.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Grid extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture waterTexture;
    private Rectangle[][] grid;
    private int rows = 16;
    private int columns = 16;

    public void create ()
    {
        batch = new SpriteBatch();
        waterTexture = new Texture(Gdx.files.internal("core/src/water.png"));

        grid = new Rectangle[rows][columns];
        float squareWidth = Gdx.graphics.getWidth() / columns;
        float squareHeight = Gdx.graphics.getHeight() / rows;

        for(int i=0; i<rows; i++)
        {
            for(int j=0; j<columns; j++)
            {
                float x = j*squareWidth;
                float y = i*squareHeight;
                grid[i][j] = new Rectangle(x, y, squareWidth, squareHeight);
            }
        }
    }


    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for(int i=0; i<rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                Rectangle currentRec = grid[i][j];
                batch.draw(waterTexture, currentRec.x, currentRec.y, currentRec.width, currentRec.height);
            }
        }

        if (Gdx.input.justTouched())
        {
            float clickX = Gdx.input.getX();
            float clickY = Gdx.input.getY();

            for(int i=0; i<rows; i++)
            {
                for(int j=0; j<columns; j++)
                {
                    Rectangle currentRec = grid[i][j];
                    if(currentRec.contains(clickX, clickY))
                    {
                        System.out.println("\nRow: " + i + "\nColumn: " + j);
                    }
                }
            }
        }
        batch.end();
    }
}