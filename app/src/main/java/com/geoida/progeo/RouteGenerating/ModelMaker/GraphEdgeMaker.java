package com.geoida.progeo.RouteGenerating.ModelMaker;

import com.geoida.progeo.OSM.OSMObjects.Node;
import com.geoida.progeo.OSM.OSMObjects.Way;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphEdge;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;
import com.geoida.progeo.RouteGenerating.GeneratingPreferences;

import java.util.HashMap;


/**
 * Created by Kamil on 20.07.2017.
 * Class for making an edges of graph from ways
 */

class GraphEdgeMaker {
    GraphEdgeMaker(){}
    private HashMap<Long,GraphVertex> vertexes;

    /**
     * @param ways Map of all ways obtained from OSM
     * @param gp Generating preferences
     * @return Map of ways in shape of graph edges that can be used for graph algorithms
     */
    HashMap<Long,GraphEdge> convert(HashMap<Long,Way> ways, GeneratingPreferences gp){
        HashMap<Long,GraphEdge> edges = new HashMap<>();
        vertexes = new HashMap<>();
        for(Way w: ways.values()){
            Node src = w.GetCrossNodes().get(0);
            Node dest = w.GetCrossNodes().get(w.GetCrossNodes().size()-1);
            GraphVertex source = new GraphVertexMaker().convert(src);
            GraphVertex destination = new GraphVertexMaker().convert(dest);
            if(!vertexes.containsKey(src.getID()))
                vertexes.put(src.getID(),source);
            if(!vertexes.containsKey(dest.getID()))
                vertexes.put(dest.getID(),destination);
            vertexes.get(src.getID()).addEdgeId(w.getID());
            vertexes.get(dest.getID()).addEdgeId(w.getID());
            vertexes.get(src.getID()).addNeighbour(vertexes.get(dest.getID()));
            vertexes.get(dest.getID()).addNeighbour(vertexes.get(src.getID()));
        }
        for(Way w : ways.values()){
            Node src = w.GetCrossNodes().get(0);
            Node dest = w.GetCrossNodes().get(w.GetCrossNodes().size()-1);
            edges.put(w.getID(),new GraphEdge(Long.toString(w.getID()),vertexes.get(src.getID()),vertexes.get(dest.getID()),(int)new WeightsMaker().convert(w,gp),w.getDistance(), w.getGreenLevel(), w.getCrossings(), w.getQuality()));
        }
        return edges;
    }

    HashMap<Long,GraphVertex> getVertices(){
        return vertexes;
    }

}
