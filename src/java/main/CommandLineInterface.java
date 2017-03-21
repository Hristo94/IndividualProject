package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap.FibonacciHeapWrapper;
import dataStructures.generic.Heap;
import dataStructures.radixHeap.RadixHeap;
import dataStructures.radixHeap.TwoLevelRadixHeap2;
import dataStructures.vanEmdeBoas.VEBTree;
import graph.Graph;
import graph.Vertex;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by hristo on 20/03/2017.
 */
public class CommandLineInterface {
    private static Scanner standardInput;
    public static void init() {
        standardInput = new Scanner(System.in);
        System.out.println("Select how to generate the graph instance by typing the option number");
        System.out.println("1: Random graph");
        System.out.println("2: From file");

        Integer option = Integer.parseInt(standardInput.nextLine());
        while(option != 1 && option != 2) {
            System.out.println("You have typed an incorrect option. Please select again");
            option = Integer.parseInt(standardInput.nextLine());
        }

        if(option == 1) {
            fromRandomGraph();
        }
        else fromFile();
    }

    private static void fromRandomGraph(){
        int numVertices;
        int numEdges;
        int maxDistance;

        Graph graph;
        do {
            System.out.print("Enter the number of vertices: ");
            numVertices = Integer.parseInt(standardInput.nextLine());
            System.out.print("Enter the number of arcs: ");
            numEdges = Integer.parseInt(standardInput.nextLine());
            System.out.print("Enter the maximum allowed distance between vertices: ");
            maxDistance = Integer.parseInt(standardInput.nextLine());

            graph = Graph.generateRandomGraph(numVertices, numEdges, maxDistance);
        }while (graph == null);

        System.out.println("Graph has been successfully created");
        System.out.println();

        runAlgorithm(graph);
    }


    private static void fromFile(){
        System.out.print("Select input file path: ");
        String filePath = standardInput.nextLine();

        try {
            Graph graph = Utils.createGraphFrom(filePath);
            System.out.println("Graph has been successfully created");
            System.out.println();

            runAlgorithm(graph);
        } catch (IOException e) {
            System.out.println("An error occurred while processing the file");
            System.exit(1);
        }
    }

    private static void runAlgorithm(Graph graph) {
        System.out.println("Select a data structure by typing the number of the option");
        System.out.println("1: Binary Heap");
        System.out.println("2: Fibonacci Heap");
        System.out.println("3: One-level Radix Heap");
        System.out.println("4: Two-level Radix Heap");
        System.out.println("5: van Emde Boas Tree");

        Integer option;
        Heap<Vertex> heap = null;
        boolean heapCreated = false;
        while(!heapCreated) {
            option = Integer.parseInt(standardInput.nextLine());
            switch (option) {
                case 1: {
                    heap = new BinaryHeapWrapper(graph.size());
                    heapCreated = true;
                    break;
                }
                case 2: {
                    heap = new FibonacciHeapWrapper(graph.size());
                    heapCreated = true;
                    break;
                }
                case 3: {
                    heap = new RadixHeap(graph.getMaxDistance());
                    heapCreated = true;
                    break;
                }
                case 4: {
                    System.out.print("Enter the number of segments: ");
                    Integer K = Integer.parseInt(standardInput.nextLine());
                    heap = new TwoLevelRadixHeap2(graph.getMaxDistance(), K);
                    heapCreated = true;
                    break;
                }
                case 5: {
                    heap = new VEBTree(graph.getMaxDistance());
                    heapCreated = true;
                    break;
                }
                default:
                    System.out.println("The selected option was invalid. Please enter a heap type again.");
            }
        }

        System.out.println("The heap was created successfully");
        System.out.println();

        System.out.print("Enter the starting vertex: ");
        Integer startVertex = Integer.parseInt(standardInput.nextLine());
        System.out.print("Enter the ending vertex: ");
        Integer endVertex = Integer.parseInt(standardInput.nextLine());

        System.out.println();
        System.out.println("Dijkstra's algorithm is running");
        System.out.println();

        long start = System.currentTimeMillis();
        graph.findShortestPath(startVertex, heap);
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;

        System.out.println(Utils.produceOutput(graph, startVertex, endVertex, elapsedTime));
    }

}
