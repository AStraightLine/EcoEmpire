package com.core.player;

import com.core.map.extract.Extractor;

import java.util.ArrayList;

public class PlayerInventory {

    private double funds;
    private double income;
    private ArrayList<Extractor> extractors = new ArrayList<>();
    //private ArrayList<Offset> offsets = new ArrayList<>();

    public PlayerInventory(float initialFunds){

        this.funds = initialFunds;
        this.income = 0; //since you don't have anything built yet

    }

    /**
     * Adds an extractor to the player's inventory
     * @param extractor The extractor to be added to the player's inventory
     */
    public void addExtractor(Extractor extractor){

        this.extractors.add(extractor);

    }

    /**
     * Will return a given extractor
     *
     * @param extractorID The ID of the extractor you want
     * @return
     */
    public Extractor getExtractor(int extractorID){

        try{

            for(int i = 0; i < (this.extractors.size()); i++){
                if(extractors.get(i).getID().equals(extractorID)){
                    return(extractors.get(i));
                }
                else{

                }
            }
        }
        catch (Exception e){
            System.out.println("Unable to find extractor with that ID");
        }

    }

}
