package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class InfrastructureInvestment extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public InfrastructureInvestment() {
        this.effectLowerBound = 1;
        this.effectUpperBound = 4;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound * rand.nextDouble())));

        this.costLowerBound = 10;
        this.costUpperBound = 15;
        setCost((costLowerBound + (costUpperBound - costLowerBound * rand.nextDouble())));

        this.maintenanceLowerBound = 1;
        this.maintenanceUpperBound = 2;
        setMaintenance(maintenanceLowerBound + (maintenanceUpperBound = maintenanceUpperBound * rand.nextDouble()));
    }
}
