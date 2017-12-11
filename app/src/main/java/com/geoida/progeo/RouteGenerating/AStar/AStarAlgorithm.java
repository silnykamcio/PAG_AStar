package com.geoida.progeo.RouteGenerating.AStar;

import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alicja on 10.12.2017.
 */

public class AStarAlgorithm {

    private Map<Long, GraphEdge> edges;
    private Set<GraphVertex> visited;
    private Set<GraphVertex> notVisited;
    private Map<GraphVertex, GraphVertex> predecessors;

    public AStarAlgorithm(Graph graph){
        edges = graph.getEdges();
    }

    private GraphVertex findMin(){
        Iterator<GraphVertex> iterator = notVisited.iterator();
        GraphVertex min = iterator.next();
        while(iterator.hasNext()) {
            GraphVertex nextVertex = iterator.next();
            if(min.getCost()>nextVertex.getCost()) {
                min = nextVertex;
            }
        }
        return min;
    }

    private List<GraphVertex> reconstructPath(GraphVertex startV, GraphVertex endV){
        List<GraphVertex> path;
        path = new ArrayList<GraphVertex>();
        path.add(endV);
        while (!path.contains(startV)){
            path.add(predecessors.get(endV));
            endV = predecessors.get(endV);
        }
        return path;
    }

    public List<GraphVertex> compute(GraphVertex startVertex, GraphVertex endVertex){
        startVertex.changeCost(0);
        notVisited.add(startVertex);
        visited = new HashSet<GraphVertex>();
        while(!notVisited.isEmpty()){
            GraphVertex x = findMin();
            if( x.equals(endVertex)){
                return reconstructPath(startVertex, endVertex);
            }
            notVisited.remove(x);
            visited.add(x);
            for(GraphVertex y:x.getNeighbours()){
                if(visited.contains(y)){
                    continue;
                }
                Double tentativeScore = x.getCost(); // + function TODO by Kamcio
                Boolean tentativeBetter = false;
                if (!notVisited.contains(y)){
                    notVisited.add(y);
                    //h_score[y] := heuristic_estimate_of_distance_to_goal_from(y) TODO by Kamcio
                    tentativeBetter = true;
                }
                else if(tentativeScore < y.getCost()){
                    tentativeBetter = true;
                }
                if(tentativeBetter == true){
                    predecessors.put(y, x);
                    y.changeCost(tentativeScore);
                    //f_score[y] := g_score[y] + h_score[y] waiting for Kamcio
                }
            }

        }
        return null;
    }



}
