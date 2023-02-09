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
     * Updates the inventory when an extractor is upgraded
     * @param extractorID what extractor is being upgraded
     */
    public void upgradeExtractor(int extractorID){

        //gets the extractor that is being upgraded
        Extractor extractorToUpdate = this.getExtractor(extractorID);

        //Player pays for the upgrade and income is updated with the new income
        this.funds = this.funds - extractorToUpgrade.getUpdateCost();

        //this needs to be the difference in income(since you are upgrading instead of buying a new one)
        this.income = this.income + extractorToUpdate.getIncomeDiff();


    }

    /**
     * Adds the income to the player's funds
     */
    public void updateFunds(){
        this.funds = this.funds + this.income;
    }

    /**
     * Adds an extractor to the player's inventory, takes price out of funds and adds income
     * @param extractor The extractor to be added to the player's inventory
     */
    public void addExtractor(Extractor extractor){

        this.extractors.add(extractor);
        this.funds = this.funds - extractor.getPrice();//updates funds
        this.income = this.income + extractor.getIncome();//updates income
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

        return null;

    }

}
