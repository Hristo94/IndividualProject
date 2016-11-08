package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap.FibonacciHeapWrapper;
import dataStructures.interfaces.Heap;
import dataStructures.pairingHeap.PairingHeapWrapper;
import graph.AdjListNode;
import graph.Graph;
import graph.Vertex;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by hristo on 01/11/2016.
 */
public class Utils {

    public static Heap createHeapFrom(String heapType, int size) {
        switch(heapType) {
            case Constants.FIBONACCI_HEAP: {
                return new FibonacciHeapWrapper(size);
            }
            case Constants.BINARY_HEAP: {
                return new BinaryHeapWrapper(size);
            }
            case Constants.PAIRING_HEAP: {
                return new PairingHeapWrapper(size);
            }
            default: {
                throw new IllegalArgumentException("Invalid heap type flag: " + heapType);
            }
        }
    }

    public static Graph createGraphFrom(String inputFileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(inputFileName));
        Graph graph = new Graph(Integer.parseInt(in.readLine()));

        // process the file with multiple threads
        in.lines().parallel().forEach(line -> {
            String[] lineElements = line.split(" ");
            int vertexIndex = Integer.parseInt(lineElements[0]);
            int adjacentVertexIndex = Integer.parseInt(lineElements[1]);
            int weight = Integer.parseInt(lineElements[2]);
            Vertex v = graph.getVertex(vertexIndex);
            v.addToAdjList(adjacentVertexIndex, weight);
        });

        return graph;
    }

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

    public static void shuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static void generateRandomGraph(int numVertices, int numEdges, int maxDistance, String fileName) {
        Graph graph = Graph.generateRandomGraph(numVertices, numEdges, maxDistance);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write(graph.size() + "\n");
            for(int i = 1; i <= graph.size(); i++) {
                Vertex v = graph.getVertex(i);
                for(AdjListNode adjListNode: v.getAdjList()) {
                    writer.write(v.getIndex() + " " + adjListNode.getVertexNumber() + " " + adjListNode.getWeight() + "\n");
                }
            }
        } catch (IOException ex) {
            // handle me
        }
        System.out.println("completed");
    }
}
