package com.core.player;

import com.core.climate.Climate;
import com.core.clock.GameClock;

public class Player {

    private String playerName;
    private double score;
    private double climateHealth;
    private float timeElapsed;

    private Climate climate;
    private GameClock gameClock;

    /**
     * The Information about the player and their game progress.
     * Needs to be created at the start of a new game
     * @param playerName The name representing the player
     */
    public Player(String playerName, Climate climate, GameClock gameClock){

        this.playerName = playerName;
        this.score = 0;
        this.timeElapsed = 0;
        this.climate = climate;
        this.gameClock = gameClock;

    }

    /**
     * Will update the time elapsed and the climate health.
     */
    public void updatePlayer(){
        this.timeElapsed = this.gameClock.getTimeElapsedInSeconds();
        this.climateHealth = this.climate.getClimateHealth();
    }

    /**
     * Calculates the score of the player
     */
    public void calculateScore(){
        //calculates score
    }

}
