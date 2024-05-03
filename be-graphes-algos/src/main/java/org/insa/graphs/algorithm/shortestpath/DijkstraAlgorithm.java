package org.insa.graphs.algorithm.shortestpath;

import java.util.*;

import org.insa.graphs.model.*;

import org.insa.graphs.algorithm.AbstractSolution;

import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    static ArrayList<Label> labelArray = new ArrayList<Label>();  // Le label à l'indice i est associé au noeud n°i

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        for (Node node : data.getGraph().getNodes()) {  // Initialisation du tableau
            labelArray.add(node.getId(), new Label(node));
        }
        
        Label originLabel = labelArray.get(data.getOrigin().getId());
        originLabel.setCoutRealise(0);  // Coût de l'origine mis à 0

        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(originLabel);

        while(! heap.isEmpty()) {
            //Label currentLabel = heap.deleteMin();
            Label currentLabel = heap.findMin();
            heap.remove(currentLabel);
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
                if (! data.isAllowed(arc)) continue;

                if (arc.getDestination() == data.getDestination()) {
                    this.notifyDestinationReached(currentNode);
                }
                
                this.notifyNodeReached(arc.getDestination());
                
                Label successorLabel = labelArray.get(arc.getDestination().getId());

                double cost = data.getCost(arc) + currentLabel.getCoutRealise();
                if (cost < successorLabel.getCoutRealise()) {  // Si on trouve un meilleur chemin
                    successorLabel.setArcPere(arc);
                    successorLabel.setCoutRealise(cost);
                    try {
                        heap.remove(successorLabel);
                    } catch (Exception e) {

                    }
                    heap.insert(successorLabel);
                }
            }
        }

        Label destinationLabel = labelArray.get(data.getDestination().getId());
        
        if (! destinationLabel.getMarque()) {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
        } else {
            solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(data.getGraph(), findPath(destinationLabel)));
        }

        return solution;
    }


    private List<Arc> findPath (Label destinationLabel) {
        List<Arc> arcs = new ArrayList<>();
        Label currentLabel = destinationLabel;

        while (currentLabel.getArcPere() != null) {
            arcs.add(currentLabel.getArcPere());
            currentLabel = labelArray.get(currentLabel.getArcPere().getOrigin().getId());
        }

        Collections.reverse(arcs);
        return arcs;
    }


}
