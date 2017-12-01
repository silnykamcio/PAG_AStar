package com.geoida.progeo.RouteRepresenting;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Class for defining the options of route
 * Created by Konrad on 2017-03-17.
 */

public class RouteLineOptions {
    public int color;
    float width;

    public RouteLineOptions() {
        PolylineOptions defaultOptions = new PolylineOptions();
        this.width = defaultOptions.getWidth();
        this.color = defaultOptions.getColor();
    }

    public RouteLineOptions(int wdth, int clr) {
        this.width = wdth;
        this.color = clr;
    }
}
