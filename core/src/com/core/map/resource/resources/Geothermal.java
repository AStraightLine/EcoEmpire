package com.core.map.resource.resources;

import com.core.map.resource.Renewable;

import java.util.Random;

public class Geothermal extends Renewable {

    private Random rand = new Random();

    private String location;
    private double baseValueLowerBoundMod = 0.25, baseValueUpperBoundMod = 0.75;
    private double baseImpactLowerBoundMod = 0.25, baseImpactUpperBoundMod = 2.5;
    private int baseStabilityLowerBoundMod = 15, baseStabilityUpperBoundMod = 30;
    private double baseExtractionCostMod = 2, baseEaseOfExtractionLowerBoundMod = 2.5, baseEaseOfExtractionUpperBoundMod = 4;

    public Geothermal(String location) {
        super(location);

        this.location = location;

        // Quantity - Renewable so infinite (set by super constructor).

        // Impact - Lower than fossils but possible the most risky renewable other than nuclear.
        setImpactLowerBound(getImpactLowerBound() + baseImpactLowerBoundMod);
        setImpactUpperBound(getImpactUpperBound() + baseImpactUpperBoundMod);
        setImpact(getImpact() + (getImpactLowerBound() + (getImpactUpperBound() - getImpactLowerBound()) * rand.nextDouble())); // May need to be rounded later to make it easier to display

        // Stability - Fairly stable but possible bad side effects.
        setStabilityLowerBound(getStabilityLowerBound() + baseStabilityLowerBoundMod);
        setStabilityUpperBound(getStabilityUpperBound() + baseStabilityUpperBoundMod);
        setStability(getStability() + (rand.nextInt(getStabilityUpperBound() - getStabilityLowerBound()) + getStabilityLowerBound()));

        // Ease of Extraction - A little more complicated than the other renewables.
        if (location == "WATER") {
            baseEaseOfExtractionLowerBoundMod += 0.5;
            baseEaseOfExtractionUpperBoundMod += 1;
        }
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // A little larger so some logistics involved in setup.
        if (location == "WATER") {
            baseExtractionCostMod += 0.5;
            baseEaseOfExtractionLowerBoundMod += 0.5;
            baseEaseOfExtractionUpperBoundMod += 0.5;
        }
        setExtractionCost(getExtractionCost() + baseExtractionCostMod);

        // Value - Largely dependent on weather and location. Fluctuations etc.
        setValueLowerBound(getValueLowerBound() + baseValueLowerBoundMod);
        setValueUpperBound(getValueUpperBound() + baseValueUpperBoundMod);
        // Take 1% of easeOfExtraction to account for the lasting effects of this difficulty.
        // Round to 2 decimal places to represent currency
        setValue(Math.round(((getValueLowerBound() + (getValueUpperBound() - getValueLowerBound()) * rand.nextDouble()) - (getEaseOfExtraction() / 100)) * 100.0) / 100.0);

        // Extraction cost - harder at sea
        // Dependent upon: base extraction cost, ease of extraction, stability, and quantity
        // costOfExtraction = costOfExtraction + (easeOfExtraction * (amount / stability)
        setExtractionCost(Math.round((getExtractionCost() + (getEaseOfExtraction() + ((getStability() / 100) * 5))) * 100.0) / 100.0);
    }
}
