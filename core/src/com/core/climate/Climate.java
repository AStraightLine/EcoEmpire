package com.core.climate;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;

import java.util.Random;

import static com.core.Const.baseHealth;

public class Climate {

    private double climateHealth;
    //Could have specific factors such as air pollution, water pollution etc..., need to link all those to climate health
    private Random r;
    private ProgressBar impactBar;

    public Climate(ProgressBar impactBar) {
        this.impactBar = impactBar;

        climateHealth = baseHealth; //Start at 100% climate
        r = new Random();
    }

    public void updateClimate(double impact) { //Pass total impact value of extractors and offsetters here (is that a word?)
        double amount = r.nextDouble() * 10; //Represents natural climate health increase or decrease, between 0 and 1% (0 inclusive, 2 exclusive)
        int incOrDec = r.nextInt(2); //Generate random integer which is either 0 or 1
        if (incOrDec == 1) { //1 represents a natural decrease
            amount = amount * -1;
        }
        climateHealth = climateHealth + amount - impact;
        if (climateHealth > baseHealth) {
            climateHealth = baseHealth; //Can't go over 100% health
        }
        else if (climateHealth < 0) {
            climateHealth = 0; //Can't go under 100% health
        }
        impactBar.setValue((float)climateHealth);
    }

    public ProgressBar getImpactBar() {
        return impactBar;
    }

    public void setImpactBar(ProgressBar bar) {
        this.impactBar = bar;
    }

    public void singleClimateImpact(double impact) {
        climateHealth -= impact;
    }

    public double getClimateHealth() {
        return (climateHealth / baseHealth) * 100; //Return percentage
    }
}
