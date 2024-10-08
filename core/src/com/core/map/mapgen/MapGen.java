package com.core.map.mapgen;

import com.badlogic.gdx.graphics.Texture;
import com.core.map.grid.TileActor;

import java.util.ArrayList;
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

    private int[][] copyTileTypes() //Create copy of 2D array for tiles, this time only storing a tile's type
    {
        int[][] temp = new int[columns][rows];
        for (int x = 0; x < columns; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                temp[x][y] = grid[x][y].getTileType();
            }
        }
        return temp;
    }

    public TileActor[][] cellularAutomata(int iterations) //Map generation algorithm, used to determine whether a tile is a land or water tile
    {
        for (int i = 0; i < iterations; i++)
        {
            int[][] temp = copyTileTypes(); //Copy of the tilemap before any changes
            for (int x = 0; x < columns; x++)
            {
                for (int y = 0; y < rows; y++)
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
                                    //Don't do anything since this is the current tile
                                }
                                else
                                {
                                    int tType = temp[x + xOffset][y + yOffset]; //Check nearby tile's tile type
                                    if (tType == 1) //Increment number of land tiles nearby
                                    {
                                        nearbyLand++;
                                    }
                                }
                            }
                        }
                    }
                    float nearbyLandPercentage = nearbyLand / max; //Get percentage of land tiles around given tile
                    TileActor t = grid[x][y];
                    if (nearbyLandPercentage > 0.624999f) //If 62.4+% of tiles nearby are land tiles, this tile becomes a land tile
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


        fillGridTextures(grid);

        return grid;
    }
    public void fillGridTextures(TileActor[][] grid)
    {
        for (int x = 0; x < columns; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                grid[x][y].setTileTexture(textures[grid[x][y].getTileType()]);
            }
        }
    }

    public TileActor[][] beachGen(TileActor[][] g)
    {
        grid = g;
        Random r = new Random();
        for (int x = 0; x < columns; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                TileActor t = grid[x][y];
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
                                TileActor tt = grid[x + xOffset][y + yOffset];
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
                            t.setTileType(2);
                            t.setTileTexture(textures[2]);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++)
        {
            refineLand(2); //Refine the noise generated for the beaches, 2 represents sand //8
        }
        return grid;
    }

    //Method to expand a land tile type's area potentially, aims to smooth out an area with z tile type
    public void refineLand(int z) //z is the number representing the tileType you are refining, based on cellular automata
    {
        Random r = new Random();
        int[][] temp = copyTileTypes(); //Get copy of current grid before changes
        for (int x = 0; x < columns; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                int tType = temp[x][y];
                if (tType == 1) //Cannot change tile if tile already was a water tile, only land tiles
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
                                int tType2 = temp[x + xOffset][y + yOffset];
                                if (tType2 == z)
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
                            grid[x][y].setTileType(z);
                            grid[x][y].setTileTexture(textures[z]);
                        }
                    }
                }
            }
        }
    }

    public TileActor[][] refineWater(TileActor[][] g, int iterations)
    {
        ArrayList<Integer> alteredX = new ArrayList<Integer>(); //Store x and y values of altered grid tiles
        ArrayList<Integer> alteredY = new ArrayList<Integer>();
        int alteredTiles = 0;
        grid = g;
        for (int i = 0; i < iterations; i++)
        {
            int[][] temp = copyTileTypes(); //Get copy of current grid before changes
            for (int x = 0; x < columns; x++)
            {
                for (int y = 0; y < rows; y++)
                {
                    int tType = temp[x][y];
                    if (tType == 0) //Cannot change tile if tile already was a water tile, only land tiles
                    {
                        boolean nearbyLand = false;
                        for (int xOffset = -1; xOffset < 2; xOffset++) //Scan nearby tiles
                        {
                            for (int yOffset = -1; yOffset < 2; yOffset++)
                            {
                                if (x + xOffset < 0 || x + xOffset >= columns || y + yOffset < 0 || y + yOffset >= rows) //Out of bounds
                                {

                                } else if (xOffset == 0 && yOffset == 0)
                                {
                                    //Do nothing since this is the current tile
                                } else //Check nearby tile's type
                                {
                                    int tType2 = temp[x + xOffset][y + yOffset];
                                    if (tType2 > 0 || tType2 == 10) {
                                        nearbyLand = true;
                                    }
                                }
                            }
                        }
                        if (nearbyLand == true)
                        {
                            grid[x][y].setTileType(10); //10 is a placeholder value just to indicate refined water tiles
                            alteredX.add(x);
                            alteredY.add(y);
                            alteredTiles++;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < alteredTiles; i++)
        {
            grid[alteredX.get(i)][alteredY.get(i)].setTileType(0); //Set all changed water tile types back to normal
            grid[alteredX.get(i)][alteredY.get(i)].setTileTexture(textures[4]);
        }
        return grid;
    }
}
