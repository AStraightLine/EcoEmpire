package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class CarbonCapture extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public CarbonCapture() {
        this.effectLowerBound = 15;
        this.effectUpperBound = 23;
        setEffect((effectLowerBound + ((effectUpperBound - effectLowerBound) * rand.nextDouble())));

        this.costLowerBound = 100;
        this.costUpperBound = 150;
        setCost(Math.round((costLowerBound + ((costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        this.maintenanceLowerBound = 25;
        this.maintenanceUpperBound = 35;
        setMaintenance(Math.round((maintenanceLowerBound + ((maintenanceUpperBound - maintenanceLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        this.setRemovalCost(0);
    }
}
