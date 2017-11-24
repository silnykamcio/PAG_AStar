package com.kamcioikoalcia.routing.Graph;

import java.util.Objects;

/**
 * Created by Kamil on 23.11.2017.
 * Class that represents graph edge
 */

public class Edge{
    //private long id;
    private String id_from;
    private String id_to;



    public Edge(Node a, Node b){
        id_from = a.getNodeId();
        id_to = a.getNodeId();
    }

    public String getEdgeInfo(){
        return "From ID: " + id_from + " To ID: " + id_to;
    }

    public String getId_from(){return id_from;}
    public String getId_to(){return id_to;}

}
