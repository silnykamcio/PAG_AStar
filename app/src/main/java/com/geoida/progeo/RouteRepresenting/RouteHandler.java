package com.geoida.progeo.RouteRepresenting;

import java.util.ArrayList;
import java.util.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



/**
 * Class for showing the routes
 * Created by Konrad on 2017-03-14.
 */

public class RouteHandler {
    /**
     * This class represents route
     */
    private class Route {
        private PointHandler points;
        private RouteLineOptions options;
        private int routeId;
        private String polyId;

        /*
         Route(List<HashMap<String, String>> routeData, RouteHandler.RouteLineOptions opts) {
         this.options = opts);
         for (int i = 0; i < routeData.size(); ++i) {
         HashMap<String, String> point = routeData.get(i);
         double lat = Double.parseDouble(point.get("lat"));
         double lng = Double.parseDouble(point.get("lng"));
         points.AddPoint(new LatLng(lat, lng));
         }
         }
         */

        /**
         *
         * @param pnts list of pints
         * @param opts route options
         */
        Route(ArrayList<LatLng> pnts, RouteLineOptions opts, int id) {
            this.options = opts;
            this.routeId = id;
            points = new PointHandler();
            for (LatLng point : pnts) {
                this.points.AddPoint(point);
            }

        }

        void draw(GoogleMap map) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.addAll(points.GetPoints());
            polyOptions.width(this.options.width);
            polyOptions.color(this.options.color);
            polyOptions.clickable(true);
            Polyline poly = map.addPolyline(polyOptions);
            polyId = poly.getId();
            poly.setClickable(true);
            System.out.println("drawed route: id: " + routeId + " polyid: " + polyId);
        }

        int getRouteID(){return routeId;}
        String getPolyID() { return polyId;}
    }

    private ArrayList<Route> routes;
    private RouteLineOptions singular_options;

    /*
      Constructor
      @param paths list of paths
     * @param optionList list of route style options
     * @throws Exception when number of paths and options is different
     */

    /*
    public RouteHandler(List<List<HashMap<String, String>>> paths, List<RouteLineOptions> optionList) throws Exception {
        if (paths.size() != optionList.size()) {
            throw new Exception("Error. Invalid number of options");
        }

        for (int i = 0; i < paths.size(); ++i) {
            routes.add(new Route(paths.get(i), optionList.get(i));
        }
    }
    */

    /*
     * This constructor is should be used if all routes have same style options
     * @param paths
     * @param option
     */

    /*
    public RouteHandler(List<List<HashMap<String, String>>> paths, RouteLineOptions option) {
        for (int i = 0; i < paths.size(); ++i) {
            routes.add(new Route(paths.get(i), option));
        }
    }
    */


    /**
     * Init only option (routes later)
     * @param option option to be applied to routes added in future
     */
     public RouteHandler(RouteLineOptions option) {
        routes = new ArrayList<>();
        singular_options = option;
    }

    /**
     * Basic initalization
     */
    RouteHandler(){
        routes = new ArrayList<>();
    }

    void changeOptions(RouteLineOptions options){
        singular_options = options;
    }

    /**
     * addRoute to class
     * @param route route to be added
     */
    public void addRoute(ArrayList<LatLng> route, int id) {
        routes.add(new Route(route,singular_options, id));
    }

    int getRouteID(String polyLineID){
        for(Route r : routes){
            if(Objects.equals(r.getPolyID(), polyLineID)){
                return r.getRouteID();
            }
        }
        return -1;
    }

    void resetRoutes(){
        routes.clear();
    }

    /**
     * Draw routes on map
     * @param map map, where routes should be drawn
     */

    public void drawRoutes(GoogleMap map) {
        for (Route route : routes) {
            route.draw(map);
        }
    }
}
