package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.core.Const;
import com.core.climate.Climate;
import com.core.map.location.Location;

import java.util.Random;

public class TileActor extends Actor {
    private boolean populated = false; //if extractor is on tile
    private boolean unavailable = false;
    private TextureRegion extractorTextureRegion;
    private int tileType;
    private int row;
    private int column;
    private Location location;
    private TextureRegion tileTextureRegion;
    private TextureRegion selectedMarkerRegion;
    private TextureRegion searchedTextureRegion;
    private boolean selected = false;
    private TileActor parentTile = this;
    private TextureRegion tree;
    private boolean isTree = false;
    private Climate climate;
    private double cHealth;
    private MapGrid grid;

    public TileActor(final int column, final int row, int tileType, Climate climate, MapGrid grid) {
        this.column = column;
        this.row = row;
        this.tileType = tileType;
        this.location = new Location(tileType);
        this.climate = climate;
        this.grid = grid;
        setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        cHealth = climate.getClimateHealth();
        batch.setColor((float)(cHealth/5)+1f, (float)(cHealth/40)+0.4f, (float)(cHealth/40)+0.4f, 1);

        batch.draw(tileTextureRegion, getX(), getY(), getWidth(), getHeight());

        if(populated == true)
        {
            if(isTree == true)
            {
                batch.draw(tree, getX()-getWidth()*1, getY(), getWidth()*Const.treeX, getHeight()*Const.treeY);
            }
            else
            {
                batch.draw(extractorTextureRegion, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
            }

        }
        if(selected == true)
        {
            if(isTree)
            {
                batch.draw(selectedMarkerRegion, getX()-getWidth()*1, getY(), getWidth()*Const.treeX, getHeight()*Const.treeY);
            }
            else if(location.getExtracting())
            {
                batch.draw(selectedMarkerRegion, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
            }
            else
            {
                if(grid.checkTileTypes(this, 4, 4, false))
                {
                    if(grid.checkAvailability(this, 4, 4))
                    {
                        batch.draw(selectedMarkerRegion, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
                        if(location.getSearched() == true)
                        {
                            batch.draw(searchedTextureRegion, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
                        }
                    }
                    else
                    {
                        batch.draw(selectedMarkerRegion, getX(), getY(), getWidth(), getHeight());
                    }

                }
                else
                {
                    batch.draw(selectedMarkerRegion, getX(), getY(), getWidth(), getHeight());
                    if(location.getSearched() == true)
                    {
                        batch.draw(searchedTextureRegion, getX(), getY(), getWidth(), getHeight());
                    }
                }
            }
        }
        else
        {
            if(location.getSearched() == true)
            {
                batch.draw(searchedTextureRegion, getX(), getY(), getWidth(), getHeight());
            }
        }
    }
    public boolean returnIsTree()
    {
        return isTree;
    }

    public TileActor getParentTile()
    {
        return parentTile;
    }
    public void initTree(Texture treeTexture)
    {
        this.selectedMarkerRegion = new TextureRegion(new Texture(Gdx.files.internal("overlay.png")));
        isTree = true;
        populated = true;
        tree = new TextureRegion(treeTexture);
    }
    public boolean removeTree()
    {
        if(populated)
        {
            tree = null;
            isTree = false;
            System.out.println("Tree removed!");
            populated = false;
            return true;
        }
        else
        {
            return false;
        }
    }
    public int getRow()
    {
        return row;
    }
    public int getColumn()
    {
        return column;
    }
    public void selectTile()
    {
        if(selectedMarkerRegion == null)
        {
            this.selectedMarkerRegion = new TextureRegion(new Texture(Gdx.files.internal("overlay.png")));
        }
        if(searchedTextureRegion == null)
        {
            this.searchedTextureRegion = new TextureRegion(new Texture(Gdx.files.internal("searched.png")));
        }
        this.selected = true;
    }
    public void deselectTile()
    {
        this.selected = false;
    }
    public int getTileType()
    {
        return tileType;
    }
    public void setUnavailable(TileActor parent)
    {
        this.parentTile = parent;
        unavailable = true;
    }

    public void setAvailable()
    {
        this.parentTile = this;
        unavailable = false;
    }
    public boolean isUnavailable()
    {
        return unavailable;
    }

    public void setTileType(int t)
    {
        tileType = t;
        location.changeTileType(t);
    }
    public void setTileTexture(Texture texture)
    {
        this.tileTextureRegion = new TextureRegion(texture);
    }

    public Location getLocation() {
        return location;
    }

    public boolean drawExtractor(Texture extractionTexture) //pass through extractor type probably, method not finished
    {
        if(!populated)
        {
            this.extractorTextureRegion = new TextureRegion(extractionTexture);
            populated = true;
            System.out.println("Extractor added!");
            return true;
        }
        return false;
    }
    public boolean removeExtractor() //pass through extractor type probably, method not finished
    {
        if(populated)
        {
            extractorTextureRegion = null;
            System.out.println("Extractor removed!");
            populated = false;
            return true;
        }
        return false;
    }
}
