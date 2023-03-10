package com.core.clock;

import com.core.player.PlayerInventory;

public class FundsPulseEvent extends PulseEvent {

    private PlayerInventory playerInventory;

    public FundsPulseEvent(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    @Override
    public void run() {
        // ToDo: Needs to take an Inventory as a parameter.
        // ToDo: Call update on Inventory for funds.
        // ToDo: Call fundsExtract on each Extraction from Inventory.
        playerInventory.updateFunds();
        playerInventory.extractFromExtractors();
    }
}
