package com.geoida.progeo.OSM.OSMObjects;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kamil on 01.12.2017.
 * CLass that represents way that can be saved as binary
 */

public class SaveWay implements Serializable{
    private Long ID;
    private String TYPE;
    private double distance;
    private ArrayList<SaveNode> Nodes;
    private ArrayList<SaveNode> CrossNodes;
    private int crossings;
    // private String enviroType;
    private double greenLevel;
    private double PositiveSlope;
    private double NegativeSlope;
    private int maxHeight;
    private int minHeight;
    private boolean isPrivate;
    private HashMap<Integer,Integer> tiles;
    public SaveWay(Way w){
        ID = w.getID();
        TYPE = w.getType();
        distance = w.getDistance();
        ArrayList<Node> nds = w.getNodes();
        Nodes = new ArrayList<>();
        for(Node n : nds){
            Nodes.add(new SaveNode(n));
        }
        ArrayList<Node> cNds = w.GetCrossNodes();
        CrossNodes = new ArrayList<>();
        for(Node n : cNds)
        {
            CrossNodes.add(new SaveNode(n));
        }
        crossings = w.getCrossings();
        greenLevel = w.getGreenLevel();
        PositiveSlope = w.getPositiveSlope();
        NegativeSlope = w.getNegativeSlope();
        maxHeight = w.getMaxHeight();
        minHeight = w.getMinHeight();
        isPrivate = w.isPrivate();
        tiles = w.getTiles();
    }

    public Way getWay(){
        ArrayList<Node> nodes = new ArrayList<>();
        for(SaveNode n : Nodes){
            nodes.add(n.getNode());
        }
        ArrayList<Node> cNodes = new ArrayList<>();
        for(SaveNode n : CrossNodes){
            cNodes.add(n.getNode());
        }
        return new Way(ID,nodes,cNodes,TYPE,crossings,isPrivate,tiles,greenLevel,PositiveSlope,NegativeSlope,minHeight,maxHeight);
    }
}
