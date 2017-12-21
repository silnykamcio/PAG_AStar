package com.geoida.progeo.RouteGenerating.Dijkstra;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by Kamil on 04.07.2017.
 * Class that represents graph edge. Each edge poses information about its source and destination vertex, id and weight.
 */

public class GraphEdge implements Comparable<GraphEdge> {
    private final String id;
    private final GraphVertex src;
    private final GraphVertex dest;
    private int weight;
    private final double distance;
    private double greenLevel;
    private int crossings;
    private double quality;

    public GraphEdge(String id, GraphVertex src, GraphVertex dest, int weight, double distance, double greenLevel, int crossings, double quality){
        this.id = id;
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.distance = distance;
        this.greenLevel = greenLevel;
        this.crossings = crossings;
        this.quality = quality;
    }

    public String getId() {
        return id;
    }
    GraphVertex getDestination(){
        return dest;
    }
    GraphVertex getSource(){
        return src;
    }
    public int getWeight() {
        return weight;
    }
    public double getDistance() { return  distance;}

    public void changeWeight (int weight) {this.weight= weight;}

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;
        if (v instanceof GraphEdge){
            GraphEdge ptr = (GraphEdge) v;
            retVal = Objects.equals(ptr.getId(), this.id);
        }
        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(@NonNull GraphEdge ge) {
        if ((Objects.equals(ge.getSource(), this.src)) && (Objects.equals(ge.getDestination(), this.dest))) {
            return 0;
        } else if (Long.parseLong(this.id) < Long.parseLong(ge.getId())){
            return -1;
        }
        return 1;
    }

    public double getGreenLevel() {
        return greenLevel;
    }

    public int getCrossings() {
        return crossings;
    }

    public double getQuality() {
        return quality;
    }
}
