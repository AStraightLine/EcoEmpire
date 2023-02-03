package com.core.map.resource;

import com.core.Const;

public class Renewable extends Resource {

    private double baseExtractionCostModifier = 5;
    private double baseValue = 1;
    private double baseValueLowerBound = 1;
    private double baseValueUpperBound = 2;
    private double baseImpact = 0.5; // Still some impact due to production etc
    private double baseImpactLowerBound = 0.5;
    private double baseImpactUpperBound = 1;
    private int baseStability = 100;
    private int baseStabilityLowerBound = 100;
    private int baseStabilityUpperBound = 150; // Max average break (repair cost) every 2.5 minutes
    private double baseEaseOfExtraction = 0.5;
    private double baseEaseOfExtractionLowerBound = 0.5;
    private double baseEaseOfExtractionUpperBound = 1;

    public Renewable(String location) {
        super(location);

        // Renewables maybe slighter cheaper to extract generally speaking
        this.setExtractionCost(baseExtractionCostModifier);
        // Renewables have infinite quantity by definition
        this.setQuantity(Const.infinity);
        this.setQuantityLowerBound(Const.infinity);
        this.setQuantityUpperBound(Const.infinity);
        // Renewables less profitable generally
        this.setValue(baseValue);
        this.setValueLowerBound(baseValueLowerBound);
        this.setValueUpperBound(baseValueUpperBound);
        // Renewables lower climate impact generally
        this.setImpact(baseImpact);
        this.setImpactLowerBound(baseImpactLowerBound);
        this.setImpactUpperBound(baseImpactUpperBound);
        // Renewables more stable generally
        this.setStability(baseStability);
        this.setStabilityLowerBound(baseStabilityLowerBound);
        this.setStabilityUpperBound(baseStabilityUpperBound);
        // NonRenewables generally easier to extract
        this.setEaseOfExtraction(baseEaseOfExtraction);
        this.setEaseOfExtractionLowerBound(baseEaseOfExtractionLowerBound);
        this.setEaseOfExtractionUpperBound(baseEaseOfExtractionUpperBound);
    }
}
