package com.core.map.offset.offsets;

import com.core.map.offset.Offset;

import java.util.Random;

public class Tree extends Offset {

    private Random rand = new Random();

    private double effectLowerBound, effectUpperBound;
    private double costLowerBound, costUpperBound;
    private double removalCost;

    public Tree() {
        super();

        // The lowest tier offset, useful if you can plant a lot of them, also game starts with lots so can't be too high.
        // Can only be as effective as you have land tiles available, becomes interesting if we implement changing sea levels.
        this.effectLowerBound = 0.125;
        this.effectUpperBound = 0.25;
        this.setEffect((effectLowerBound + (effectUpperBound - effectLowerBound) * rand.nextDouble()));

        // Minimal cost just needs to be purchased, cleared and planted.
        this.costLowerBound = 0.5;
        this.costUpperBound = 2.5;
        this.setCost(Math.round(((costLowerBound + (costUpperBound - costLowerBound)) * rand.nextDouble()) * 100.0) / 100.0);

        // No maintenance cost
        this.setMaintenance(0);

        //Removal cost
        this.setRemovalCost(Math.round(((0.25 + (0.5 - 0.25) * rand.nextDouble())) * 100.0) / 100.0);
    }

}
