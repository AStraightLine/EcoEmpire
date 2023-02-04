package com.core.map.location;

import com.badlogic.gdx.graphics.Texture;
import com.core.Const;
import com.core.map.extract.Extractor;
import com.core.map.extract.extractors.*;
import com.core.map.resource.resources.*;

public class Location {

    private String type;

    private boolean searched;

    private Extractor extractor;
    private String extractingResource;
    private Boolean extracting;

    private Texture extractionTexture;

    private Coal coal;
    private Gas gas;
    private Nuclear nuclear;
    private Oil oil;
    private Solar solar;
    private Wind wind;
    private Hydro hydro;
    private Geothermal geothermal;

    public Location(int type) {
        this.searched = false;
        this.extracting = false;

        if (type == 0) {
            this.type = "WATER";
        } else if (type == 1) {
            this.type = "LAND";
        }

        this.coal = new Coal(this.type);
        this.gas = new Gas(this.type);
        this.nuclear = new Nuclear(this.type);
        this.oil = new Oil(this.type);
        this.solar = new Solar(this.type);
        this.wind = new Wind(this.type);
        this.hydro = new Hydro(this.type);
        this.geothermal = new Geothermal(this.type);

        System.out.println("Coal - Value: " + coal.getValue() + " Quantity: " + coal.getQuantity() + " Impact: " + coal.getImpact() + " Stability: " + coal.getStability() + " Extraction Cost: " + coal.getExtractionCost());
        System.out.println("Gas - Value: " + gas.getValue() + " Quantity: " + gas.getQuantity() + " Impact: " + gas.getImpact() + " Stability: " + gas.getStability() + " Extraction Cost: " + gas.getExtractionCost());
        System.out.println("Oil - Value: " + oil.getValue() + " Quantity: " + oil.getQuantity() + " Impact: " + oil.getImpact() + " Stability: " + oil.getStability() + " Extraction Cost: " + oil.getExtractionCost());
        System.out.println("Solar - Value: " + solar.getValue() + " Quantity: " + solar.getQuantity() + " Impact: " + solar.getImpact() + " Stability: " + solar.getStability() + " Extraction Cost: " + solar.getExtractionCost());
        System.out.println("Wind - Value: " + wind.getValue() + " Quantity: " + wind.getQuantity() + " Impact: " + wind.getImpact() + " Stability: " + wind.getStability() + " Extraction Cost: " + wind.getExtractionCost());
        System.out.println("Hydro - Value: " + hydro.getValue() + " Quantity: " + hydro.getQuantity() + " Impact: " + hydro.getImpact() + " Stability: " + hydro.getStability() + " Extraction Cost: " + hydro.getExtractionCost());
        System.out.println("Geothermal - Value: " + geothermal.getValue() + " Quantity: " + geothermal.getQuantity() + " Impact: " + geothermal.getImpact() + " Stability: " + geothermal.getStability() + " Extraction Cost: " + geothermal.getExtractionCost());
        System.out.println("Nuclear - Value: " + nuclear.getValue() + " Quantity: " + nuclear.getQuantity() + " Impact: " + nuclear.getImpact() + " Stability: " + nuclear.getStability() + " Extraction Cost: " + nuclear.getExtractionCost());
    }

    public Extractor buildExtractor(String resource) { // resource should come from the extraction type paid for.

        // Needs updating when possible to set extractionTexture depending on which resource is being extracted.

        if (resource != null) {
            this.extracting = true; // Each location (tile) can only have one extraction.

            // Make sure appropriate for type (WATER / LAND)
            if (this.type == "WATER") {
                switch(resource) {
                    case "COAL":
                        this.extractor = new CoalExtractor(coal);
                        this.extractingResource = Const.coal;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "GAS":
                        this.extractor = new GasExtractor(gas);
                        this.extractingResource = Const.gas;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "NUCLEAR":
                        this.extractor = null;
                        this.extractingResource = null;
                        //this.extractionTexture = null;
                        break;
                    case "OIL":
                        this.extractor = new OilExtractor(oil);
                        this.extractingResource = Const.oil;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "SOLAR":
                        this.extractor = null;
                        this.extractingResource = null;
                        //this.extractionTexture = null;
                        break;
                    case "WIND":
                        this.extractor = new WindExtractor(wind);
                        this.extractingResource = Const.wind;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "HYDRO":
                        this.extractor = new HydroExtractor(hydro);
                        this.extractingResource = Const.hydro;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "GEOTHERMAL":
                        this.extractor = new GeothermalExtractor(geothermal);
                        this.extractingResource = Const.geothermal;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                }
            } else if (this.type == "LAND") {
                switch(resource) {
                    case "COAL":
                        this.extractor = new CoalExtractor(coal);
                        this.extractingResource = Const.coal;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "GAS":
                        this.extractor = new GasExtractor(gas);
                        this.extractingResource = Const.gas;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "NUCLEAR":
                        this.extractor = new NuclearExtractor(nuclear);
                        this.extractingResource = Const.nuclear;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "OIL":
                        this.extractor = new OilExtractor(oil);
                        this.extractingResource = Const.oil;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "SOLAR":
                        this.extractor = new SolarExtractor(solar);
                        this.extractingResource = Const.solar;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "WIND":
                        this.extractor = new WindExtractor(wind);
                        this.extractingResource = Const.wind;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "HYDRO":
                        this.extractor = new HydroExtractor(hydro);
                        this.extractingResource = Const.hydro;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                    case "GEOTHERMAL":
                        this.extractor = new GeothermalExtractor(geothermal);
                        this.extractingResource = Const.geothermal;
                        //this.extractionTexture = new Texture("appropriatePath");
                        break;
                }
            }

            return this.extractor;
        }

        else return null;
    }

    public String getType() {
        return type;
    }
    public Extractor getExtractor() {
        return extractor;
    }

    public Boolean getExtracting() {
        return extracting;
    }

    public Coal getCoal() {
        return coal;
    }

    public Gas getGas() {
        return gas;
    }

    public Nuclear getNuclear() {
        return nuclear;
    }

    public Oil getOil() {
        return oil;
    }

    public Solar getSolar() {
        return solar;
    }

    public Wind getWind() {
        return wind;
    }

    public Hydro getHydro() {
        return hydro;
    }

    public Geothermal getGeothermal() {
        return geothermal;
    }

    public String getExtractingResource() {
        return extractingResource;
    }

    public Texture getExtractionTexture() {
        return extractionTexture;
    }

    public void setSearched(boolean b) {
        searched = b;
    }

    public boolean getSearched() {
        return searched;
    }
}
