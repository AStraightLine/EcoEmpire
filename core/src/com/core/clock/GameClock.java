package com.core.clock;

import java.util.Timer;

public class GameClock {

    // ToDo: Provide object references for access to funds/climate state + modifiers. Pass to relevant eventPulses. Ensure no race conditions.

    private Timer timer;
    private ClockEvent clockEvent;
    private FundsPulseEvent fundsEventPulse;
    private ClimatePulseEvent climateEventPulse;

    public GameClock() {
        timer = new Timer();
        clockEvent = new ClockEvent();
        fundsEventPulse = new FundsPulseEvent();
        climateEventPulse = new ClimatePulseEvent();

        timer.scheduleAtFixedRate(clockEvent, 0, 1000);
        timer.scheduleAtFixedRate(fundsEventPulse, 2500, 5000);
        timer.scheduleAtFixedRate(climateEventPulse, 5000, 5000);
    }

    public int getTimeElapsedInSeconds() {
        return clockEvent.getTimeElapsedInSeconds();
    }

}
