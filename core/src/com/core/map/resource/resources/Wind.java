package com.core.map.resource.resources;

import com.core.map.resource.Renewable;

import java.util.Random;

public class Wind extends Renewable {

    private Random rand = new Random();

    private String location;
    private double baseValueLowerBoundMod = 0, baseValueUpperBoundMod = 1;
    private double baseImpactLowerBoundMod = 0.25, baseImpactUpperBoundMod = 0.75;
    private int baseStabilityLowerBoundMod = 25, baseStabilityUpperBoundMod = 50;
    private double baseExtractionCostMod = 1.5, baseEaseOfExtractionLowerBoundMod = 2, baseEaseOfExtractionUpperBoundMod = 2.5;

    public Wind(String location) {
        super(location);

        this.location = location;

        // Quantity - Renewable so infinite (set by super constructor).

        // Impact - Very low impact, production may involve fossil fuels and components extracted through mining, large so some damage to habitats and the like, but once setup almost 0.
        setImpactLowerBound(getImpactLowerBound() + baseImpactLowerBoundMod);
        setImpactUpperBound(getImpactUpperBound() + baseImpactUpperBoundMod);
        setImpact(getImpact() + (getImpactLowerBound() + (getImpactUpperBound() - getImpactLowerBound()) * rand.nextDouble())); // May need to be rounded later to make it easier to display

        // Stability - Fairly stable, might be damaged by environment, easily repairable.
        setStabilityLowerBound(getStabilityLowerBound() + baseStabilityLowerBoundMod);
        setStabilityUpperBound(getStabilityUpperBound() + baseStabilityUpperBoundMod);
        setStability(getStability() + (rand.nextInt(getStabilityUpperBound() - getStabilityLowerBound()) + getStabilityLowerBound()));

        // Ease of Extraction - Easy on land but at sea a little more funds involved.
        if (location == "WATER") {
            baseEaseOfExtractionLowerBoundMod += 0.5;
            baseEaseOfExtractionUpperBoundMod += 1;
        }
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // A little larger so some logistics involved in setup.
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
