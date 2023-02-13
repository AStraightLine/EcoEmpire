package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.core.map.location.Location;

import java.util.Random;

public class TileActor extends Actor {
    //private int extractorType;
    private boolean populated = false; //if extractor is on tile
    private boolean unavailable = false;
    private TextureRegion extractorSprite;
    private Texture extractorTexture;
    private int tileType;
    private int row;
    private int column;
    private Location location;
    private TextureRegion sprite;
    private Texture selectedTexture;
    private TextureRegion selectedSprite;
    private boolean selected = false;
    private TileActor parentTile = this;
    private TextureRegion tree;
    private boolean isTree = false;
    private boolean isParent = false;
    //private String[] extractorFileNames = {"sea-rig.png", "land-rig.png"};

    public TileActor(final int row, final int column, int tileType) {
        this.row = row;
        this.column = column;
        this.tileType = tileType;
        this.location = new Location(tileType);

        //setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        setTouchable(Touchable.enabled);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());

        if(populated == true)
        {
            if(isTree == true)
            {
                batch.draw(tree, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
            }
            else
            {
                batch.draw(extractorSprite, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
            }

        }
        if(selected == true)
        {
            selectedSprite.setTexture(selectedTexture);
            if(isParent)
            {
                if(isTree)
                {
                    batch.draw(selectedSprite, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
                }
                else
                {
                    batch.draw(selectedSprite, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
                }

            }
            else
            {
                batch.draw(selectedSprite, getX(), getY(), getWidth(), getHeight());
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
        this.selectedTexture = new Texture(Gdx.files.internal("overlay.png"));
        this.selectedSprite = new TextureRegion(selectedTexture);
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
        if(selectedTexture == null)
        {
            this.selectedTexture = new Texture(Gdx.files.internal("overlay.png"));
        }

        this.selectedSprite = new TextureRegion(selectedTexture);
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
        isParent = false;
        unavailable = true;
    }

    public void setAvailable()
    {
        this.parentTile = this;
        isParent = false;
        unavailable = false;
    }

    public void setAsParent()
    {
        isParent = true;
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
        this.sprite = new TextureRegion(texture);
    }

    public Location getLocation() {
        return location;
    }

    public boolean drawExtractor(Texture extractionTexture) //pass through extractor type probably, method not finished
    {
        if(!populated)
        {
            this.extractorSprite = new TextureRegion(extractionTexture);
            populated = true;
            System.out.println("Extractor added!");
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean removeExtractor() //pass through extractor type probably, method not finished
    {
        if(populated)
        {
            extractorSprite = null;
            System.out.println("Extractor removed!");
            populated = false;
            return true;
        }
        else
        {
            return false;
        }

    }

}
