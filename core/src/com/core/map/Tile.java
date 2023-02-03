package com.core.map;
import com.badlogic.gdx.math.Rectangle;
public class Tile extends Rectangle
{
    private float oilConcentration;
    //value between 0 and 1, google says a rig can produce as many as 5000 barrels a day, so multiply by this(just an idea)
    //supposedly some countries produce 100s of thousands per rig per day(middle east), so maybe a country modifier could be useful
    //potentially increment decay of this value also? for if oil runs out, and also natural fluctuation
    private int tileType;
    //0 for water
    //1 for land
    //2 etc

    private Location location;




    public Tile(float x, float y, float width, float height, int tileType)
    {
        super(x, y, width, height);

        this.tileType = tileType;
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

    public int getType()
    {
        return tileType;
    }
}
