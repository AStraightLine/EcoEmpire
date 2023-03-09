package com.core.player;

import com.core.map.extract.Extractor;
import com.core.map.offset.Offset;

import java.util.ArrayList;

public class PlayerInventory {

    private double funds;
    private double income;
    private double climateImpact;
    private ArrayList<Extractor> extractors = new ArrayList<>();
    private ArrayList<Offset> offsets = new ArrayList<>();

    /**
     * Creates a new inventory for the player
     * @param initialFunds The amount of funds the player starts the game with
     */
    public PlayerInventory(double initialFunds){

        this.funds = initialFunds;
        this.income = 0; //since you don't have anything built yet

    }

    /**
     * Updates the inventory when an extractor is upgraded
     * @param extractor what extractor is being upgraded
     */
    public void upgradeExtractor(Extractor extractor){

        //Checks that the extractor is in the player's inventory
        if(this.isExtractorInInventory(extractor)){

            //Cost of upgrading is removed from the funds
            //this.funds = this.funds - extractor.getUpgradeCost();

            //Adds on the extra income the upgrade is generating
            //this.income = this.income + extractor.getIncomeDiff();
        }
        else{
            System.out.println("Extractor isn't in the inventory");
        }


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
    public void addExtractor(Extractor extractor, double cost){

        this.extractors.add(extractor);
        this.funds = this.funds - extractor.getExtractionCost();//updates funds
        this.income = this.income + extractor.getValue();//updates income
        this.climateImpact = this.climateImpact + extractor.getImpact();
    }

    /**
     * Will remove a given extractor from the player's inventory
     * @param extractor The extractor you want to remove from the inventory
     */
    public void removeExtractor(Extractor extractor){

        //check that the extractor is in the inventory
        if(isExtractorInInventory(extractor)){
            extractors.remove(extractor);
            this.income = this.income - extractor.getValue();
            this.climateImpact = this.climateImpact - extractor.getImpact();
            System.out.println("Extractor removed");
        }
        else{
            System.out.println("Extractor isn't in inventory");
        }
    }

    /**
     * Will check if the given extractor is in the player's inventory
     *
     * @param extractor the extractor you want to find in the inventory
     * @return If the extractor is in the player's inventory
     */
    public Boolean isExtractorInInventory(Extractor extractor){

        try{

            for(int i = 0; i < (this.extractors.size()); i++){
                if(extractors.get(i).equals(extractor)){
                    return(Boolean.TRUE);
                }
                else{

                }
            }
        }
        catch (Exception e){
            System.out.println("Unable to find extractor with that ID");
        }
        return(Boolean.FALSE);
    }

    public void addOffset(Offset offset) {
        this.offsets.add(offset);
        this.funds -= offset.getCost();
        this.income -= offset.getMaintenance();
        this.climateImpact -= offset.getEffect(); // Negative as climateImpact is deducted from climate and "effect" is a positive value.
    }

    public void removeOffset(Offset offset) {
        if (isOffsetInInventory(offset)) {
            this.income += offset.getMaintenance();
            this.climateImpact += offset.getEffect();
            offsets.remove(offset);
        }
    }

    public boolean isOffsetInInventory(Offset offset) {
        try {
            for (int i = 0; i < offsets.size(); i++) {
                if (offsets.get(i).equals(offset)) {
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public double getFunds() {
        return this.funds;
    }
    public void setFunds(double funds)
    {
        this.funds = funds;
    }
    public void setClimateImpact(double climateImpact)
    {
        this.climateImpact = climateImpact;
    }

    public  double getClimateImpact() { return this.climateImpact; }

    public double getIncome() {return this.income; }

    public void charge(double cost) {
        if (this.funds >= cost) {
            this.funds -= cost;
        }
    }

    public ArrayList<Offset> getOffsets() {
        return offsets;
    }
}
