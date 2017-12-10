package com.geoida.progeo.RouteGenerating.Dijkstra;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Kamil on 04.07.2017.
 * Class that represents single vertex in graph. Each vertex have an id and can be connected to one or more edges
 */

public class GraphVertex implements Comparable<GraphVertex>, Comparator<GraphVertex> {
    final private Long id;
    private double cost;
    private int distance;
    private HashSet<Long> edgeIds;
    private HashSet<GraphVertex> neighbours;

    public GraphVertex(){this.id = null;}

    public GraphVertex(Long id)
    {
        this.cost = Double.MAX_VALUE;
        this.id = id;
        edgeIds = new HashSet<>();
        neighbours = new HashSet<>();
    }


    public void changeCost(double cost){
        this.cost = cost;
    }
    void changeDistance(int distance) {this.distance = distance;}

    public Long getId()
    {
        return id;
    }
    public double getCost() {return cost;}
    public int getDistance() {return distance;}
    public void addEdgeId(Long id){edgeIds.add(id);}
    public void addNeighbour(GraphVertex n) {neighbours.add(n);}
    public HashSet<Long> getEdgeIds(){return edgeIds;}
    public Set<GraphVertex> getNeighbours(){return neighbours;}
    @Override
    public int hashCode() {
        final int prime = 31;
        return 17 * prime+((id==null) ? 0 : id.hashCode());
    }

    @Override
    public int compare(GraphVertex graphVertex, GraphVertex t1) {
        if(graphVertex.getCost() < t1.getCost())
            return -1;
        else if(graphVertex.getCost() > t1.getCost())
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass()!= obj.getClass()) return false;
        GraphVertex comp = (GraphVertex) obj;
        return !((id == null && comp.id != null) || !id.equals(comp.id));
    }

    @Override
    public int compareTo(@NonNull GraphVertex gv) {
        if (Objects.equals(this.id, gv.getId())) {
            return 0;
        } else if (this.id < gv.getId()){
            return -1;
        }
        return 1;
    }
}
