package com.geoida.progeo.OSM.OSMObjects;

import java.io.Serializable;
/**
 * Created by Kamil on 13.08.2017.
 * Class that represents OSM tag object
 */

public class Tag implements Serializable {
    private String key;
    private String value;
    public Tag(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
