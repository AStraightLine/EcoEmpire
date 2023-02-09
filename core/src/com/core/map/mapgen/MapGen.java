package com.core.map.mapgen;

import com.badlogic.gdx.graphics.Texture;
import com.core.map.TileActor;

import java.util.Random;

public class MapGen {
    private TileActor[][] grid;
    private int rows;
    private int columns;
    private Texture[] textures;

    public MapGen(TileActor[][] g, int r, int c, Texture[] t)
    {
        grid = g;
        rows = r;
        columns = c;
        textures = t;
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

    public TileActor[][] cellularAutomata(int iterations) //Map generation algorithm
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
                        for (int yOffset = -1; yOffset < 2; yOffset++) //Scan 8 tiles surrounding given tile
                        {
                            for (int xOffset = -1; xOffset < 2; xOffset++)
                            {
                                if (x + xOffset < 0 || x + xOffset >= columns || y + yOffset < 0 || y + yOffset >= rows) //Out of bounds
                                {
                                    max--; //Decrease maximum possible of land tiles if out of bounds
                                }
                                else if (xOffset == 0 && yOffset == 0)
                                {
                                    //Don't to anything since this is the current tile
                                }
                                else
                                {
                                    TileActor t = temp[y + yOffset][x + xOffset];
                                    int tType = t.getTileType(); //Check nearby tile's tile type
                                    if (tType == 1) //Increment number of land tiles nearby
                                    {
                                        nearbyLand++;
                                    }
                                }
                            }
                        }
                    }
                    float nearbyLandPercentage = nearbyLand / max; //Get percentage of land tiles around given tile
                    TileActor t = grid[y][x];
                    if (nearbyLandPercentage > 0.5) //If 50+% of tiles nearby are land tiles, this tile becomes a land tile
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
        return grid;
    }

    public TileActor[][] beachGen()
    {
        Random r = new Random();
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                TileActor t = grid[y][x];
                if (t.getTileType() == 1) //Cannot be a sand tile if tile already was a water tile, only land tiles
                {
                    boolean touchingWater = false;
                    double beachChance = 0.4; //Chance of a tile becoming a sand tile (beach), constant
                    for (int xOffset = -1; xOffset < 2; xOffset++) //Scan nearby tiles
                    {
                        for (int yOffset = -1; yOffset < 2; yOffset++)
                        {
                            if (x + xOffset < 0 || x + xOffset >= columns || y + yOffset < 0 || y + yOffset >= rows) //Out of bounds
                            {

                            }
                            else if (xOffset == 0 && yOffset == 0)
                            {
                                //Do nothing since this is the current tile
                            }
                            else //Check nearby tile's type
                            {
                                TileActor tt = grid[y + yOffset][x + xOffset];
                                int tType = tt.getTileType();
                                if (tType == 0) //0 represents water tile
                                {
                                    touchingWater = true;
                                }
                            }
                        }
                    }
                    if (touchingWater == true && t.getTileType() == 1) //Can only change a land tile to sand and at least 1 nearby tile is water
                    {
                        double c = r.nextDouble(); //Get a random double value between 0 and 1 (0 inclusive, 1 exclusive)
                        if (c < beachChance) //If value is lower than chance boundary, tile becomes a sand tile
                        {
                            t.setTileType(2, textures[2]);
                        }
                    }
                }
            }
        }
        refineLand(2); //Refine the noise generated for the beaches, 2 represents sand
        return grid;
    }

    //Method to expand a land tile type's area potentially
    public void refineLand(int z) //z is the number representing the tileType you are refining, based on cellular automata
    {
        Random r = new Random();
        TileActor[][] temp = copyTiles(); //Get copy of current grid before changes
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                TileActor t = temp[y][x];
                if (t.getTileType() == 1) //Cannot change tile if tile already was a water tile, only land tiles
                {
                    boolean touchingTileZ = false;
                    double tileZChance = 0.0; //Chance of a land tile becoming a tile of type z
                    for (int xOffset = -1; xOffset < 2; xOffset++) //Scan nearby tiles
                    {
                        for (int yOffset = -1; yOffset < 2; yOffset++)
                        {
                            if (x + xOffset < 0 || x + xOffset >= columns || y + yOffset < 0 || y + yOffset >= rows) //Out of bounds
                            {

                            }
                            else if (xOffset == 0 && yOffset == 0)
                            {
                                //Do nothing since this is the current tile
                            }
                            else //Check nearby tile's type
                            {
                                TileActor tt = temp[y + yOffset][x + xOffset];
                                int tType = tt.getTileType();
                                if (tType == z)
                                {
                                    touchingTileZ = true;
                                    tileZChance = tileZChance + 0.125; //Max 8 tiles surrounding given tile, therefore a nearby tile of type Z increase change by 1/8
                                }
                            }
                        }
                    }
                    if (touchingTileZ == true) //If a tile has tiles of type z nearby, there is a chance it can become a tile of type z
                    {
                        double c = r.nextDouble(); //Generate random double
                        if (c < tileZChance) //If number falls within chance zone, it becomes tile of type z
                        {
                            grid[y][x].setTileType(z, textures[z]);
                        }
                    }
                }
            }
        }
    }
}
