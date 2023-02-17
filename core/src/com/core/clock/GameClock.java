package com.core.clock;

import com.core.climate.Climate;
import com.core.player.PlayerInventory;

import java.time.LocalTime;
import java.util.Timer;

public class GameClock {

    // ToDo: Provide object references for access to funds/climate state + modifiers. Pass to relevant eventPulses. Ensure no race conditions.

    // Only problem here is that pause is in the middle of the spectrum, not the end. Might not be a problem but we can work it out as a team.

    private Timer timer;
    private ClockEvent clockEvent;
    private FundsPulseEvent fundsEventPulse;
    private ClimatePulseEvent climateEventPulse;
    private PlayerInventory playerInventory;
    private Climate climate;
    private int timeMod; // Scaled from -5 to 5. Can change range if necessary. 0 is equivalent of a pause.
    private int lastTimeMod; // Last state before a pause.
    private boolean isPaused;
    private boolean pausedByPause;

    private long fundsPulseEventRate; // How often funds should be updated (in milliseconds)
    private long climatePulseEventRate; // How often the climate state should be updated (in milliseconds)

    // Only set when schedule is updated, but also only used by this class. Do not rely on this outside.
    // If necessary use TimerEvent.scheduledExecutionTime(); (Replace TimerEvent with specific instance, I.E. fundsPulseEvent or climatePulseEvent.
    private long nextFundsPulseDelay; // Time until next scheduled funds update (in milliseconds)
    private long nextClimatePulseDelay; // Time until next scheduled climate state update (in milliseconds)

    public GameClock(PlayerInventory playerInventory, Climate climate) {

        this.playerInventory = playerInventory;
        this.climate = climate;

        this.timer = new Timer();
        this.clockEvent = new ClockEvent(this);
        this.fundsEventPulse = new FundsPulseEvent(playerInventory);
        this.climateEventPulse = new ClimatePulseEvent(playerInventory, climate);

        this.timeMod = 1; // Change this to 0 if we wish to start the game paused and the below to "true".
        this.isPaused = false; // False means game is running. We can decide if we want to start games paused or un-paused.
        this.fundsPulseEventRate = 2000;
        this.climatePulseEventRate = 2000;

        schedule();
    }

    // Initial timer setup. Hardcoded once but never used again.
    // But if we want to change when the initial update to funds and climate happens, we do that here in the "delay" parameter.
    private void schedule() {
        timer.scheduleAtFixedRate(clockEvent, 0, 1000);
        timer.scheduleAtFixedRate(fundsEventPulse, 1000, fundsPulseEventRate);
        timer.scheduleAtFixedRate(climateEventPulse, 2000, climatePulseEventRate);
    }

    // Called everytime the game is paused, un-paused, or when the game time is sped up / down.
    private void updateSchedule() {
        if (isPaused) { // Save time until next scheduled updates in milliseconds. Ensure they remain positive by making their values absolute.
            this.nextFundsPulseDelay = Math.abs(fundsPulseEventRate - (System.currentTimeMillis() - fundsEventPulse.scheduledExecutionTime()));
            this.nextClimatePulseDelay = Math.abs(climatePulseEventRate - (System.currentTimeMillis() - climateEventPulse.scheduledExecutionTime()));
        }

        // Cancel scheduled tasks.
        fundsEventPulse.cancel();
        climateEventPulse.cancel();

        // If the game is paused do nothing.
        // Else rebuild the tasks with updated game speed taken into account + delays relevant to when next task should occur (how long was left to wait).
        if (!isPaused) {
            this.fundsEventPulse = new FundsPulseEvent(playerInventory);
            this.climateEventPulse = new ClimatePulseEvent(playerInventory, climate);

            if (timeMod > 0) {
                timer.scheduleAtFixedRate(fundsEventPulse, nextFundsPulseDelay / timeMod, fundsPulseEventRate / timeMod);
                timer.scheduleAtFixedRate(climateEventPulse, nextClimatePulseDelay / timeMod, climatePulseEventRate / timeMod);
            } else if (timeMod < 0) {
                timer.scheduleAtFixedRate(fundsEventPulse, nextFundsPulseDelay * (Math.abs(timeMod) + 1), fundsPulseEventRate * (Math.abs(timeMod) + 1));
                timer.scheduleAtFixedRate(climateEventPulse, nextClimatePulseDelay * (Math.abs(timeMod) + 1), climatePulseEventRate * (Math.abs(timeMod) + 1));
            }
        }
    }

    public void handlePause() {
        if (!isPaused) {
            setTimeMod(0);
        } else {
            setTimeMod(lastTimeMod);
        }
    }

    private void setTimeMod(int mod) {
        if (mod == 0) { // Pause request
            this.lastTimeMod = this.timeMod; // Set last game speed to current speed before updating.
            this.pausedByPause = true; // A literal pause request is pausing the game (as opposed to speed inc / dec).
        }
        // Supporting 10 different speeds + pause
        if (pausedByPause) {
            if (mod >= -5 && mod <= 5) {
                this.timeMod = mod;
                if (this.timeMod == 0) {
                    this.isPaused = true;
                } else {
                    this.isPaused = false;
                }
            }
        }
        updateSchedule();
    }

    public void incTimeMod() {
        // Check if incrementation will take speed beyond 5x.
        if ((timeMod + 1) <= 5) {
            if ((timeMod + 1) == 0) { // speedMod was -1, +1 = 0 = pause.
                timeMod++;
                isPaused = true;
                pausedByPause = false;
            } else if (isPaused) { // Resume from pause state.
                if ((lastTimeMod + 1) > 5) { // Out of range, don't increase but resume.
                    timeMod = lastTimeMod;
                    isPaused = false;
                } else if (pausedByPause) { // We could be anywhere in speed settings, so use lastTimeMod.
                    timeMod = lastTimeMod + 1;
                    isPaused = false;
                } else { // Pause entered by inc / dec so don't use lastTimeMod.
                    timeMod++;
                    isPaused = false;
                }
            } else { // Regular inc.
                timeMod++;
                isPaused = false;
            }
        }
        updateSchedule();
    }

    public void decTimeMod() {
        // Check if decrement will take speed below -5x.
        if ((timeMod - 1) >= -5) {
            if ((timeMod - 1) == 0) { // timeMod was 1, -1 = 0 = Pause.
                timeMod--;
                isPaused = true;
                pausedByPause = false;
            } else if (isPaused) { // Resume from pause.
                if ((lastTimeMod - 1) < -5) { // Out of range, don't increase but resume.
                    timeMod = lastTimeMod;
                    isPaused = false;
                } else if (pausedByPause) { // We could be anywhere in speed settings, so use lastTimeMod.
                    timeMod = lastTimeMod - 1;
                    isPaused = false;
                } else { //Pause entered by inc / dec so don't use lastTimeMod.
                    timeMod--;
                    isPaused = false;
                }
            } else { // Regular dec.
                timeMod--;
                isPaused = false;
            }
        }

        updateSchedule();
    }

    public int getTimeMod() {
        return timeMod;
    }

    public int getLastTimeMod() {
        return lastTimeMod;
    }

    public boolean getPauseState() {
        return isPaused;
    }

    public float getTimeElapsedInSeconds() {
        return clockEvent.getTimeElapsedInSeconds();
    }
}