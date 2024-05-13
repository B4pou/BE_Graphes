package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class LabelStar extends Label {
    private double distDest;


    public LabelStar(Node sommetCourant, Node destination) {
        super(sommetCourant);
        this.distDest = Point.distance(sommetCourant.getPoint(), destination.getPoint());
    }


    public double getDistDest() {
        return this.distDest;
    }
    
    public double getCost() {
        return this.coutRealise + this.distDest;
    }

    
}
