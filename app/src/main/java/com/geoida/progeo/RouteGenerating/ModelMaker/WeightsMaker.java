package com.geoida.progeo.RouteGenerating.ModelMaker;


import android.util.Log;

import com.geoida.progeo.OSM.OSMObjects.Way;
import com.geoida.progeo.RouteGenerating.GeneratingPreferences;


import java.util.HashSet;

/**
 * Created by Kamil on 20.07.2017.
 * This class converts data from datasources such as way types. green level and so on to integer weight values for graph alghrotihm.
 *
 * One and only rule in weight genereting is to keep weight always higher or equal to route length (this fact is used later in path searching algorithms).
 */

class WeightsMaker {

    private HashSet<String> goodWays;
    private HashSet<String> mediumWays;

    WeightsMaker() {
        goodWays = new HashSet<>();
        mediumWays = new HashSet<>();
        goodWays.add("path");
        goodWays.add("footway");
        goodWays.add("bridleway");
        goodWays.add("cycleway");
        mediumWays.add("track");
        mediumWays.add("living_street");
        mediumWays.add("residental");
        mediumWays.add("unclassified");
    }

    public double convert(String wayType,double len){
        if(goodWays.contains(wayType)) return (1*len);
        else if(mediumWays.contains(wayType)) return  (1.5*len);
        else return (2*len);
    }
    //TODO: Weights have to be stabilized!!!
    double convert(Way w, GeneratingPreferences gp){
        String type = w.getType();
        double weight;

        if(w.isPrivate())
            return Double.MAX_VALUE;

            float waysPref =  (float)gp.getRoutePriority()/100;
            float crossPref = (float)gp.getCrossRoadsPriority()/100;
            float greenPref = (float)gp.getGreenPriority()/100;

            double routeWeight = 0;
            if (goodWays.contains(type)) routeWeight = waysPref;
            else if (mediumWays.contains(type)) routeWeight = waysPref*1.5;
            else routeWeight = waysPref*2;


            double greenWeight = 0;
            greenWeight = (2 - w.getGreenLevel())*greenPref;

            weight = 1 + (routeWeight + greenWeight);
            //System.out.println("distance: " + w.getDistance());
            weight *= w.getDistance();
            double crossingWeight = 0;
            crossingWeight =  1 + (w.getCrossings()*crossPref);
            weight *= crossingWeight;
            //Log.wtf("WeightsMaker", "Weight: " + weight);
            return weight;
    }
}
