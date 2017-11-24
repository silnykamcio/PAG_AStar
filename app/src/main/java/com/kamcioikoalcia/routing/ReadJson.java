package com.kamcioikoalcia.routing;

import android.util.JsonReader;
import android.util.Log;

import com.kamcioikoalcia.routing.Graph.Edge;
import com.kamcioikoalcia.routing.Graph.Node;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by Alicja on 23.11.2017.
 */

public class ReadJson{

HashSet<Node>nodes;
HashSet<Edge>edges;
    public ReadJson(BufferedReader jsonFile){
        nodes = new HashSet<Node>();
        edges = new HashSet<Edge>();

        StringBuilder total = new StringBuilder(2048);
        String line;
        try {
            while ((line = jsonFile.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String js = total.toString();

        JsonReader jsonReader = new JsonReader(new StringReader(js));
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (Objects.equals(name, "features")) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()){
                        jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String name1 = jsonReader.nextName();

                        if (Objects.equals(name1, "geometry")) {
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String name2 = jsonReader.nextName();

                                if (Objects.equals(name2, "coordinates")) {
                                    Node n=  new Node(0,0);
                                    Node n1 = new Node (0,0);
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()) {
                                        jsonReader.beginArray();
                                        Double x = jsonReader.nextDouble();
                                        Double y = jsonReader.nextDouble();
                                        n = new Node(x,y);
                                        nodes.add(new Node(x,y));
                                        jsonReader.endArray();
                                    }
                                    jsonReader.endArray();
                                    if(nodes.size()%2 == 0){
                                        edges.add(new Edge(n1,n));
                                    }
                                    else
                                    {
                                        n1 = n;
                                    }
                                } else
                                    jsonReader.skipValue();
                            }
                            jsonReader.endObject();
                        } else
                            jsonReader.skipValue();
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                }
                else
                    jsonReader.skipValue();
            }
            jsonReader.endObject();
            jsonReader.close();
            Log.wtf("JsonParser", "Number of edges: " + edges.size());
            Log.wtf("JsonParser", "Number of nodes: " + nodes.size());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
