package com.geoida.progeo.RouteGenerating;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kamil on 10.08.2017.
 * Class for holding all user preferences during generating process
 */

public class GeneratingPreferences {

    private int greenPriority;
    private int routePriority;
    private int crossRoadsPriority;

    private LatLng startPoint;
    private LatLng endPoint;

    public GeneratingPreferences(int greenPriority, int routePriority, int crossRoadsPriority, LatLng startPoint, LatLng endPoint){
        this.greenPriority = greenPriority;
        this.routePriority = routePriority;
        this.crossRoadsPriority = crossRoadsPriority;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public int getGreenPriority() {
        return greenPriority;
    }

    public int getRoutePriority() {
        return routePriority;
    }

    public int getCrossRoadsPriority() {
        return crossRoadsPriority;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }
}
