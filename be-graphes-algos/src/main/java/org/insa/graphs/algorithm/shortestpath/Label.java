package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {
    protected Node sommetCourant;
    protected boolean marque;
    protected double coutRealise;
    protected Arc arcPere;


    public Label(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
        this.marque = false;
        this.coutRealise = Double.MAX_VALUE;
        this.arcPere = null;
    }


    public Node getSommetCourant() {
        return this.sommetCourant;
    }
    
    public boolean getMarque() {
        return this.marque;
    }

    public void setMarque() {
        this.marque = true;
    }


    public double getCoutRealise() {
        return this.coutRealise;
    }

    public void setCoutRealise(double coutRealise) {
        this.coutRealise = coutRealise;
    }


    public Arc getArcPere() {
        return this.arcPere;
    }

    public void setArcPere(Arc arcPere) {
        this.arcPere = arcPere;
    }


    public Node getOrigin() {
        return this.arcPere.getOrigin();
    }

    public double getCost() {
        return this.coutRealise;
    }

    public int compareTo(Label label) {
        double difference = this.getCost() - label.getCost();
        if (difference < 0) {
            return -1;

        } else if (difference > 0) {
            return 1;

        } else {
            difference = this.getCoutRealise() - label.getCoutRealise();
            if (difference < 0) {
                return 1;
    
            } else if (difference > 0) {
                return -1;

            } else {
                return 0;
            }
        }
    }
}
