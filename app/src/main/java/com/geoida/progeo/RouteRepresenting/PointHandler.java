package com.geoida.progeo.RouteRepresenting;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 06.03.2017.
 * Class for representation and storage of point choosed by user
 */

public class PointHandler {

    //List of points
    private List<LatLng> LoP = new ArrayList<>() ;

    PointHandler() {}

    /**
     * Adding LatLng point to object
     * @param point LatLng point value that will be added to container
     */
    void AddPoint(LatLng point)
    {
        LoP.add(point);
    }

    /**
     *
     * @param lati latitude of point
     * @param longi longitude of point
     */
    public void AddPoint(float lati, float longi)
    {
        LatLng pnt = new LatLng(lati,longi);
        LoP.add(pnt);
    }

    /**
     * Adding marker of lastly added point
     * @param map GoogleMap map object needed to place marker
     */
    public void AddMarkerToMap(GoogleMap map)
    {
        map.addMarker(new MarkerOptions()
                .position(LoP.get(LoP.size() - 1))
                .title("Point "+(LoP.size()-1)));
    }

    /**
     * Adding marker of i-th point
     * @param i number of point to be showed as marker (i=0 -> first point)
     * @param map GoogleMap map object needed to place marker
     * @return true if operation successful
     */
    public boolean AddMarkerToMap(int i,GoogleMap map)
    {
        if(i < LoP.size()) {
            map.addMarker(new MarkerOptions()
                    .position(LoP.get(i))
                    .title("Point " + (i)));
            return true;
        }
        else return false;
    }

    /**
     * Adds all marker that are in container
     * @param map GoogleMap map object needed to place marker
     */
    public void AddMarkersToMap(GoogleMap map)
    {
        for(int i = 0; i<LoP.size();i++)
        {
            map.addMarker(new MarkerOptions()
                    .position(LoP.get(i))
                    .title("Point "+i));
        }
    }

    /**
     *
     * @param i number of point  (i=0 -> first point)
     * @return Latitude and longitude of point
     */
    public LatLng GetPoint(int i)
    {
        return LoP.get(i);
    }

    /**
     *
     * @return list of points in route
     */
    List<LatLng> GetPoints() {return LoP;}

}
