package com.kamcioikoalcia.routing.Graph;

import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by Kamil on 23.11.2017.
 * Class that represents graph nodes
 */

public class Node implements Comparable<Node> {
    private String id;
    private double x;
    private double y;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
        getIdValue(x, y);
    }

    private void getIdValue(double x, double y) {
        String xStr = String.valueOf(x);
        String yStr = String.valueOf(y);
        xStr = xStr.replace(".", "");
        yStr = yStr.replace(".", "");
        this.id = xStr + yStr;
    }

    public String getNodeInfo() {
        return "X: " + x + " Y: " + y + " ID: " + id;
    }

    String getNodeId() {
        return id;
    }


    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Node) {
            Node ptr = (Node) v;
            retVal = Objects.equals(ptr.id, this.id);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Node node) {
        if (Objects.equals(this.id, node.getNodeId())) {
            return 0;
        }
        return 1;
    }
}
