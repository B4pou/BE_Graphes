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

    public static ShortestPathData dataNormal, dataImpossible;
    public static DijkstraAlgorithm dijNormal;
    public static BellmanFordAlgorithm belNormal;
    public static ShortestPathSolution solutionDij;
    public static ShortestPathSolution solutionBel;

    @BeforeClass
    public static void initAll() throws IOException {
        // Visit these directory to see the list of available files on Commetud.
        mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        // final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();


        dataNormal = new ShortestPathData(graph, graph.getNodes().get(468), graph.getNodes().get(4683), ArcInspectorFactory.getAllFilters().get(0));  // Chemin existant, tout véhicule
        dataImpossible = new ShortestPathData(graph, graph.getNodes().get(22346), graph.getNodes().get(18274), ArcInspectorFactory.getAllFilters().get(0));  // Chemin inexistant, tout véhicule
        
        dijNormal = new DijkstraAlgorithm(dataNormal);
        belNormal = new BellmanFordAlgorithm(dataNormal);

        solutionDij = dijNormal.run();
        solutionBel = belNormal.run();
       
    }


    @Test
    public void testLongueur() {
        Path pathDij = solutionDij.getPath();
        Path pathBel = solutionBel.getPath();

        assertEquals(pathBel.getLength(), pathDij.getLength(), 1e-6);
    }

    @Test
    public void testVitesse() {
        Path pathDij = solutionDij.getPath();
        Path pathBel = solutionBel.getPath();

        assertEquals(pathBel.getMinimumTravelTime(), pathDij.getMinimumTravelTime(), 1e-6);
    }
}
