package com.core;

import java.util.*;

public class Const{

    //Pixel per metre
    public static final float PPM = 32;
    public static final int treeX = 2, treeY = 4;
    // Infinity representation for renewable resource quantity (and anything else)
    public static final Double infinity = Double.POSITIVE_INFINITY;
    // resources
    public static final String coal = "COAL", gas = "GAS", nuclear = "NUCLEAR", oil = "OIL", solar = "SOLAR", wind = "WIND", hydro = "HYDRO", geothermal = "GEOTHERMAL";
    public static final String[] resourceNames = {"Coal", "Gas", "Nuclear", "Oil", "Solar", "Wind", "Hydro", "Geothermal"};
    public static final String[] offsetNames = {"Carbon Capture", "Climate Research", "Infrastructure Investment", "Lobby", "Solar Geoengineering", "Transport Investment", "Tree"};
    // tile types
    public static final String water = "WATER", land = "LAND", sand = "SAND";
    //Base climate health
    public static final Double baseHealth = 1000.0;

    // Extractor paths
    public static final Map<String, ArrayList<String>> paths;
    static {
        Map<String, ArrayList<String>> initMap = new HashMap<String, ArrayList<String>>();

        // Coal
        ArrayList<String> coalPaths = new ArrayList<String>();
        coalPaths.add("land-coal-mine.png");
        coalPaths.add("sea-coal-mine.png");
        initMap.put(Const.coal, coalPaths);
        // Gas
        ArrayList<String> gasPaths = new ArrayList<String>();
        gasPaths.add("land-gas.png");
        gasPaths.add("sea-gas.png");
        initMap.put(Const.gas, gasPaths);
        // Nuclear
        ArrayList<String> nuclearPaths = new ArrayList<String>();
        nuclearPaths.add("nuclear.png");
        initMap.put(Const.nuclear, nuclearPaths);
        // Oil
        ArrayList<String> oilPaths = new ArrayList<String>();
        oilPaths.add("land-rig.png");
        oilPaths.add("sea-rig.png");
        initMap.put(Const.oil, oilPaths);
        // Solar
        ArrayList<String> solarPaths = new ArrayList<String>();
        solarPaths.add("solar-land.png");
        initMap.put(Const.solar, solarPaths);
        // Wind
        ArrayList<String> windPaths = new ArrayList<String>();
        windPaths.add("wind-turbine-land.png");
        windPaths.add("wind-turbine-sea.png");
        initMap.put(Const.wind, windPaths);
        // Hydro
        ArrayList<String> hydroPaths = new ArrayList<String>();
        hydroPaths.add("hydro-land.png");
        hydroPaths.add("hydro-sea.png");
        initMap.put(Const.hydro, hydroPaths);
        // Geothermal
        ArrayList<String> geothermalPaths = new ArrayList<String>();
        geothermalPaths.add("geothermal-land.png");
        geothermalPaths.add("geothermal-sea.png");
        initMap.put(Const.geothermal, geothermalPaths);

        paths = Collections.unmodifiableMap(initMap);
    }
}
