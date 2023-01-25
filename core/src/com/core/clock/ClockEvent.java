package com.core.clock;

public class ClockEvent extends PulseEvent {

    private int timeElapsedInSeconds;

    public ClockEvent() {
        timeElapsedInSeconds = 0;
    }

    @Override
    public void run() {
        timeElapsedInSeconds++;
    }

    public int getTimeElapsedInSeconds() {
        return timeElapsedInSeconds;
    }
}
