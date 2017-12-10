package com.geoida.progeo.RouteGenerating.AStar;

import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;

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

    private List<GraphVertex> reconstructPath(GraphVertex endV){};

    public List<GraphVertex> compute(GraphVertex startVertex, GraphVertex endVertex){
        startVertex.changeCost(0);
        notVisited.add(startVertex);
        visited = new HashSet<GraphVertex>();
        while(!notVisited.isEmpty()){
            GraphVertex x = findMin();
            if( x.equals(endVertex)){
                return reconstructPath(endVertex);
            }
            notVisited.remove(x);
            visited.add(x);
            for(GraphVertex y:x.getNeighbours()){
                if(visited.contains(y)){
                    continue;
                }
                //Double tentativeScore = x.getCost() + TODO create heuristic values

            }


        }
    }



}
