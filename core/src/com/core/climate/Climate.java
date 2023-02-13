package com.core.climate;

import java.util.Random;

public class Climate {

    private double climateHealth;
    //Could have specific factors such as air pollution, water pollution etc..., need to link all those to climate health
    private Random r;

    public Climate() {
        climateHealth = 100; //Start at 100% climate
        r = new Random();
    }

    public void updateClimate(double impact) { //Pass total impact value of extractors and offsetters here (is that a word?)
        double amount = r.nextDouble() * 2; //Represents natural climate health increase or decrease, between 0 and 2% (0 inclusive, 2 exclusive)
        int incOrDec = r.nextInt(2);
        if (incOrDec == 1) {//1 represents an increase, 0 means decrease
            climateHealth = climateHealth + amount - impact;
            if (climateHealth > 100) {
                climateHealth = 100; //Can't go over 100% health
            }
        }
        else {
            climateHealth = climateHealth - amount - impact;
            if (climateHealth < 0) {
                climateHealth = 0; //Can't go under 100% health
            }
        }
    }

    public double getClimateHealth() {
        return climateHealth;
    }
}
