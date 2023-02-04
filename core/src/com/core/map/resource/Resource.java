package com.core.map.resource;

public class Resource {

    private String location;
    private double quantity, quantityLowerBound, quantityUpperBound;
    private double value, valueLowerBound, valueUpperBound;
    private double impact, impactLowerBound, impactUpperBound;
    private int stability, stabilityLowerBound, stabilityUpperBound; // Determines size of dice roll. So higher number = more stable
    private double easeOfExtraction, easeOfExtractionLowerBound, easeOfExtractionUpperBound; // Lower better, use to calculate cost of extraction / value penalty
    private double baseExtractionCost = 10;
    private double extractionCost; // No bounds as dependent on the other bounded variables (Though each resource should have a base extraction cost too)

    public Resource(String location) {
        this.location = location;
        this.extractionCost = baseExtractionCost;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setQuantityLowerBound(double quantityLowerBound) {
        this.quantityLowerBound = quantityLowerBound;
    }

    public void setQuantityUpperBound(double quantityUpperBound) {
        this.quantityUpperBound = quantityUpperBound;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValueLowerBound(double valueLowerBound) {
        this.valueLowerBound = valueLowerBound;
    }

    public void setValueUpperBound(double valueUpperBound) {
        this.valueUpperBound = valueUpperBound;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public void setImpactLowerBound(double impactLowerBound) {
        this.impactLowerBound = impactLowerBound;
    }

    public void setImpactUpperBound(double impactUpperBound) {
        this.impactUpperBound = impactUpperBound;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }

    public void setStabilityLowerBound(int stabilityLowerBound) {
        this.stabilityLowerBound = stabilityLowerBound;
    }

    public void setStabilityUpperBound(int stabilityUpperBound) {
        this.stabilityUpperBound = stabilityUpperBound;
    }

    public void setEaseOfExtraction(double easeOfExtraction) {
        this.easeOfExtraction = easeOfExtraction;
    }

    public void setEaseOfExtractionLowerBound(double easeOfExtractionLowerBound) {
        this.easeOfExtractionLowerBound = easeOfExtractionLowerBound;
    }

    public void setEaseOfExtractionUpperBound(double easeOfExtractionUpperBound) {
        this.easeOfExtractionUpperBound = easeOfExtractionUpperBound;
    }

    public void setExtractionCost(double extractionCost) {
        this.extractionCost = extractionCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getValue() {
        return value;
    }

    public double getImpact() {
        return impact;
    }

    public double getExtractionCost() {
        return extractionCost;
    }

    public int getStability() {
        return stability;
    }

    public double getEaseOfExtraction() {
        return easeOfExtraction;
    }

    public double getEaseOfExtractionLowerBound() {
        return easeOfExtractionLowerBound;
    }

    public double getEaseOfExtractionUpperBound() {
        return easeOfExtractionUpperBound;
    }

    public double getQuantityLowerBound() {
        return quantityLowerBound;
    }

    public double getQuantityUpperBound() {
        return quantityUpperBound;
    }

    public double getValueLowerBound() {
        return valueLowerBound;
    }

    public double getValueUpperBound() {
        return valueUpperBound;
    }

    public double getImpactLowerBound() {
        return impactLowerBound;
    }

    public double getImpactUpperBound() {
        return impactUpperBound;
    }

    public int getStabilityLowerBound() {
        return stabilityLowerBound;
    }

    public int getStabilityUpperBound() {
        return stabilityUpperBound;
    }

    public String getLocation() {
        return location;
    }
}