package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.model.*;

import org.insa.graphs.algorithm.AbstractSolution;

import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm { 

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        Label[] labelArray = new Label[data.getGraph().size()];  // Le label à l'indice i est associé au noeud n°i

        for (Node node : data.getGraph().getNodes()) {  // Initialisation du tableau
            labelArray[node.getId()] = initLabel(node, data.getDestination());
        }
        
        Label originLabel = labelArray[data.getOrigin().getId()];
        originLabel.setCoutRealise(0);  // Coût de l'origine mis à 0

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(originLabel);

        while(! heap.isEmpty()) {
            Label currentLabel = heap.deleteMin();
            //Label currentLabel = heap.findMin();
            //heap.remove(currentLabel);
            currentLabel.setMarque();
            Node currentNode = currentLabel.getSommetCourant();

            if (currentLabel == originLabel) {
                this.notifyOriginProcessed(currentNode);
            } else if (currentNode == data.getDestination()) {
                break;
            }
            
            this.notifyNodeMarked(currentNode);
            

            List<Arc> successor = currentNode.getSuccessors();

            for (Arc arc : successor) {
                if (! data.isAllowed(arc)) {
                    continue;
                }

                if (arc.getDestination() == data.getDestination()) {
                    this.notifyDestinationReached(currentNode);
                }
                
                this.notifyNodeReached(arc.getDestination());
                
                Label successorLabel = labelArray[arc.getDestination().getId()];

                double cost = data.getCost(arc) + currentLabel.getCoutRealise();
                if (cost < successorLabel.getCoutRealise()) {  // Si on trouve un meilleur chemin
                    if (successorLabel.getMarque()) {
                        throw new RuntimeException("Sommet déjà marqué !!");
                    }

                    if (successorLabel.getCoutRealise() < Double.MAX_VALUE) {
                        //System.out.println("suppression element, taille : "+ heap);
                        heap.remove(successorLabel);
                    } 
                    
                    successorLabel.setArcPere(arc);
                    successorLabel.setCoutRealise(cost);

                    
                    heap.insert(successorLabel);
                    //System.out.println("insertion element, taille : "+ heap);
                }
            }
        }

        Label destinationLabel = labelArray[data.getDestination().getId()];
        
        if (! destinationLabel.getMarque()) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            Path path = new Path(data.getGraph(), findPath(destinationLabel, labelArray));
            if (path.isValid()) {
                solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, path);
            }

            
        }

        return solution;
    }


    private List<Arc> findPath (Label destinationLabel, Label[] labelArray) {
        List<Arc> arcs = new ArrayList<>();
        Label currentLabel = destinationLabel;

        while (currentLabel.getArcPere() != null) {
            arcs.add(currentLabel.getArcPere());
            currentLabel = labelArray[currentLabel.getArcPere().getOrigin().getId()];
        }

        Collections.reverse(arcs);
        return arcs;
    }


    protected Label initLabel(Node sommetCourant, Node destination) {
        return new Label(sommetCourant);
    }
}

