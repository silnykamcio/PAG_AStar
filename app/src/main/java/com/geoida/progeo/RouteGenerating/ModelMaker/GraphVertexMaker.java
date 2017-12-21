package com.geoida.progeo.RouteGenerating.ModelMaker;

import com.geoida.progeo.OSM.OSMObjects.Node;
import com.geoida.progeo.RouteGenerating.Dijkstra.GraphVertex;

/**
 * Created by Kamil on 20.07.2017.
 * Class for making graph vertexes from nodes
 */

class GraphVertexMaker {
    GraphVertexMaker(){}

    GraphVertex convert(Node n){
        return new GraphVertex(n.getID(), n.getCoords());
    }
}
