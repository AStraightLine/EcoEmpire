package com.core.map.offset;

public class Offset {

    private String location = null; // null by default. Some Offsets such as Trees will have a location attached but most won't.
    private double effect, effectLowerBound, effectUpperBound;
    private double cost, costLowerBound, costUpperBound;
    private double maintenance, maintenanceLowerBound, maintenanceUpperBound;

    public Offset() {
        this.effect = 0;
        this.cost = 0;
        this.maintenance = 0;
    }

    public double getEffect() {
        return effect;
    }

    public void setEffect(double effect) {
        this.effect = effect;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(double maintenance) {
        this.maintenance = maintenance;
    }
}
