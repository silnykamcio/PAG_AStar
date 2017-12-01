package com.geoida.progeo.RouteGenerating.ModelMaker;

import android.os.AsyncTask;
import android.util.Log;

import com.geoida.progeo.Callbacks.OnThreadFinished;
import com.geoida.progeo.OSM.OSMObjects.Node;
import com.geoida.progeo.OSM.OSMObjects.SaveNode;
import com.geoida.progeo.OSM.OSMObjects.SaveWay;
import com.geoida.progeo.OSM.OSMObjects.Way;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.geoida.progeo.RouteGenerating.GeneratingPreferences;
import com.geoida.progeo.Utils.Stoper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Kamil on 14.08.2017.
 * Class where osm objects are converted to grpah object along with proper weights
 */

public class ModelMaker extends AsyncTask<Void, Void, String> {

    private OnThreadFinished listener;
    private HashSet<Node> nodes;
    private HashMap<Long,Way> propWays;
    private HashMap<Long,GraphEdge> edges;
    private HashMap<Long,GraphVertex> vertices;
    private GeneratingPreferences GP;


    public ModelMaker(HashMap<Long,Way> propWays, HashSet<Node> nodes, OnThreadFinished listener, GeneratingPreferences GP){
        this.listener = listener;
        this.propWays = propWays;
        this.nodes = nodes;
        this.GP = GP;
    }

    public HashSet<Node> getNodes(){
        return nodes;
    }

    public HashMap<Long,Way> getPropWays(){
        return propWays;
    }

    public HashMap<Long, GraphEdge> getEdges(){
        return edges;
    }

    public HashMap<Long,GraphVertex> getVertices(){
        return vertices;
    }

    @Override
    protected void onPreExecute()
    {
        //Create a new progress dialog
        //  progressDialog = new ProgressDialog(DataProvider.GetMainActivity());
        //DataProvider.getProgressDialog().setMessage("Analyzing data, please wait...");
    }
    @Override
    protected String doInBackground(Void... voids) {
        makeGraphObjects();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // progressDialog.dismiss();

        listener.OnThreadFinished("ModelMade");
    }

    private void addNodes(){
        Stoper.start("AddingNodes");
        nodes = new HashSet<>();

        for(Way w : propWays.values()){
            nodes.addAll(w.GetCrossNodes());
        }
        Stoper.stop();
    }


    private void makeGraphObjects(){
        Stoper.start("Converting to graph objects");
        GraphEdgeMaker gem = new GraphEdgeMaker();

        edges = gem.convert(propWays,GP);
        System.out.println(edges.size());
        vertices = gem.getVertices();
        Stoper.stop();
    }


}
