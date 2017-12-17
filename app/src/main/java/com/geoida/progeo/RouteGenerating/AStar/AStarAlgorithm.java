package com.geoida.progeo.RouteGenerating.AStar;

import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.geoida.progeo.Utils.Stoper;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by Alicja on 10.12.2017.
 */

public class AStarAlgorithm {

    private Map<Long, GraphEdge> edges;
    private Set<GraphVertex> visited;
    private PriorityQueue<GraphVertex> notVisited;
    private Map<GraphVertex, GraphVertex> predecessors;

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
        return edges.get(aIds.iterator().next()).getWeight();
    }

    private double computeHeuristics(GraphVertex n, GraphVertex end){
        return SphericalUtil.computeDistanceBetween(n.getCoords(),end.getCoords());
    }

    public ArrayList<GraphVertex> compute(GraphVertex startVertex, GraphVertex endVertex){
        startVertex.changeCost(0);
        notVisited.add(startVertex);
        visited = new HashSet<GraphVertex>();
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
                }
            }

        }
        return null;
    }



}
