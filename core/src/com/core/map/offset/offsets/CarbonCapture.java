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
        this.effectUpperBound = 50;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound * rand.nextDouble())));

        this.costLowerBound = 50;
        this.costUpperBound = 100;
        setCost((costLowerBound + (costUpperBound - costLowerBound * rand.nextDouble())));

        this.maintenanceLowerBound = 5;
        this.maintenanceUpperBound = 10;
        setMaintenance(maintenanceLowerBound + (maintenanceUpperBound = maintenanceUpperBound * rand.nextDouble()));
    }
}
