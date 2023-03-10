package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class ClimateResearch extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public ClimateResearch() {
        this.effectLowerBound = 2;
        this.effectUpperBound = 7;
        setEffect((effectLowerBound + ((effectUpperBound - effectLowerBound) * rand.nextDouble())));

        this.costLowerBound = 40;
        this.costUpperBound = 100;
        setCost(Math.round((costLowerBound + ((costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        setMaintenance(0);

        this.setRemovalCost(0);
    }
}
