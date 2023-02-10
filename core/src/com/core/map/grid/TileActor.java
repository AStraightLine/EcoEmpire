package com.core.map.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.core.map.location.Location;

public class TileActor extends Actor {
    //private int extractorType;
    private boolean populated = false; //if extractor is on tile
    private boolean unavailable = false;
    private Sprite extractorSprite;
    private Texture extractorTexture;
    private int tileType;
    private int row;
    private int column;
    private Texture texture;
    private Location location;
    private Sprite sprite;
    private Texture selectedTexture;
    private Sprite selectedSprite;
    private boolean selected = false;
    private TileActor parentTile = this;
    private boolean isParent = false;
    //private String[] extractorFileNames = {"sea-rig.png", "land-rig.png"};

    public TileActor(final int row, final int column, int tileType, Texture texture) {
        this.row = row;
        this.column = column;
        this.tileType = tileType;
        this.location = new Location(tileType);

        this.texture = texture;

        this.sprite = new Sprite(texture);

        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setTouchable(Touchable.enabled);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getWidth(), getHeight());

        if(selected == true)
        {
            selectedSprite.setTexture(selectedTexture);
            if(isParent)
            {
                batch.draw(selectedSprite, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
            }
            else
            {
                batch.draw(selectedSprite, getX(), getY(), getWidth(), getHeight());
            }
        }
        if(populated == true)
        {
            extractorSprite.setTexture(extractorTexture);
            batch.draw(extractorSprite, getX()-getWidth()*3, getY(), getWidth()*4, getHeight()*4);
        }

    }
    public TileActor getParentTile()
    {
        return parentTile;
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

        this.selectedSprite = new Sprite(selectedTexture);
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
    public void setAsParent()
    {
        isParent = true;
    }


    public boolean isUnavailable()
    {
        return unavailable;
    }

    public void setTileType(int t, Texture texture)
    {
        tileType = t;
        location.changeTileType(t);
        this.texture = texture;
        this.sprite.setTexture(texture);
    }

    public Location getLocation() {
        return location;
    }

    public boolean drawExtractor(Texture extractionTexture) //pass through extractor type probably, method not finished
    {
        //this.extractorType = extractorType;
        if(!populated)
        {
            this.extractorTexture = extractionTexture;


            this.extractorSprite = new Sprite(extractorTexture);
            populated = true;
            System.out.println("Extractor added!");
            return true;
        }
        else
        {
            return false;
        }

    }

}
