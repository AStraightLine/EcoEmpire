package com.core.map.extract;

import com.core.map.resource.NonRenewable;
import com.core.map.resource.Resource;

public abstract class Extractor implements ExtractFunds, ExtractClimate {

    private Resource resource;
    private int extractionRate;
    private boolean disabled;
    private boolean broken;

    public Extractor(Resource resource) {
        this.resource = resource;
        this.extractionRate = 1;
        this.disabled = false;
        this.broken = false;
    }

    @Override
    public int extractFunds() {
        // NOTE: Called by fundsPulseEvent() after updating Inventory funds.
        // void for now but should likely return a tuple containing value and impact
        // may also have a chance of breaking each extraction depending on stability in which case roll the dice and see if breaks:
        // If it does, give the user the chance to rebuild for a fraction of the extractionCost
        double currentQuantity = resource.getQuantity();
        if (disabled == true) {
            return 0; //0 represents extractor is empty/disabled
        } else {
            //Place stability roll somewhere here
            if (resource instanceof NonRenewable) {
                double newQuantity = currentQuantity - extractionRate;
                resource.setQuantity(newQuantity);
                if (newQuantity <= 0) {
                    disabled = true;
                    resource.setQuantity(0);
                    return 1; //Represents extractor has now ran out after this extraction
                } else {
                    return 2; //Represents extractor still fine
                }
            } else {
                return 2;
            }
        }
    }

    @Override
    public void extractClimate() {
        // Note: Called by climatePulseEvent()
    }

    public String getLocation() {
        return resource.getLocation();
    }

    public double getQuantity() {
        return resource.getQuantity();
    }

    public double getValue() {
        return resource.getValue();
    }

    public double getImpact() {
        return resource.getImpact();
    }

    public int getStability() {
        return resource.getStability();
    }

    public double getExtractionCost() {
        return resource.getExtractionCost();
    }

    public boolean getDisabled() {
        return disabled;
    }

    public boolean getBroken() {
        return broken;
    }
}
