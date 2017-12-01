package com.geoida.progeo.OSM.OSMObjects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Kamil on 01.12.2017.
 * Class that represents node that can be saved as binary file
 */

public class SaveNode implements Serializable {
    private Long ID;
    private double Lat;
    private double Lng;
    private HashMap<String,Tag> tags;
    public SaveNode(Node n){
        this.ID = n.getID();
        LatLng temp = n.getCoords();
        this.Lat = temp.latitude;
        this.Lng = temp.longitude;
        this.tags = n.getTags();
    }

    public Node getNode(){
        return new Node(ID,new LatLng(Lat,Lng),tags);
    }
}
