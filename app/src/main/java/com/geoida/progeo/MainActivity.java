package com.geoida.progeo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnThreadFinished {

    private ModelMaker modelMaker;
    private RouteGenerator routeGenerator;
    private GeneratingPreferences generatingPreferences;
    private RouteInfo routeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng position = new LatLng(52.210901, 20.975014);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

        InputStream is = getResources().openRawResource(R.raw.test_ways_w);
        InputStream is2 = getResources().openRawResource(R.raw.test_ways_n);
        HashMap<Long, SaveWay> saveWays = new HashMap<>();
        HashSet<SaveNode> saveNodes = new HashSet<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            ObjectInputStream ois2 = new ObjectInputStream(is2);
            Object a = ois.readObject();
            Object b = ois2.readObject();
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
        generatingPreferences = new GeneratingPreferences(50,50,50,new LatLng(52.258210, 21.021865),new LatLng(52.214484, 20.979801));
        modelMaker = new ModelMaker(ways,nodes,this,generatingPreferences);
        modelMaker.execute();

    }

    public void showRouteInformation(GoogleMap map, TextView distance, TextView slope,
                                     ProgressBar routeType, ProgressBar crossRoads, ProgressBar greenLevel){
        Pair<LatLng, LatLng> bbox = routeInfo.boundingBox();
        //map.addMarker(new MarkerOptions().position(routeCenter));

        // Setting route in the center of map
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng((bbox.second.latitude - bbox.first.latitude) / 2 + bbox.first.latitude,
                (bbox.second.longitude - bbox.first.longitude) / 2 + bbox.first.longitude));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14.0F);

        map.moveCamera(center);
        map.animateCamera(zoom);

        map.setMinZoomPreference(11F);

        RouteLineOptions opts = new RouteLineOptions(12, getResources().getColor(R.color.colorPrimaryDark));
        RouteHandler route = new RouteHandler(opts);
        ArrayList<LatLng> path = routeInfo.getPointList();
        route.addRoute(path,0);
        route.drawRoutes(map);

        DecimalFormat prec = new DecimalFormat("0.0");

            slope.setText("-");

        distance.setText(prec.format(Math.round(routeInfo.getLength() / 100.00)/10.00));
        barInitializer(greenLevel, (int)routeInfo.getGreenEnviroLvl(), 100, false);
        barInitializer(routeType, (int)routeInfo.getGoodWaysLvl(), 100, false);
        barInitializer(crossRoads, (int)(routeInfo.getCrossings()/(Math.round(routeInfo.getLength() / 100.00)/10.00)*10), 100, true);
    }

    public Bitmap createMapMarker(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private void barInitializer(ProgressBar bar, int value, int max, boolean inversed) {
        bar.setMax(max);
        Drawable barDraw = bar.getProgressDrawable();

        int color;
        if (value < max/6)
            color = inversed ? R.color.level_1 : R.color.level_6;
        else if (value < max/6 * 2)
            color = inversed ? R.color.level_2 : R.color.level_5;
        else if (value < max/6 * 3)
            color = inversed ? R.color.level_3 : R.color.level_4;
        else if (value < max/6 * 4)
            color = inversed ? R.color.level_4 : R.color.level_3;
        else if (value < max/6 * 5)
            color = inversed ? R.color.level_5 : R.color.level_2;
        else
            color = inversed ? R.color.level_6 : R.color.level_1;

        barDraw.setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_IN);
        bar.setProgressDrawable(barDraw);
        bar.setProgress(value);

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
                Fragment SumFrag = new FragmentSummary();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.map,SumFrag,"Summary");
                transaction.addToBackStack(SumFrag.getTag());
                transaction.commit();

                break;
            }
        }
    }
}
