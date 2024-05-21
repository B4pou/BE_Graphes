package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class LabelStar extends Label {
    private double distDest;


    public LabelStar(Node sommetCourant, Node destination, double distDest) {
        super(sommetCourant);
        this.distDest = distDest;
    }


    public double getDistDest() {
        return this.distDest;
    }
    
    public double getCost() {
        return this.coutRealise + this.distDest;
    }

    
}
