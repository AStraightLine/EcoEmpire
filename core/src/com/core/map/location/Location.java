package com.core.map.location;

import com.core.map.extract.Extractor;
import com.core.map.extract.extractors.*;
import com.core.map.resource.resources.*;

public class Location {

    private String type;

    private Extractor extractor;
    private Boolean extracting;

    private Coal coal;
    private Gas gas;
    private Nuclear nuclear;
    private Oil oil;
    private Solar solar;
    private Wind wind;
    private Hydro hydro;
    private Tidal tidal; // Will need updating when we implement coast type. Until then, I'll just set everything to 0.
    private Geothermal geothermal;

    public Location(int type) {
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
        this.tidal = new Tidal(this.type);
        this.geothermal = new Geothermal(this.type);
    }

    public void setExtractor(String resource) { // resource should come from the extraction type paid for.
        if (resource != null) {
            this.extracting = true; // Each location (tile) can only have one extraction.

            switch(resource) {
                case "COAL":
                    this.extractor = new CoalExtractor(coal);
                    break;
                case "GAS":
                    this.extractor = new GasExtractor(gas);
                    break;
                case "NUCLEAR":
                    this.extractor = new NuclearExtractor(nuclear);
                    break;
                case "OIL":
                    this.extractor = new OilExtractor(oil);
                    break;
                case "SOLAR":
                    this.extractor = new SolarExtractor(solar);
                    break;
                case "WIND":
                    this.extractor = new WindExtractor(wind);
                    break;
                case "HYDRO":
                    this.extractor = new HydroExtractor(hydro);
                    break;
                case "TIDAL":
                    this.extractor = new TidalExtractor(tidal);
                    break;
                case "GEOTHERMAL":
                    this.extractor = new GeothermalExtractor(geothermal);
                    break;
            }
        }
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

    public Tidal getTidal() {
        return tidal;
    }

    public Geothermal getGeothermal() {
        return geothermal;
    }
}
