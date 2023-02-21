package com.core.randomevents;

import com.core.map.grid.MapGrid;

public abstract class AreaEvent implements RandomEvent
{
    private MapGrid mapGrid;

    public AreaEvent(MapGrid mapGrid)
    {
        this.mapGrid = mapGrid;
    }

    public abstract void execute();
}
