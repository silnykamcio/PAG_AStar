package com.geoida.progeo.OSM.OSMObjects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Kamil on 15.07.2017.
 * Class for representing an nodes in OSM
 */

public class Node implements Comparable<Node>, Serializable {
    private Long ID;
    private LatLng Crd;
    private HashMap<String,Tag> tags;

    public Node(Long ID, LatLng Crd, HashMap<String,Tag> tags){
        this.ID = ID;
        this.Crd = Crd;
        this.tags = tags;
    }

    public Node(Long ID, LatLng Crd){
        this.ID = ID;
        this.Crd = Crd;
        this.tags = new HashMap<>();
    }

    public Tag getTag(String key) {
        if(tags.containsKey(key))
            return tags.get(key);
        else
            return new Tag("null","null");
    }
    public HashMap<String,Tag> getTags(){return tags;}
    public Long getID(){return ID;}
    public LatLng getCoords(){return Crd;}
    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Node){
            Node ptr = (Node) v;
            retVal = Objects.equals(ptr.ID, this.ID);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Node node) {
        if (Objects.equals(this.ID, node.getID())) {
            return 0;
        } else if (this.ID < node.getID()) {
            return -1;
        }
        return 1;
    }

}
