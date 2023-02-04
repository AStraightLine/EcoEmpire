package com.core.map.resource.resources;

import com.core.map.resource.Renewable;

import java.util.Random;

public class Solar extends Renewable {

    private Random rand = new Random();

    private String location;
    private double baseValueLowerBoundMod = 0, baseValueUpperBoundMod = 0.5;
    private double baseImpactLowerBoundMod = 0, baseImpactUpperBoundMod = 0.5;
    private int baseStabilityLowerBoundMod = 25, baseStabilityUpperBoundMod = 50;
    private double baseEaseOfExtractionLowerBoundMod = 0, baseEaseOfExtractionUpperBoundMod = 0.5;

    public Solar(String location) {
        super(location);

        this.location = location;

        // Quantity - Renewable so infinite (set by super constructor).

        // Impact - Very low impact, production may involve fossil fuels and components extracted through mining, but once setup almost 0.
        setImpactLowerBound(getImpactLowerBound() + baseImpactLowerBoundMod);
        setImpactUpperBound(getImpactUpperBound() + baseImpactUpperBoundMod);
        setImpact(getImpact() + (getImpactLowerBound() + (getImpactUpperBound() - getImpactLowerBound()) * rand.nextDouble())); // May need to be rounded later to make it easier to display

        // Stability - Fairly stable, might be damaged by environment, easily repairable.
        setStabilityLowerBound(getStabilityLowerBound() + baseStabilityLowerBoundMod);
        setStabilityUpperBound(getStabilityUpperBound() + baseStabilityUpperBoundMod);
        setStability(getStability() + (rand.nextInt(getStabilityUpperBound() - getStabilityLowerBound()) + getStabilityLowerBound()));

        // Ease of Extraction - only possible on land (well maybe not, but for now). Build the panels, install them, done.
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // Value - Need lots of panel coverage to generate any meaningful amount, lots of cost for land and production etc. none at night, so very low financial output.
        // However, could increase if in an area with lots of sunlight so some variance.
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
