package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.model.*;

import org.insa.graphs.algorithm.AbstractSolution;

import org.insa.graphs.algorithm.utils.BinaryHeap;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label initLabel(Node sommetCourant, Node destination) {
        return new LabelStar(sommetCourant, destination);
    }

}


