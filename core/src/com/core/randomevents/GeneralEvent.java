package com.core.randomevents;

import com.core.player.PlayerInventory;

public abstract class GeneralEvent implements RandomEvent
{
    //For events that directly affect funds, income or climate
    private PlayerInventory inv;

    public GeneralEvent(PlayerInventory inv)
    {
        this.inv = inv;
    }

    public abstract void execute();
}
