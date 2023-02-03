package com.core.map.resource;

public class NonRenewable extends Resource {

    private double baseExtractionCostModifier = 10;
    private int baseQuantity = 600;
    private double baseValue = 2;
    private double baseValueLowerBound = 2;
    private double baseValueUpperBound = 4;
    private double baseImpact = 1;
    private double baseImpactLowerBound = 1;
    private double baseImpactUpperBound = 2;
    private int baseStability = 75;
    private int baseStabilityLowerBound = 75;
    private int baseStabilityUpperBound = 120; // Max average break (repair cost) every 2 minutes
    private double baseEaseOfExtraction = 1;
    private double baseEaseOfExtractionLowerBound = 1;
    private double baseEaseOfExtractionUpperBound = 2;

    public NonRenewable(String location) {
        super(location);

        // NonRenewables maybe slighter more expensive to extract generally speaking
        this.setExtractionCost(baseExtractionCostModifier);
        // Base quantity of 600 ensures 10 minutes of extraction at level 1 extraction rate.
        this.setQuantity(baseQuantity);
        // NonRenewables more profitable generally
        this.setValue(baseValue);
        this.setValueLowerBound(baseValueLowerBound);
        this.setValueUpperBound(baseValueUpperBound);
        // NonRenewables higher climate impact generally
        this.setImpact(baseImpact);
        this.setImpactLowerBound(baseImpactLowerBound);
        this.setImpactUpperBound(baseImpactUpperBound);
        // NonRenewables less stable generally
        this.setStability(baseStability);
        this.setStabilityLowerBound(baseStabilityLowerBound);
        this.setStabilityUpperBound(baseStabilityUpperBound);
        // NonRenewables generally harder to extract
        this.setEaseOfExtraction(baseEaseOfExtraction);
        this.setEaseOfExtractionLowerBound(baseEaseOfExtractionLowerBound);
        this.setEaseOfExtractionUpperBound(baseEaseOfExtractionUpperBound);
    }
}
