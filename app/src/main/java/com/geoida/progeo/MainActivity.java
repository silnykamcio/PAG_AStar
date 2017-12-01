package com.geoida.progeo;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.geoida.progeo.Callbacks.OnThreadFinished;
import com.geoida.progeo.OSM.OSMObjects.Node;
import com.geoida.progeo.OSM.OSMObjects.SaveNode;
import com.geoida.progeo.OSM.OSMObjects.SaveWay;
import com.geoida.progeo.OSM.OSMObjects.Way;
import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.RouteFinder;
import com.geoida.progeo.RouteGenerating.GeneratingPreferences;
import com.geoida.progeo.RouteGenerating.ModelMaker.ModelMaker;
import com.geoida.progeo.RouteGenerating.RouteGenerator;
import com.geoida.progeo.RouteRepresenting.RouteHandler;
import com.geoida.progeo.RouteRepresenting.RouteInfo;
import com.geoida.progeo.RouteRepresenting.RouteLineOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnThreadFinished {

    private GoogleMap mMap;
    private ModelMaker modelMaker;
    private RouteGenerator routeGenerator;
    private GeneratingPreferences generatingPreferences;
    private RouteInfo routeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(52.210901, 20.975014);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);

        InputStream is = getResources().openRawResource(R.raw.test_ways_w);
        InputStream is2 = getResources().openRawResource(R.raw.test_ways_n);
        HashMap<Long, SaveWay> saveWays = new HashMap<>();
        HashSet<SaveNode> saveNodes = new HashSet<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            ObjectInputStream ois2 = new ObjectInputStream(is2);
            saveWays = (HashMap<Long, SaveWay>)ois.readObject();
            saveNodes = (HashSet<SaveNode>)ois2.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        HashMap<Long, Way> ways = new HashMap<>();
        HashSet<Node> nodes = new HashSet<>();
        for(SaveWay w : saveWays.values()) {
            Way propWay = w.getWay();
            ways.put(propWay.getID(),propWay);
        }
        for(SaveNode n : saveNodes){
            nodes.add(n.getNode());
        }

        Log.wtf("test", ""+ways.size());
        Log.wtf("test", ""+nodes.size());
        generatingPreferences = new GeneratingPreferences(50,50,50,new LatLng(52.21766,20.9781),new LatLng(52.204237, 20.975813));
        modelMaker = new ModelMaker(ways,nodes,this,generatingPreferences);
        modelMaker.execute();

    }

    @Override
    public void OnThreadFinished(String id) {
        switch (id){
            case "ModelMade":{
                routeGenerator = new RouteGenerator(modelMaker,this,generatingPreferences);
                routeGenerator.execute();
                break;
            }
            case "RouteComputed":{
               routeInfo = new RouteInfo(routeGenerator.getPathAsWays(0),routeGenerator.getPathAsPositions(0));
                RouteLineOptions opts = new RouteLineOptions(15, Color.BLUE);
                RouteHandler route = new RouteHandler(opts);
                ArrayList<LatLng> path = routeInfo.getPointList();
                route.addRoute(path,0);
                route.drawRoutes(mMap);
                break;
            }
        }
    }
}
