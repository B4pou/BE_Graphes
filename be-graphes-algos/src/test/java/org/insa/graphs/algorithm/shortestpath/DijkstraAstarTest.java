package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Function;

import org.insa.graphs.algorithm.ArcInspector;
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

    private static TestCase testCaseNormal, testCaseImpossible, testCaseSame;
    @BeforeClass
    public static void initAll() throws IOException {
        // Visit these directory to see the list of available files on Commetud.
        mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr";
        // final String pathName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        // Create a graph reader.
        reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        // Read the graph.
        graph = reader.read();


        // Test de trois filtres (all, cars, bicycle) pour chaque
        testCaseNormal = new TestCase(468, 4683);  // Chemin existant
        testCaseImpossible = new TestCase(22346, 18274);  // Chemin inexistant
        testCaseSame = new TestCase(354, 354);  // Origine = Destination

    }

    @Test
    public void testImpossible() {
        feasibilityDijkstra(testCaseNormal, true);
        feasibilityDijkstra(testCaseImpossible, false);
        feasibilityDijkstra(testCaseSame, true);

        feasibilityAStar(testCaseNormal, true);
        feasibilityAStar(testCaseImpossible, false);
        feasibilityAStar(testCaseSame, true);
    }

    @Test
    public void testLongueur() {
        compareLengthsDijkstra(testCaseNormal);
        // compareLengthsDijkstra(testCaseImpossible);
        // compareLengthsDijkstra(testCaseSame);

        compareLengthsAStar(testCaseNormal);
        // compareLengthsAStar(testCaseImpossible);
        // compareLengthsAStar(testCaseSame);
    }

    @Test
    public void testVitesse() {
        compareTravelTimesDijkstra(testCaseNormal);
        // compareTravelTimesDijkstra(testCaseImpossible);
        // compareTravelTimesDijkstra(testCaseSame);

        compareTravelTimesAStar(testCaseNormal);
        // compareTravelTimesAStar(testCaseImpossible);
        // compareTravelTimesAStar(testCaseSame);
    }



    private void feasibilityDijkstra(TestCase testCase, boolean isFeasible) {
        assertEquals(isFeasible, testCase.dijkstraAll.isFeasible());
        assertEquals(isFeasible, testCase.dijkstraCars.isFeasible());
        assertEquals(isFeasible, testCase.dijkstraPedest.isFeasible());
    }

    private void feasibilityAStar(TestCase testCase, boolean isFeasible) {
        assertEquals(isFeasible, testCase.aStarAll.isFeasible());
        assertEquals(isFeasible, testCase.aStarCars.isFeasible());
        assertEquals(isFeasible, testCase.aStarPedest.isFeasible());
    } 


    private void compareLengthsDijkstra(TestCase testCase) {
        assertEquals(testCase.bellmanFordAll.getPath().getLength(), testCase.dijkstraAll.getPath().getLength(), 1e-6);
        assertEquals(testCase.bellmanFordCars.getPath().getLength(), testCase.dijkstraCars.getPath().getLength(), 1e-6);
        assertEquals(testCase.bellmanFordPedest.getPath().getLength(), testCase.dijkstraPedest.getPath().getLength(), 1e-6);
    }

    private void compareLengthsAStar(TestCase testCase) {
        assertEquals(testCase.bellmanFordAll.getPath().getLength(), testCase.aStarAll.getPath().getLength(), 1e-6);
        assertEquals(testCase.bellmanFordCars.getPath().getLength(), testCase.aStarCars.getPath().getLength(), 1e-6);
        assertEquals(testCase.bellmanFordPedest.getPath().getLength(), testCase.aStarPedest.getPath().getLength(), 1e-6);
    }


    private void compareTravelTimesDijkstra(TestCase testCase) {
        double bellmanAll = testCase.bellmanFordAll.getPath().getMinimumTravelTime();
        double bellmanCars = testCase.bellmanFordCars.getPath().getMinimumTravelTime();
        double bellmanPedest = testCase.bellmanFordPedest.getPath().getMinimumTravelTime();

        assertEquals(bellmanAll, testCase.dijkstraAll.getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(bellmanCars, testCase.dijkstraCars.getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(bellmanPedest, testCase.dijkstraPedest.getPath().getMinimumTravelTime(), 1e-6);
    }

    private void compareTravelTimesAStar(TestCase testCase) {
        double bellmanAll = testCase.bellmanFordAll.getPath().getMinimumTravelTime();
        double bellmanCars = testCase.bellmanFordCars.getPath().getMinimumTravelTime();
        double bellmanPedest = testCase.bellmanFordPedest.getPath().getMinimumTravelTime();

        assertEquals(bellmanAll, testCase.aStarAll.getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(bellmanCars, testCase.aStarCars.getPath().getMinimumTravelTime(), 1e-6);
        assertEquals(bellmanPedest, testCase.aStarPedest.getPath().getMinimumTravelTime(), 1e-6);
    }



    private static class TestCase {
        ShortestPathSolution aStarAll, aStarCars, aStarPedest;
        ShortestPathSolution dijkstraAll, dijkstraCars, dijkstraPedest;
        ShortestPathSolution bellmanFordAll, bellmanFordCars, bellmanFordPedest;


        TestCase(int origin, int destination) {
            this.dijkstraAll = getShortestPathSolution(origin, destination, ArcInspectorFactory.allArcsL, DijkstraAlgorithm::new);
            this.dijkstraCars = getShortestPathSolution(origin, destination, ArcInspectorFactory.forCarsL, DijkstraAlgorithm::new);
            this.dijkstraPedest = getShortestPathSolution(origin, destination, ArcInspectorFactory.forBicyclesL, DijkstraAlgorithm::new);

            this.bellmanFordAll = getShortestPathSolution(origin, destination, ArcInspectorFactory.allArcsL, BellmanFordAlgorithm::new);
            this.bellmanFordCars = getShortestPathSolution(origin, destination, ArcInspectorFactory.forCarsL, BellmanFordAlgorithm::new);
            this.bellmanFordPedest = getShortestPathSolution(origin, destination, ArcInspectorFactory.forBicyclesL, BellmanFordAlgorithm::new);

            this.aStarAll = getShortestPathSolution(origin, destination, ArcInspectorFactory.allArcsL, AStarAlgorithm::new);
            this.aStarCars = getShortestPathSolution(origin, destination, ArcInspectorFactory.forCarsL, AStarAlgorithm::new);
            this.aStarPedest = getShortestPathSolution(origin, destination, ArcInspectorFactory.forBicyclesL, AStarAlgorithm::new);
        }


        private static ShortestPathSolution getShortestPathSolution(int origin, int destination, ArcInspector filter, Function<ShortestPathData, ShortestPathAlgorithm> constructeur) {
            ShortestPathData data = new ShortestPathData(graph, graph.getNodes().get(origin), graph.getNodes().get(destination), filter);
            ShortestPathAlgorithm algorithm = constructeur.apply(data);
            return algorithm.run();
        }
    }
}
