package com.core.map;
import com.badlogic.gdx.math.Rectangle;
import com.core.map.location.Location;

public class Tile extends Rectangle
{
    private float oilConcentration;
    //value between 0 and 1, google says a rig can produce as many as 5000 barrels a day, so multiply by this(just an idea)
    //supposedly some countries produce 100s of thousands per rig per day(middle east), so maybe a country modifier could be useful
    //potentially increment decay of this value also? for if oil runs out, and also natural fluctuation

    // (^) nice research, but I worry if we try to model the real world like that we're going to hit problems with max integers very quickly
    // I only saw this after writing the Location, Resource, Extraction etc. outlines, but I think keeping it a little more arcade(y) is the way to go, at least at first.

    private int tileType;
    //0 for water
    //1 for land
    //2 etc

    // Location - Hold resource data and extraction data for this Tile.
    private Location location;

    public Tile(float x, float y, float width, float height, int tileType)
    {
        super(x, y, width, height);

        this.tileType = tileType;
        this.location = new Location(tileType);
    }

    public Tile(int tileType)
    {
        this.tileType = tileType;

        initialise();
    }

    public void initialise()
    {
        ;
    }

    public int getTileType()
    {
        return tileType;
    }

    public void setTileType(int t)
    {
        tileType = t;
    }


    public int getType()
    {
        return tileType;
    }

    public Location getLocation() {
        return location;
    }
}
