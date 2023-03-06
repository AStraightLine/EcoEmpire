package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class TransportInvestment extends Offset {
    private Random rand = new Random();
    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceLowerBound, maintenanceUpperBound;

    public TransportInvestment() {
        this.effectLowerBound = 2;
        this.effectUpperBound = 6;
        setEffect((effectLowerBound + ((effectUpperBound - effectLowerBound) * rand.nextDouble())));

        this.costLowerBound = 20;
        this.costUpperBound = 30;
        setCost(Math.round((costLowerBound + ((costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        this.maintenanceLowerBound = 3;
        this.maintenanceUpperBound = 5;
        setMaintenance(Math.round((maintenanceLowerBound + ((maintenanceUpperBound - maintenanceLowerBound) * rand.nextDouble())) * 100.0) / 100.0);
    }
}
