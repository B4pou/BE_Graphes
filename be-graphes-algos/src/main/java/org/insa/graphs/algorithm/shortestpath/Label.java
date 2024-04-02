package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.model.*;

public class Label {
    private Node sommetCourant;
    private boolean marque;
    private double coutRealise;
    private Arc arcPere;

    static ArrayList<Label> tab;


    public Node getSommetCourant() {
        return this.sommetCourant;
    }
    
    public boolean getmarque() {
        return this.marque;
    }

    public double getCoutRealise() {
        return this.coutRealise;
    }

    public Arc getArcPere() {
        return this.arcPere;
    }

    public Node getOrigin() {
        return this.arcPere.getOrigin();
    }

    public double getCost() {
        return this.getCoutRealise();
    }
}
