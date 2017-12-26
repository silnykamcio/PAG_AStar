package com.geoida.progeo.RouteGenerating.AStar;

import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by Alicja on 10.12.2017.
 * Class that generates route with use of A* Algorithm
 */

public class AStarAlgorithm {

    private Map<Long, GraphEdge> edges;
    private PriorityQueue<GraphVertex> notVisited;
    private Map<GraphVertex, GraphVertex> predecessors;
    private double geomDist;

    public AStarAlgorithm(Graph graph){
        edges = graph.getEdges();
        notVisited = new PriorityQueue<>(graph.getVertexCount(), new GraphVertex());
        System.out.println("Nodes count: " + graph.getVertexCount());
        predecessors = new HashMap<>();
    }

    private GraphVertex findMin(){
        return notVisited.remove();
    }

    private ArrayList<GraphVertex> reconstructPath(GraphVertex startV, GraphVertex endV){
        geomDist = SphericalUtil.computeDistanceBetween(startV.getCoords(),endV.getCoords());
        ArrayList<GraphVertex> path;
        path = new ArrayList<>();
        path.add(endV);
        while (!path.contains(startV)){
            path.add(predecessors.get(endV));
            endV = predecessors.get(endV);
        }
        return path;
    }

    private int getWeight(GraphVertex node, GraphVertex target){
        HashSet<Long> aIds = new HashSet<>(node.getEdgeIds());
        HashSet<Long> bIds = new HashSet<>(target.getEdgeIds());
        aIds.retainAll(bIds);
        GraphEdge e =  edges.get(aIds.iterator().next());
//        int weight;
//        if(prevGreenLvl > 0.5) {
//            weight =  e.getWeight() * ((int) (1 + (1-e.getGreenLevel())));
//        }
//        else {
//            weight =e.getWeight();
//        }
//        prevGreenLvl = e.getGreenLevel();
//        return weight;
        if(node.getGreenCost() > geomDist*0.5){
            return e.getWeight() * ((int) (1 + (1+e.getGreenLevel())*4));
        }
        else if(node.getGreenCost() > geomDist*0.05){
            return e.getWeight() * ((int) (1 + (1-e.getGreenLevel())*4));
        }
//        if(e.getCrossings() != 0) {
//            return e.getWeight() * node.getCrossCost()*2;
//        }
        else
            return e.getWeight();
    }

    private int getCrossWeight(GraphVertex node, GraphVertex target){
        HashSet<Long> aIds = new HashSet<>(node.getEdgeIds());
        HashSet<Long> bIds = new HashSet<>(target.getEdgeIds());
        aIds.retainAll(bIds);
        GraphEdge e =  edges.get(aIds.iterator().next());
        return e.getCrossings();
    }

    private double getGreenWeight(GraphVertex node, GraphVertex target){
        HashSet<Long> aIds = new HashSet<>(node.getEdgeIds());
        HashSet<Long> bIds = new HashSet<>(target.getEdgeIds());
        aIds.retainAll(bIds);
        GraphEdge e =  edges.get(aIds.iterator().next());
        return e.getGreenLevel()*e.getDistance();
    }


    private double computeHeuristics(GraphVertex n, GraphVertex end){
        return SphericalUtil.computeDistanceBetween(n.getCoords(),end.getCoords());
    }

    public ArrayList<GraphVertex> compute(GraphVertex startVertex, GraphVertex endVertex){
        startVertex.changeCost(0);
        notVisited.add(startVertex);
        Set<GraphVertex> visited = new HashSet<>();
        while(!notVisited.isEmpty()){
            GraphVertex x = findMin();
            if( x.equals(endVertex)){
                return reconstructPath(startVertex, endVertex);
            }
            visited.add(x);

            for(GraphVertex y:x.getNeighbours()){
                if(visited.contains(y)){
                    continue;
                }
                Integer crossingsScore = x.getCrossCost() + getCrossWeight(x,y);
                Double greenScore = x.getGreenCost() + getGreenWeight(x,y);
                Double tentativeScore = x.getCost() + getWeight(x,y);
                Boolean tentativeBetter = false;

                if(tentativeScore < y.getCost()){
                    notVisited.add(y);
                    y.setHeuristic(computeHeuristics(y,endVertex));
                    tentativeBetter = true;
                }
                if(tentativeBetter){
                    predecessors.put(y, x);
                    y.changeCost(tentativeScore);
                    y.changeCrossCost(crossingsScore);
                    y.changeGreenCost(greenScore);
                }
            }

        }
        return null;
    }



}
