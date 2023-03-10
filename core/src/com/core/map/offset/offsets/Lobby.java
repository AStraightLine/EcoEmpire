package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class Lobby extends Offset {

    private Random rand = new Random();

    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double maintenanceBaseCost, maintenanceLowerBound, maintenanceUpperBound;

    public Lobby() {
        this.effectLowerBound = 0.25;
        this.effectUpperBound = 2;
        setEffect((effectLowerBound + (effectUpperBound - effectLowerBound) * rand.nextDouble()));

        // Maintenance somewhat bounded by effect, but still able to get a "good / bad deal"
        maintenanceBaseCost = 0.25;
        maintenanceLowerBound = 0;
        maintenanceUpperBound = 1.75;
        double tmpMaintenance = maintenanceBaseCost + (maintenanceLowerBound + ((maintenanceUpperBound - maintenanceLowerBound) * rand.nextDouble()));
        double mod = rand.nextInt(3) + 1;
        setMaintenance((((tmpMaintenance + effectUpperBound) * mod) - ((effectUpperBound - getEffect()) * mod)) / 3);

        setCost(10); // Standard cost but maintenance dependent on effect. Have to buy into politicians council
        this.costLowerBound = getCost() + getMaintenance();
        this.costUpperBound = this.costLowerBound * 2;
        setCost(Math.round(((costLowerBound + (costUpperBound - costLowerBound) * rand.nextDouble())) * 100.0) / 100.0);

        this.setRemovalCost(0);
    }
}
