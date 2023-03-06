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
        setEffect((effectLowerBound + ((effectUpperBound - effectLowerBound) * rand.nextDouble())));

        this.costLowerBound = 400;
        this.costUpperBound = 600;
        setCost(Math.round((costLowerBound + ((costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        this.maintenanceLowerBound = 60;
        this.maintenanceUpperBound = 100;
        setMaintenance(Math.round((maintenanceLowerBound + ((maintenanceUpperBound - maintenanceLowerBound) * rand.nextDouble())) * 100.0) / 100.0);
    }
}
