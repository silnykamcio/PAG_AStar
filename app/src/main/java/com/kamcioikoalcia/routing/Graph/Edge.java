package com.kamcioikoalcia.routing.Graph;

/**
 * Created by Kamil on 23.11.2017.
 * Class that represents graph edge
 */

public class Edge {
    //private long id;
    private long id_from;
    private long id_to;

    public Edge(Node a, Node b){
        id_from = a.getNodeId();
        id_to = a.getNodeId();
    }

    public String getEdgeInfo(){
        return "From ID: " + id_from + " To ID: " + id_to;
    }
}
