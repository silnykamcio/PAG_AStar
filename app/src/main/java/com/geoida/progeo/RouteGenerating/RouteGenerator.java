package com.geoida.progeo.RouteGenerating;


import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.geoida.progeo.Callbacks.OnThreadFinished;
import com.geoida.progeo.OSM.OSMObjects.Node;
import com.geoida.progeo.OSM.OSMObjects.Way;
import com.geoida.progeo.RouteGenerating.AStar.AStarAlgorithm;
import com.geoida.progeo.RouteGenerating.Dijkstra.Graph;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.geoida.progeo.RouteGenerating.Dijkstra.RouteFinder;
import com.geoida.progeo.RouteGenerating.ModelMaker.ModelMaker;
import com.geoida.progeo.Utils.Stoper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Kamil on 2017-08-04. Class where all route generating computations will be held
 */

public class RouteGenerator extends AsyncTask<Void, Void, String> {

    private OnThreadFinished listener;
    private ArrayList<ArrayList<GraphVertex>> paths;
    private HashSet<Node> nodes;
    private HashMap<Long,Way> propWays;
    private HashMap<Long,GraphEdge> edges;
    private HashMap<Long,GraphVertex> vertices;
    private GeneratingPreferences GP;

    public ArrayList<ArrayList<GraphVertex>> getPaths(){return paths;}

    public ArrayList<GraphVertex> getPath(int num){return paths.get(num);}

    public int getPathsCount(){
            return paths.size();
    }

    public boolean isPathsGenerated(){
        return paths != null;
    }

    /**
     * @return List of LatLng points obtained from generated path
     */
    public ArrayList<LatLng> getPathAsPositions(int num) {
        ArrayList<LatLng> pos = new ArrayList<>();
        ArrayList<GraphVertex> path = paths.get(num);
        for(int i= 0; i<path.size()-1;i++) {
            GraphVertex a = path.get(i);
            GraphVertex b = path.get(i+1);
            HashSet<Long> aIds = new HashSet<>(a.getEdgeIds());
            HashSet<Long> bIds = new HashSet<>(b.getEdgeIds());
            aIds.retainAll(bIds);
            Iterator<Long> iter = aIds.iterator();
                if(iter.hasNext()){
                Way way = propWays.get(iter.next());
                ArrayList<LatLng> wayPos = new ArrayList<>();
                for (Node n : way.GetNodes()) {
                    wayPos.add(n.getCoords());
                }

                ArrayList<Node> cNodes = new ArrayList<>(way.GetCrossNodes());
                if (Objects.equals(cNodes.get(0).getID(), b.getId())) {
                    Collections.reverse(wayPos);
                }
                pos.addAll(wayPos);
            }
        }
        return pos;
    }

    public ArrayList<Way> getPathAsWays(int num){
        ArrayList<Way> ways = new ArrayList<>();
        ArrayList<GraphVertex> path = paths.get(num);
        for(int i = 0; i<path.size()-1;i++){
            GraphVertex a = path.get(i);
            GraphVertex b = path.get(i+1);
            HashSet<Long> aIds = new HashSet<>(a.getEdgeIds());
            HashSet<Long> bIds = new HashSet<>(b.getEdgeIds());
            aIds.retainAll(bIds);
            Iterator<Long> iter = aIds.iterator();
            if(iter.hasNext()) {
                Way way = propWays.get(iter.next());
                ways.add(way);
            }
        }
        return ways;
    }

    public HashMap<Long,Node> getNodes() {
        HashMap<Long,Node> map = new HashMap<>();
        for(Node n : nodes){
            map.put(n.getID(),n);
        }
        return map;
    }

    public RouteGenerator(ModelMaker MM, OnThreadFinished listener, GeneratingPreferences GP){
        this.listener = listener;
        this.GP = GP;
        this.nodes = MM.getNodes();
        this.propWays = MM.getPropWays();
        this.edges = MM.getEdges();
        this.vertices = MM.getVertices();
        System.out.println(this.nodes.size() + ", " + this.propWays.size() + ", " + this.edges.size() + ", " + this.vertices.size());
    }

    @Override
    protected void onPreExecute()
    {
        //DataProvider.getProgressDialog().setMessage("Generating best route, please wait...");
    }

    @Override
    protected String doInBackground(Void... params) {
            generateRoutesAutomatic();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.OnThreadFinished("RouteComputed");
    }

    private void generateRoutesAutomatic(){
        Stoper.start("Graph creating");
        Graph g = new Graph(vertices,edges);
        Stoper.stop();
        Log.wtf("RouteGenerating", "Graph elements created");
        Stoper.start("RouteFinder");
        //RouteFinder rf = new RouteFinder(g);
        Log.wtf("RouteGenerating", "RouteFinder initialized");
        AStarAlgorithm aStar = new AStarAlgorithm(g);
        paths = new ArrayList<>();
        paths.add(aStar.compute(vertices.get(getClosest(nodes,GP.getStartPoint()).getID()),vertices.get(getClosest(nodes,GP.getEndPoint()).getID())));
        //rf.compute(vertices.get(getClosest(nodes,GP.getStartPoint()).getID()));
        Stoper.stop();
        Log.wtf("RouteGenerating", "RouteFinder computed");
        Stoper.start("RouteSearching");
       // paths = null;
        //paths = new ArrayList<>();
        //paths.add(rf.getPath(vertices.get(getClosest(nodes,GP.getEndPoint()).getID())));
        Log.wtf("path", ""+paths.size());
        Stoper.stop();
    }

    /**
     * @param nodes Set of all nodes
     * @param pnt Position choosed by user
     * @return Node that is closest to position choosed by user (in otder to start computation process from this point)
     */
    private Node getClosest(HashSet<Node> nodes,LatLng pnt){
        Location a = new Location("a");
        a.setLatitude(pnt.latitude);
        a.setLongitude(pnt.longitude);
        double minDist = Double.MAX_VALUE;
        Node closest = null;
        for(Node n : nodes){
            LatLng pos = n.getCoords();
            Location b = new Location("b");
            b.setLatitude(pos.latitude);
            b.setLongitude(pos.longitude);
            double dist = a.distanceTo(b);
            if(dist < minDist) {
                closest = n;
                minDist = dist;
            }
        }
        return closest;
    }
}
