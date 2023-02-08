package com.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.core.map.location.Location;

public class TileActor extends Actor {
    private boolean populated = false; //if extractor is on tile
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
            batch.draw(selectedSprite, getX(), getY(), getWidth(), getHeight());
        }
        if(populated == true)
        {

            extractorSprite.setTexture(extractorTexture);
            batch.draw(extractorSprite, getX(), getY(), getWidth(), getHeight());
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

        this.selectedSprite = new Sprite(selectedTexture);
        this.selected = true;
    }
    public void deselectTile()
    {
        this.selected = false;
    }

    public void initialise()
    {
        ;
    }

    public int getTileType()
    {
        return tileType;
    }

    public void setTileType(int t, Texture texture)
    {
        tileType = t;
        this.texture = texture;
        this.sprite.setTexture(texture);
    }
    public int getType()
    {
        return tileType;
    }

    public Location getLocation() {
        return location;
    }

    public boolean drawExtractor() //pass through extractor type probably, method not finished
    {
        if(populated == false)
        {

            if(tileType==0)
            {
                this.extractorTexture = new Texture(Gdx.files.internal("land-rig.png"));
            }
            if(tileType==1)
            {
                this.extractorTexture = new Texture(Gdx.files.internal("sea-rig.png"));
            }
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
