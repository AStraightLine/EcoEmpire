package com.core.randomevents;

import com.core.map.extract.Extractor;

public abstract class ExtractorEvent implements RandomEvent
{

    private Extractor extractor;

    public ExtractorEvent(Extractor extractor) //May need player inventory passed in here as well
    {
        this.extractor = extractor;
    }

    public abstract void execute();

}
