package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class CarbonCapture extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public CarbonCapture() {
        this.effectLowerBound = 10;
        this.effectUpperBound = 30;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound * rand.nextDouble())));

        this.costLowerBound = 30;
        this.costUpperBound = 60;
        setCost(Math.round(costLowerBound + (costUpperBound - costLowerBound * rand.nextDouble())));

        this.maintenanceLowerBound = 5;
        this.maintenanceUpperBound = 10;
        setMaintenance(Math.round(maintenanceLowerBound + (maintenanceUpperBound = maintenanceUpperBound * rand.nextDouble())));
    }
}
