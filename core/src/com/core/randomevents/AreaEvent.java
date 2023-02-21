package com.core.randomevents;

import com.core.map.grid.MapGrid;

public abstract class AreaEvent implements RandomEvent
{
    private MapGrid grid;

    public AreaEvent(MapGrid grid)
    {
        this.grid = grid;
    }

    public abstract void execute();
}
