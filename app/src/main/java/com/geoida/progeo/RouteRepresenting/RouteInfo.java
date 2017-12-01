package com.geoida.progeo.RouteRepresenting;

import android.util.Pair;

import com.geoida.progeo.OSM.OSMObjects.Way;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Kamil on 2017-08-15.
 * Class that holds the info about route to be presented
 */

public class RouteInfo {
    private int length;
    private int goodWaysLvl;
    private float greenEnviroLvl;
    private float meanPosSlope;
    private float meanNegSlope;
    private float maxPosSlope;
    private float maxNegSlope;
    private int denivelation;
    private int crossings;

    private String name;

    private HashSet<String> goodWays;
    private HashSet<String> mediumWays;

    private ArrayList<LatLng>pntList;

    public RouteInfo() {
        name = "Default";
        length = 0;
        goodWaysLvl = 0;
        greenEnviroLvl = 0;
        crossings = 0;
    }

    public RouteInfo(ArrayList<Way> ways, ArrayList<LatLng>pntList){
        this.pntList = pntList;
        length = 0;
        goodWaysLvl = 0;
        greenEnviroLvl = 0.0F;
        crossings = 0;
        goodWays = new HashSet<>();
        mediumWays = new HashSet<>();
        goodWays.add("path");
        goodWays.add("footway");
        goodWays.add("bridleway");
        mediumWays.add("track");
        mediumWays.add("living_street");
        mediumWays.add("residental");
        mediumWays.add("unclassified");
        getInfo(ways);
    }

    void setMeanPosSlope(float meanPosSlope){
        this.meanPosSlope = meanPosSlope;
    }

    void setMeanNegSlope(float meanNegSlope){
        this.meanNegSlope = meanNegSlope;
    }

    void setMaxPosSlope(float maxPosSlope){
        this.maxPosSlope = maxPosSlope;
    }

    void setMaxNegSlope(float maxNegSlope){
        this.maxNegSlope = maxNegSlope;
    }

    private void getInfo(ArrayList<Way> ways){

        float goodWaysCounter = 0;
        double greenEnviroCounter = 0;
        double posSlopeSum = 0;
        double negSlopeSum = 0;
        int posSlopeCount = 0;
        int negSlopeCount = 0;

        int maxHeightValue = Integer.MIN_VALUE;
        int minHeightValue = Integer.MAX_VALUE;

        double maxPosSlope = 0;
        double maxNegSlope = 0;
        for(Way w : ways){
            length+= w.getDistance();
            String wayType = w.getType();
            double enviroType = w.getGreenLevel();
            if(goodWays.contains(wayType) || mediumWays.contains(wayType))
                goodWaysCounter += w.getDistance();

            greenEnviroCounter += w.getDistance()*enviroType;

            if(w.getMaxHeight() > maxHeightValue)
                maxHeightValue = w.getMaxHeight();
            if(w.getMinHeight() < minHeightValue)
                minHeightValue = w.getMinHeight();

            System.out.println("max: " + w.getMaxHeight());
            System.out.println("min: " + w.getMinHeight());

            posSlopeSum += w.getPositiveSlope();
            if(w.getPositiveSlope() != 0)
                posSlopeCount++;

            negSlopeSum += w.getNegativeSlope();
            if(w.getNegativeSlope() != 0)
                negSlopeCount++;

            if(w.getPositiveSlope() > maxPosSlope)
                maxPosSlope = w.getPositiveSlope();

            if(w.getNegativeSlope() > maxNegSlope)
                maxNegSlope = w.getNegativeSlope();

            crossings += w.getCrossings();
        }
        if(maxHeightValue != Integer.MIN_VALUE && minHeightValue != Integer.MAX_VALUE)
            denivelation = maxHeightValue - minHeightValue;
        else
            denivelation = Integer.MIN_VALUE;
        meanPosSlope = (float)(posSlopeSum/(float)posSlopeCount);
        meanNegSlope = (float)(negSlopeSum/(float)negSlopeCount);
        this.maxPosSlope = (float)maxPosSlope;
        this.maxNegSlope = (float)maxNegSlope;
        goodWaysLvl = (int)((goodWaysCounter/(float)length)*100);
        greenEnviroLvl =  (int)((greenEnviroCounter/(float)length)*100);
        System.out.println(greenEnviroLvl);
    }

    public double getGreenEnviroLvl() {
        return greenEnviroLvl;
    }

    public int getGoodWaysLvl() {
        return goodWaysLvl;
    }

    public int getLength() {
        return length;
    }

    public int getCrossings() {
        return crossings;
    }

    public ArrayList<LatLng> getPointList(){
        return pntList;
    }

    void setGreenEnviroLvl(int lvl) {greenEnviroLvl = lvl;}

    void setGoodWaysLvl(int lvl) {goodWaysLvl = lvl;}

    void setLength(int length) {this.length = length;}

    void setCrossings(int crossings) {this.crossings = crossings;}

    void setPntList(ArrayList<LatLng> points) {pntList = points;}

    public void setName (String name) {this.name = name;}

    void setDenivelation (int denivelation) {this.denivelation = denivelation;}

    /*
    Method which return min from to LatLng
    @param LatLnt for comparison
    @param LatLnt for comparison
 */
    //boundaring box method
    private LatLng min(LatLng lf, LatLng rt) {
        return new LatLng(Math.min(lf.latitude, rt.latitude), Math.min(lf.longitude, rt.longitude));

    }
    /*
        Method which return min from to LatLng
        @param LatLnt for comparison
        @param LatLnt for comparison
    */
    private LatLng max(LatLng lf, LatLng rt) {
        return new LatLng(Math.max(lf.latitude, rt.latitude), Math.max(lf.longitude, rt.longitude));
    }
    /*
    Method which return boundaring box of the route
 */
    public Pair<LatLng, LatLng> boundingBox() {
        LatLng pmin = pntList.get(0), pmax = pntList.get(0);

        for (int i = 1; i < pntList.size(); ++i) {
            pmin = min(pmin, pntList.get(i));
            pmax = max(pmax, pntList.get(i));
        }

        return new Pair<LatLng, LatLng>(pmin, pmax);
    }

    float getMeanPosSlope() {
        return meanPosSlope;
    }

    float getMeanNegSlope() {
        return meanNegSlope;
    }

    float getMaxPosSlope() {
        return maxPosSlope;
    }

    float getMaxNegSlope() {
        return maxNegSlope;
    }

    public int getDenivelation() {
        return denivelation;
    }

    public String getName() {
        return name;
    }
}
