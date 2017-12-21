package com.geoida.progeo.OSM.OSMObjects;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Class for representing an way from OSM
 * Created by Kamil on 15.07.2017.
 */

public class Way implements Serializable {
    private Long ID;
    private String TYPE;
    private double distance;
    private ArrayList<Node> Nodes;
    private ArrayList<Node> CrossNodes;
    private int crossings;
    // private String enviroType;
    private double greenLevel;
    private double PositiveSlope;
    private double NegativeSlope;
    private int maxHeight;
    private int minHeight;
    private boolean isPrivate;
    private HashMap<Integer,Integer> tiles;


    public ArrayList<Node> getNodes(){return Nodes;}

    public Way(Long ID, List<Node> nodeIds, List<Node>cross, String TYPE)
    {
        this.ID = ID;
        this.TYPE = TYPE;
        CrossNodes = new ArrayList<>();
        CrossNodes.addAll(cross);
        Nodes = new ArrayList<>();
        Nodes.addAll(nodeIds);
        this.distance = computeDistance(Nodes);
        crossings = 0;
        isPrivate = false;
    }

    public Way(Long ID, List<Node> nodeIds,List<Node>cross, String TYPE, int crossings, String enviroType, boolean isPrivate, HashMap<Integer,Integer> tiles)
    {
        this.ID = ID;
        this.TYPE = TYPE;
        // this.enviroType = enviroType;
        CrossNodes = new ArrayList<>();
        CrossNodes.addAll(cross);
        Nodes = new ArrayList<>();
        Nodes.addAll(nodeIds);
        this.distance = computeDistance(Nodes);
        this.crossings = crossings;
        this.isPrivate = isPrivate;
        this.tiles = new HashMap<>(tiles);
    }

    public Way(Long ID, List<Node> nodeIds,List<Node>cross, String TYPE, int crossings, boolean isPrivate, HashMap<Integer,Integer> tiles, double greenLevel
               ,double PositiveSlope, double NegativeSlope, int minHeight, int maxHeight)
    {
        this.ID = ID;
        this.TYPE = TYPE;
        CrossNodes = new ArrayList<>();
        CrossNodes.addAll(cross);
        Nodes = new ArrayList<>();
        Nodes.addAll(nodeIds);
        this.distance = computeDistance(Nodes);
        this.crossings = crossings;
        this.isPrivate = isPrivate;
        this.tiles = new HashMap<>(tiles);
        this.greenLevel = greenLevel;
        this.PositiveSlope = PositiveSlope;
        this.NegativeSlope = NegativeSlope;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    //public void setEnviroType(String tag){ enviroType = tag;}
    public void setGreenLevel(double level) { this.greenLevel = level;}
    public void setSlope(double[] slope) {
        PositiveSlope = slope[0];
        NegativeSlope = slope[1];
        maxHeight = (int)slope[2];
        minHeight = (int)slope[3];
    }

    private double computeDistance(ArrayList<Node> nodes){
        double length = 0;
        for(int i = 0; i < nodes.size()-1;i++){
            Location a = new Location("a");
            a.setLatitude(nodes.get(i).getCoords().latitude);
            a.setLongitude(nodes.get(i).getCoords().longitude);

            Location b = new Location("b");
            b.setLatitude(nodes.get(i+1).getCoords().latitude);
            b.setLongitude(nodes.get(i+1).getCoords().longitude);
            length+=a.distanceTo(b);
        }
        return length;
    }

    public void addCross(List<Node>cross){
        CrossNodes.clear();
        CrossNodes.addAll(cross);
    }
    public String getType() {return TYPE;}
    public Long getID() {return ID;}
    public ArrayList<Node> GetNodes(){return Nodes;}
    public ArrayList<Node> GetCrossNodes(){return CrossNodes;}
    public double getDistance() {return distance;}

    public int getCrossings() {
        return crossings;
    }

    public double getQuality(){

        HashSet<String> goodWays;
        HashSet<String> mediumWays;
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
        if(goodWays.contains(TYPE)) return (1);
        else if(mediumWays.contains(TYPE)) return  (1.5);
        else return (2);
    }

//    public String getEnviroType() {
//        return enviroType;
//    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public HashMap<Integer, Integer> getTiles() {
        return tiles;
    }

    public double getGreenLevel() {
        return greenLevel;
    }

    public double getPositiveSlope() {
        return PositiveSlope;
    }

    public double getNegativeSlope() {return NegativeSlope;}

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }


}
