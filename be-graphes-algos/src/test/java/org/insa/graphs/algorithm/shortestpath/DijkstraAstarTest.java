package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

import org.junit.BeforeClass;
import org.junit.Test;


public class DijkstraAstarTest {

    public static Graph graph;
    public static GraphReader reader;
    public static String mapName;

    private static List<TestCase> testCasesNormal, testCasesImpossible, testCasesSame;
    @BeforeClass
    public static void initAll() throws IOException {
        // Visit these directory to see the list of available files on Commetud.
        mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        // final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();

        int[] filters = {0, 1, 2}; // Indices des filtres à tester
        // Test de trois filtres (all, cars, bicycle) pour chaque
        testCasesNormal = createTestCases(468, 4683, filters);  // Chemin existant
        testCasesImpossible = createTestCases(22346, 18274, filters);  // Chemin inexistant
        testCasesSame = createTestCases(354, 354, filters);  // Origine = Destination

    }


    private static List<TestCase> createTestCases(int origin, int destination, int[] filterIndices) {
        List<TestCase> testCases = new ArrayList<>();
        for (int filterIndex : filterIndices) {
            testCases.add(new TestCase(origin, destination, filterIndex));
        }
        return testCases;
    }


    @Test
    public void testImpossible() {
        for (TestCase testCase : testCasesNormal) {
            compareFeasibility(testCase);
        }
        for (TestCase testCase : testCasesImpossible) {
            compareFeasibility(testCase);
        }
        for (TestCase testCase : testCasesSame) {
            assertTrue(testCase.dijkstraSolution.isFeasible());
            assertTrue(testCase.astarSolution.isFeasible());
        }
    }

    @Test
    public void testLongueur() {
        for (TestCase testCase : testCasesNormal) {
            comparePathLengths(testCase);
        }
        for (TestCase testCase : testCasesSame) {
            assertEquals(0, testCase.dijkstraSolution.getPath().getLength(), 1e-6);
            assertEquals(0, testCase.astarSolution.getPath().getLength(), 1e-6);
        }
    }

    @Test
    public void testVitesse() {
        for (TestCase testCase : testCasesNormal) {
            compareTravelTimes(testCase);
        }
        for (TestCase testCase : testCasesSame) {
            assertEquals(0, testCase.dijkstraSolution.getPath().getMinimumTravelTime(), 1e-6);
            assertEquals(0, testCase.astarSolution.getPath().getMinimumTravelTime(), 1e-6);
        }
    }



    private void compareFeasibility(TestCase testCase) {
        assertEquals(testCase.bellmanFordSolution.isFeasible(), testCase.dijkstraSolution.isFeasible());
        assertEquals(testCase.bellmanFordSolution.isFeasible(), testCase.astarSolution.isFeasible());
    }

    private void comparePathLengths(TestCase testCase) {
        assertEquals(testCase.bellmanFordSolution.getPath().getLength(), testCase.dijkstraSolution.getPath().getLength(), 1e-6);
        assertEquals(testCase.bellmanFordSolution.getPath().getLength(), testCase.astarSolution.getPath().getLength(), 1e-6);
    }

    private void compareTravelTimes(TestCase testCase) {
        assertEquals(testCase.bellmanFordSolution.getPath().getMinimumTravelTime(), testCase.dijkstraSolution.getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(testCase.bellmanFordSolution.getPath().getMinimumTravelTime(), testCase.astarSolution.getPath().getMinimumTravelTime(), 1e-6);
    }



    private static class TestCase {
        ShortestPathSolution dijkstraSolution;
        ShortestPathSolution bellmanFordSolution;
        ShortestPathSolution astarSolution;

        TestCase(int origin, int destination, int filterIndex) {
            this.dijkstraSolution = createSolution(origin, destination, filterIndex, DijkstraAlgorithm::new);
            this.bellmanFordSolution = createSolution(origin, destination, filterIndex, BellmanFordAlgorithm::new);
            this.astarSolution = createSolution(origin, destination, filterIndex, AStarAlgorithm::new);
        }

        private ShortestPathSolution createSolution(int origin, int destination, int filterIndex, Function<ShortestPathData, ShortestPathAlgorithm> algorithmFactory) {
            ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), ArcInspectorFactory.getAllFilters().get(filterIndex));
            ShortestPathAlgorithm algorithm = algorithmFactory.apply(data);
            return algorithm.run();
        }
    }
}
