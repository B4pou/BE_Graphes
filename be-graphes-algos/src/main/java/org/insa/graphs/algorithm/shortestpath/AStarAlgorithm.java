package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution;

import org.insa.graphs.algorithm.utils.BinaryHeap;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label initLabel(Node sommetCourant, Node destination, Mode mode, int maxSpeed) {
        double distDest;
        if (mode == Mode.LENGTH) {
            distDest = Point.distance(sommetCourant.getPoint(), destination.getPoint());
        } else {
            distDest = Point.distance(sommetCourant.getPoint(), destination.getPoint())/maxSpeed;
        }
        
        return new LabelStar(sommetCourant, destination, distDest);
    }

}


