package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class TransportInvestment extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public TransportInvestment() {
        this.effectLowerBound = 5;
        this.effectUpperBound = 20;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound * rand.nextDouble())));

        this.costLowerBound = 20;
        this.costUpperBound = 30;
        setCost((costLowerBound + (costUpperBound - costLowerBound * rand.nextDouble())));

        this.maintenanceLowerBound = 2;
        this.maintenanceUpperBound = 5;
        setMaintenance(maintenanceLowerBound + (maintenanceUpperBound = maintenanceUpperBound * rand.nextDouble()));
    }
}
