package com.core.map.extract;

import com.core.map.resource.Resource;

public abstract class Extractor implements Extract {

    private Resource resource;
    private int extractionRate;

    public Extractor(Resource resource) {
        this.resource = resource;
        this.extractionRate = 1;
    }

    @Override
    public void extract() {
        // void for now but should likely return a tuple containing value and impact
        // may also have a chance of breaking each extraction depending on stability in which case roll the dice and see if breaks:
        // If it does, give the user the chance to rebuild for a fraction of the extractionCost
    }
}
