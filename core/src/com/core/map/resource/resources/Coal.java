package com.core.map.resource.resources;

import com.core.map.resource.NonRenewable;
import com.core.map.resource.Renewable;

import java.util.Random;

public class Coal extends NonRenewable {

    private Random rand = new Random();

    private String location;
    private int baseQuantityUpperBoundMod = 0;
    private double baseValueLowerBoundMod = 4, baseValueUpperBoundMod = 8;
    private double baseImpactLowerBoundMod = 2, baseImpactUpperBoundMod = 5;
    private int baseStabilityLowerBoundMod = 0, baseStabilityUpperBoundMod = 15;
    private double baseEaseOfExtractionLowerBoundMod = 2, baseEaseOfExtractionUpperBoundMod = 5;

    public Coal(String location) {
        super(location);

        this.location = location;

        // Quantity - Fossil so low quantity, also been mined longest(?) so make rare
        setQuantityUpperBound(getQuantityUpperBound() + baseQuantityUpperBoundMod);
        setQuantity((int)((getQuantity() + (rand.nextInt((int)getQuantityUpperBound() - (int)getQuantityLowerBound()) + (int)getQuantityLowerBound()))/15));

        // Impact - Fossil fuel so high impact + mining operations
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

        // Ease of Extraction - Mining so not so easy, easier if land rather than sea.
        if (location == "WATER") {
            baseEaseOfExtractionLowerBoundMod += 0.5;
            baseEaseOfExtractionUpperBoundMod += 2;
        }
        setEaseOfExtractionLowerBound(getEaseOfExtractionLowerBound() + baseEaseOfExtractionLowerBoundMod);
        setEaseOfExtractionUpperBound(getEaseOfExtractionUpperBound() + baseEaseOfExtractionUpperBoundMod);
        setEaseOfExtraction(getEaseOfExtraction() + (getEaseOfExtractionLowerBound() + (getEaseOfExtractionUpperBound() - getEaseOfExtractionLowerBound()) * rand.nextDouble()));

        // Value - Most expensive fossil fuel - so shift both upper and lower
        setValueLowerBound(getValueLowerBound() + baseValueLowerBoundMod);
        setValueUpperBound(getValueUpperBound() + baseValueUpperBoundMod);
            // Take 1% of easeOfExtraction to account for the lasting effects of this difficulty.
            // Round to 2 decimal places to represent currency
        setValue(Math.round(((getValueLowerBound() + (getValueUpperBound() - getValueLowerBound()) * rand.nextDouble()) - (getEaseOfExtraction() / 100)) * 100.0) / 100.0);

        // Extraction cost - harder at sea, mining, medium as harder than most renewables but well practiced by humanity
            // Dependent upon: base extraction cost, ease of extraction, stability, and quantity
            // costOfExtraction = costOfExtraction + (easeOfExtraction * (amount / stability)
        setExtractionCost(Math.round(getExtractionCost() + (getEaseOfExtraction() * (getQuantity() / getStability())) * 100.0) / 100.0);
    }
}
