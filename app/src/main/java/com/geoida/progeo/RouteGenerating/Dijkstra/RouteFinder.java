package com.geoida.progeo.RouteGenerating.Dijkstra;


import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by Kamil on 04.07.2017.
 * The "engine" of Dijkstra algorithm implementation where full graph is analyised. In effect one can get best route from A to B in O(1) complexivity.
 * It still needs some improvements in order to achieve best optimalization
 */

public class RouteFinder {
    private final Map<Long,GraphEdge> edges;
    private Set<GraphVertex> settledNodes;
    private Map<GraphVertex, GraphVertex> predecessors;
    private PriorityQueue<GraphVertex> unSettledQueue;

    public RouteFinder(Graph graph)
    {
        this.edges = new HashMap<>(graph.getEdges());
        this.unSettledQueue = new PriorityQueue<GraphVertex>(graph.getVertexCount(),new GraphVertex());
    }

    /**
     * Copmputes the best paths from staring vertex with respect to given weights
     * @param StartVertex Starting node
     */
    public void compute(GraphVertex StartVertex){
        settledNodes = new HashSet<>();
        predecessors = new HashMap<>();

        StartVertex.changeCost(0.0);
        StartVertex.changeDistance(0);
        unSettledQueue.add(StartVertex);
        while(!unSettledQueue.isEmpty()){
            GraphVertex node = getMinimum();
            settledNodes.add(node);
            findMinimalWeights(node);
        }

    }

    /**
     * @return minimum value from prority queue
     */
    private GraphVertex getMinimum() {
        return unSettledQueue.remove();
    }

    /**
     * Finds minimal weights from staring node
     * @param node starting node
     */
    private void findMinimalWeights(GraphVertex node){
        Set<GraphVertex> adjacentNodes = getNeighbors(node);
        int smallestWeight = (int)node.getCost();
        int smallestDist = node.getDistance();
        for(GraphVertex target : adjacentNodes)
        {
            if(!isSettled(target)) {
                int v[] = getWeight(node,target);
                int weight = v[0];
                int distance = v[1];
                if (target.getCost() > smallestWeight + weight) {
                    target.changeCost(smallestWeight + weight);
                    target.changeDistance(smallestDist + distance);
                    predecessors.put(target, node);
                    unSettledQueue.add(target);
                }
            }
        }
    }

    /**
     * Gets weigh of the edge between two nodes
     * @param node starting node
     * @param target ending node
     * @return weight value
     */
    private int[] getWeight(GraphVertex node, GraphVertex target){
        int[] vals = new int[2];
        HashSet<Long> aIds = new HashSet<>(node.getEdgeIds());
        HashSet<Long> bIds = new HashSet<>(target.getEdgeIds());
        aIds.retainAll(bIds);
        vals[0] = edges.get(aIds.iterator().next()).getWeight();
        vals[1] = (int)edges.get(aIds.iterator().next()).getDistance();
        return vals;
    }

    /**
     * Get all neighbouring nodes of given node (connected only with one edge)
     * @param node Starting node
     * @return List of all neighbours of starting node
     */
    private Set<GraphVertex> getNeighbors(GraphVertex node) {
        return node.getNeighbours();
    }

    /**
     * Checks if given node is already settled
     * @param destination vertex to be checked
     * @return true if settled, false otherwise
     */
    private boolean isSettled(GraphVertex destination) {
        return settledNodes.contains(destination);
    }

    /**
     * Gets an list with nodes that are making an optimal route between starting point and given target
     * @param target Target
     * @return List with nodes
     */
    public LinkedList<GraphVertex> getPath(GraphVertex target){
        LinkedList<GraphVertex> path = new LinkedList<>();
        GraphVertex step = target;
        path.add(step);
        while(predecessors.get(step)!=null){
            step = predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        Log.wtf("path_size"," "+ path.size());
        return path;
    }

}
