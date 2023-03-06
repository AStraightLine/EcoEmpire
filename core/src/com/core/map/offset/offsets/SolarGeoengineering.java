package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class SolarGeoengineering extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public SolarGeoengineering() {
        this.effectLowerBound = 10;
        this.effectUpperBound = 100;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound * rand.nextDouble())));

        this.costLowerBound = 100;
        this.costUpperBound = 300;
        setCost(Math.round(costLowerBound + (costUpperBound - costLowerBound * rand.nextDouble())));

        this.maintenanceLowerBound = 50;
        this.maintenanceUpperBound = 100;
        setMaintenance(Math.round(maintenanceLowerBound + (maintenanceUpperBound = maintenanceUpperBound * rand.nextDouble())));
    }
}
