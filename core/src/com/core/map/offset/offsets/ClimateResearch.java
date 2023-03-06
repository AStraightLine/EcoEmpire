package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class ClimateResearch extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public ClimateResearch() {
        this.effectLowerBound = 10;
        this.effectUpperBound = 50;
        setEffect((effectLowerBound + ((effectUpperBound - effectLowerBound) * rand.nextDouble())));

        this.costLowerBound = 20;
        this.costUpperBound = 60;
        setCost(Math.round((costLowerBound + ((costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        setMaintenance(0);
    }
}
