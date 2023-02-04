package com.core.map.resource.resources;

import com.core.map.resource.NonRenewable;

import java.util.Random;

public class Oil extends NonRenewable {

    private Random rand = new Random();

    private String location;
    private int baseQuantityUpperBoundMod = 100;
    private double baseValueLowerBoundMod = 2, baseValueUpperBoundMod = 6;
    private double baseImpactLowerBoundMod = 2, baseImpactUpperBoundMod = 8;
    private int baseStabilityLowerBoundMod = 0, baseStabilityUpperBoundMod = 5;
    private double baseEaseOfExtractionLowerBoundMod = 1, baseEaseOfExtractionUpperBoundMod = 4;

    public Oil(String location) {
        super(location);

        this.location = location;

        // Quantity - Fossil so lower quantity, newer to extraction than coal, highly dependent, running out?
        setQuantityUpperBound(getQuantityUpperBound() + baseQuantityUpperBoundMod);
        setQuantity(getQuantity() + (rand.nextInt((int)getQuantityUpperBound() - (int)getQuantityLowerBound()) + (int)getQuantityLowerBound()));

        // Impact - Fossil fuel so high impact, transport heavy impact
        setImpactLowerBound(getImpactLowerBound() + baseImpactLowerBoundMod);
        setImpactUpperBound(getImpactUpperBound() + baseImpactUpperBoundMod);
        setImpact(getImpact() + (getImpactLowerBound() + (getImpactUpperBound() - getImpactLowerBound()) * rand.nextDouble())); // May need to be rounded later to make it easier to display

        // Stability - Depends on land or sea. Land been doing it for a long time so slightly more stable but still less than renewable baseline.
        // High number = more stable - Roll dice between bounds, if it lands on 0, extraction breaks. So lower number = more breaks.
        if (location == "LAND") { // Less stable
            baseStabilityLowerBoundMod += 25;
            baseStabilityUpperBoundMod += 25;
        } // Else leave at current stability and set:
        setStabilityLowerBound(getStabilityLowerBound() + baseStabilityLowerBoundMod);
        setStabilityUpperBound(getStabilityUpperBound() + baseStabilityUpperBoundMod);
        setStability(getStability() + (rand.nextInt(getStabilityUpperBound() - getStabilityLowerBound()) + getStabilityLowerBound()));

        // Ease of Extraction - easier if land rather than sea.
        if (location == "WATER") {
            baseEaseOfExtractionLowerBoundMod += 0.5;
            baseEaseOfExtractionUpperBoundMod += 2;
        }
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // Value - least expensive fossil fuel by far - so shift both upper and lower
        setValueLowerBound(getValueLowerBound() + baseValueLowerBoundMod);
        setValueUpperBound(getValueUpperBound() + baseValueUpperBoundMod);
        // Take 1% of easeOfExtraction to account for the lasting effects of this difficulty.
        // Round to 2 decimal places to represent currency
        setValue(Math.round(((getValueLowerBound() + (getValueUpperBound() - getValueLowerBound()) * rand.nextDouble()) - (getEaseOfExtraction() / 100)) * 100.0) / 100.0);

        // Extraction cost - harder at sea
        // Dependent upon: base extraction cost, ease of extraction, stability, and quantity
        // costOfExtraction = costOfExtraction + (easeOfExtraction * (amount / stability)
        setExtractionCost(Math.round(getExtractionCost() + (getEaseOfExtraction() * (getQuantity() / getStability())) * 100.0) / 100.0);
    }
}
