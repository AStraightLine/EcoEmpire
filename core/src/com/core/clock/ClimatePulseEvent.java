package com.core.clock;

import com.core.player.PlayerInventory;

public class ClimatePulseEvent extends PulseEvent {

    private PlayerInventory playerInventory;

    public ClimatePulseEvent(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    @Override
    public void run() {
        // ToDo: Take Inventory and Climate model as parameters.
        // ToDo: Get Extractors from Inventory.
        // ToDo: Call appropriate Extract extract method on each Extractor from Inventory.
    }
}
