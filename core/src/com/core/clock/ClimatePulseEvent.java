package com.core.clock;

import com.core.climate.Climate;
import com.core.player.PlayerInventory;

public class ClimatePulseEvent extends PulseEvent {

    private PlayerInventory playerInventory;
    private Climate climate;

    public ClimatePulseEvent(PlayerInventory playerInventory, Climate climate) {

        this.playerInventory = playerInventory;
        this.climate = climate;
    }

    @Override
    public void run() {
        // ToDo: Take Inventory and Climate model as parameters.
        // ToDo: Get Extractors from Inventory.
        // ToDo: Call appropriate Extract extract method on each Extractor from Inventory.
        double impact = playerInventory.getClimateImpact();
        climate.updateClimate(impact);
    }
}
