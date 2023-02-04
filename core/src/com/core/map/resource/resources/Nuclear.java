package com.core.map.resource.resources;

import com.core.map.resource.NonRenewable;

import java.util.Random;

public class Nuclear extends NonRenewable {

    private Random rand = new Random();

    private String location;
    private double baseValueLowerBoundMod = 5, baseValueUpperBoundMod = 10;
    private double baseImpactLowerBoundMod = 0.25, baseImpactUpperBoundMod = 3;
    private int baseStabilityLowerBoundMod = 0, baseStabilityUpperBoundMod = 25;
    private double baseExtractionCostMod = 50, baseEaseOfExtractionLowerBoundMod = 2.5, baseEaseOfExtractionUpperBoundMod = 4;

    public Nuclear(String location) {
        super(location);

        this.location = location;

        // Quantity - Either find rare or import so random
        setQuantity(rand.nextInt(1000 - 0) + 0);

        // Impact - Very low but potential to go very badly.
        // PERHAPS: Keep impact low but look to stability, if we hit a 0 stability role, instantly take some multiple of the impact from the climate one time.
        setImpactLowerBound(getImpactLowerBound() + baseImpactLowerBoundMod);
        setImpactUpperBound(getImpactUpperBound() + baseImpactUpperBoundMod);
        setImpact(getImpact() + (getImpactLowerBound() + (getImpactUpperBound() - getImpactLowerBound()) * rand.nextDouble())); // May need to be rounded later to make it easier to display

        // Stability - Fairly stable but potential to go very badly.
        setStabilityLowerBound(getStabilityLowerBound() + baseStabilityLowerBoundMod);
        setStabilityUpperBound(getStabilityUpperBound() + baseStabilityUpperBoundMod);
        setStability(getStability() + (rand.nextInt(getStabilityUpperBound() - getStabilityLowerBound()) + getStabilityLowerBound()));

        // Ease of Extraction - Complicated, highly skilled, chance to go wrong.
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // Expensive and timely to set up.
        setExtractionCost(getExtractionCost() + baseExtractionCostMod);

        // Value - Very valuable.
        setValueLowerBound(getValueLowerBound() + baseValueLowerBoundMod);
        setValueUpperBound(getValueUpperBound() + baseValueUpperBoundMod);
        // Take 1% of easeOfExtraction to account for the lasting effects of this difficulty.
        // Round to 2 decimal places to represent currency
        setValue(Math.round(((getValueLowerBound() + (getValueUpperBound() - getValueLowerBound()) * rand.nextDouble()) - (getEaseOfExtraction() / 100)) * 100.0) / 100.0);

        // Extraction cost - Lots of startup cost + highly skilled workers + safety concerns
        // Dependent upon: base extraction cost, ease of extraction, stability, and quantity
        // costOfExtraction = costOfExtraction + (easeOfExtraction * (amount / stability)
        setExtractionCost(Math.round((getExtractionCost() + (getEaseOfExtraction() + ((getStability() / 100) * 5))) * 100.0) / 100.0);
    }
}
