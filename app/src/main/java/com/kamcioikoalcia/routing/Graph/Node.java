package com.kamcioikoalcia.routing.Graph;

/**
 * Created by Kamil on 23.11.2017.
 * Class that represents graph nodes
 */

public class Node {
    private long id;
    private double x;
    private double y;

    public Node(double x, double y){
        this.x = x;
        this.y = y;
        getIdValue(x,y);
    }

    private void getIdValue(double x, double y){
        String xStr = String.valueOf(x);
        String yStr = String.valueOf(y);
        xStr = xStr.replace(".","");
        yStr = yStr.replace(".","");
        this.id = Long.valueOf(xStr.substring(0,5) + yStr.substring(0,5));
    }

    public String getNodeInfo(){
        return "X: " + x + " Y: " + y + " ID: " + id;
    }

    public long getNodeId() {return id;}


}
