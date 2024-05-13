package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


public class DijkstraAstarTest {

    public static Graph graph;
    public static GraphReader reader;
    public static String mapName;

    public static ShortestPathData data;
    public static DijkstraAlgorithm dij;
    public static BellmanFordAlgorithm bel;
    public static ShortestPathSolution solutionDijNormalAll, solutionDijImpossibleAll, solutionDijSameAll;
    public static ShortestPathSolution solutionBelNormalAll, solutionBelImpossibleAll, solutionbelSameAll;

    @BeforeClass
    public static void initAll() throws IOException {
        // Visit these directory to see the list of available files on Commetud.
        mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        // final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();


        // Chemin existant, tout véhicule
        data = new ShortestPathData(graph, graph.getNodes().get(468), graph.getNodes().get(4683), ArcInspectorFactory.getAllFilters().get(0));

        dij = new DijkstraAlgorithm(data);
        bel = new BellmanFordAlgorithm(data);

        solutionDijNormalAll = dij.run();
        solutionBelNormalAll = bel.run();


        // Chemin inexistant, tout véhicule
        data = new ShortestPathData(graph, graph.getNodes().get(22346), graph.getNodes().get(18274), ArcInspectorFactory.getAllFilters().get(0));
        
        dij = new DijkstraAlgorithm(data);
        bel = new BellmanFordAlgorithm(data);

        solutionDijImpossibleAll = dij.run();
        solutionBelImpossibleAll = bel.run();


        // Origine = Destination, tout véhicule
        data = new ShortestPathData(graph, graph.getNodes().get(354), graph.getNodes().get(354), ArcInspectorFactory.getAllFilters().get(0));
        
        dij = new DijkstraAlgorithm(data);
        bel = new BellmanFordAlgorithm(data);

        solutionDijSameAll = dij.run();
        solutionbelSameAll = bel.run();
        
       
    }

    @Test
    public void testImpossible() {
        assertEquals(solutionBelNormalAll.isFeasible(), solutionDijNormalAll.isFeasible());
        assertEquals(solutionBelImpossibleAll.isFeasible(), solutionDijImpossibleAll.isFeasible());
        assertTrue(solutionDijSameAll.isFeasible());
    }


    @Test
    public void testLongueur() {
        Path pathDijNormalAll = solutionDijNormalAll.getPath();
        Path pathDijSameAll = solutionDijSameAll.getPath();

        Path pathBelNormalAll = solutionBelNormalAll.getPath();

        assertEquals(pathBelNormalAll.getLength(), pathDijNormalAll.getLength(), 1e-6);
        assertEquals(0, pathDijSameAll.getLength(), 1e-6);
    }

    @Test
    public void testVitesse() {
        Path pathDijNormalAll = solutionDijNormalAll.getPath();
        Path pathDijSameAll = solutionDijSameAll.getPath();

        Path pathBelNormalAll = solutionBelNormalAll.getPath();

        assertEquals(pathBelNormalAll.getMinimumTravelTime(), pathDijNormalAll.getMinimumTravelTime(), 1e-6);
        assertEquals(0, pathDijSameAll.getMinimumTravelTime(), 1e-6);
    }
}
