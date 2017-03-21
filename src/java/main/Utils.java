package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap.FibonacciHeapWrapper;
import dataStructures.generic.Heap;
import dataStructures.radixHeap.RadixHeap;
import dataStructures.radixHeap.TwoLevelRadixHeap2;
import dataStructures.vanEmdeBoas.VEBTree;
import graph.AdjListNode;
import graph.Graph;
import graph.Vertex;

import java.io.*;
import java.nio.charset.StandardCharsets;

// Contains commonly used utility methods that simplify the work of the use interfaces
public class Utils {

    // specifying a heap type from the GUI combobox, results in generating the appropriate heap

    public static Heap createHeapFrom(String heapType, Graph graph) {
        switch(heapType) {
            case Constants.FIBONACCI_HEAP: {
                return new FibonacciHeapWrapper(graph.size());
            }
            case Constants.BINARY_HEAP: {
                return new BinaryHeapWrapper(graph.size());
            }
            case Constants.ONE_LEVEL_RADIX_HEAP: {
                return new RadixHeap(graph.getMaxDistance());
            }
            case Constants.TWO_LEVEL_RADIX_HEAP: {
                // todo change K not to be hardcoded
                return new TwoLevelRadixHeap2(graph.getMaxDistance(), 1024);
            }
            case Constants.VAN_EMDE_BOAS_TREE: {
                return new VEBTree(graph.getMaxDistance());
            }
            default: {
                throw new IllegalArgumentException("Invalid heap type flag: " + heapType);
            }
        }
    }

    // Loads a text file containing a benchmark instance from the 9th DIMACS Implementation Challenge
    // The program is compatible only with this file format
    public static Graph createGraphFrom(String inputFileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(inputFileName));

        int maxDistance = 0;
        int numVertices = 0;

        String line;
        while((line = in.readLine()) != null) {
            String[] lineElements = line.split(" ");
            if(lineElements[0].equals("p")) {
                numVertices = Integer.parseInt(lineElements[2]);
                break;
            }
        }

        Graph graph = new Graph(numVertices);
        while((line = in.readLine()) != null) {
            String[] lineElements = line.split(" ");
            if(lineElements[0].equals("a")) {
                break;
            }
        }

        while(line != null) {
            String[] lineElements = line.split(" ");
            int vertexIndex = Integer.parseInt(lineElements[1]);
            int adjacentVertexIndex = Integer.parseInt(lineElements[2]);
            int weight = Integer.parseInt(lineElements[3]);
            if(weight > maxDistance) {
                maxDistance = weight;
            }

            Vertex v = graph.getVertex(vertexIndex);
            v.addToAdjList(adjacentVertexIndex, weight);

            line = in.readLine();
        }

        graph.setMaxDistance(maxDistance);
        return graph;
    }

    // Produces the final output by displaying what the shortest path is and what is the elapsed time
    public static String produceOutput(Graph graph, int startVertex, int endVertex, long elapsedTime) {
        String output = "";

        int distance = graph.getVertex(endVertex).getDistance();
        if(distance == Integer.MAX_VALUE) {
            output += "Path between " + startVertex + " and " + endVertex + " doesn't exist.";
        }
        else {
            String path = "";
            int cur = graph.getVertex(endVertex).getIndex();
            path = cur + " " + path;
            while(cur != startVertex){
                cur = graph.getVertex(cur).getPredecessor();
                path = cur + " " + path;
            }

            output += "Shortest distance between " + startVertex + " and " + endVertex + " is " + distance + "\n\n";
            output += "Shortest path: " + path + "\n\n";
        }

        output += "Elapsed time: " + elapsedTime + " milliseconds";
        return  output;
    }
}
