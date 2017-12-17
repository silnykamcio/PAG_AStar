package com.geoida.progeo.RouteGenerating.Dijkstra;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Kamil on 04.07.2017.
 * Class representing an graph made from vertices and edges.
 */

public class Graph {
    private final Map<Long,GraphVertex> vertexes;
    private final Map<Long,GraphEdge> edges;

    public Graph(Map<Long,GraphVertex> vertexes, Map<Long,GraphEdge> edges)
    {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public int getVertexCount() {return vertexes.size();}
    public void showGraphInfo(){
        System.out.println("Graph vertices:");
        int i = 1;
        for(GraphVertex v : vertexes.values()){
            System.out.println(i + ". ID: " + v.getId());
            i++;
        }
        System.out.println("Graph edges:");
        int j = 1;
        for(GraphEdge e : edges.values()){
            System.out.println(j + ". ID: " + e.getId() + " Destination: " + e.getDestination().getId() + " Source: " + e.getSource().getId() + " Weight: " + e.getWeight());
        }
    }

    public int getRouteweight(LinkedList<GraphVertex> route){
        int weight = 0;
        for(int i = 0; i < route.size()-1;i++){
            weight += findEdge(route.get(i), route.get(i+1)).getWeight();
        }
        return weight;
    }

    private GraphEdge findEdge(GraphVertex a, GraphVertex b){
        for(GraphEdge e : getEdges().values()){
            if(e.getDestination() == b && e.getSource() == a)
                return e;
        }
        return null;
    }

    Map<Long,GraphVertex> getVertexes()
    {
        return vertexes;
    }

    public Map<Long,GraphEdge> getEdges() {
        return edges;
    }
}
