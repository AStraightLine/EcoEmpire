package com.core.map.extract;

interface Extract {

    // void for now but should likely return a tuple containing value and impact
    // may also have a chance of breaking each extraction depending on stability in which case roll the dice and see if breaks:
    // If it does, give the user the chance to rebuild for a fraction of the extractionCost
    public void extract();
}
