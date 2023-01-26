package com.core.clock;

public class ClockEvent extends PulseEvent {

    private float timeElapsedInSeconds;
    private GameClock gameClock;
    private int timeMod;
    private boolean isPaused;

    public ClockEvent(GameClock gameClock) {
        timeElapsedInSeconds = 0;
        this.gameClock = gameClock;
        this.timeMod = gameClock.getTimeMod();
        this.isPaused = gameClock.getPauseState();
    }

    @Override
    public void run() {
        this.timeMod = gameClock.getTimeMod();
        this.isPaused = gameClock.getPauseState();

        if (!isPaused) {
            if (timeMod > 0) {
                timeElapsedInSeconds += 1 * timeMod;
            } else if (timeMod < 0) {
                // negative representation needs to be converted to a positive for division.
                // (-1 converted to 1 means nothing in terms of speed change so +1 and work at a different scale to positives)
                timeElapsedInSeconds += 1 / (float)(Math.abs(timeMod) + 1);
            }
        }
    }

    public float getTimeElapsedInSeconds() {
        return timeElapsedInSeconds;
    }
}
