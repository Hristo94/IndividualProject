package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap.FibonacciHeapWrapper;
import dataStructures.interfaces.Heap;
import graph.Graph;
import graph.Vertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args)  {
        String inputFileName = args[0];
        String heapTypeFlag = args[1];
        int startVertex = Integer.parseInt(args[2]);
        int endVertex = Integer.parseInt(args[3]);

        try {
            Graph graph = createGraphFrom(inputFileName);
            Heap heap = createHeapFrom(heapTypeFlag, graph.size());

            long start = System.currentTimeMillis();
            graph.findShortestPath(startVertex, heap);
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;

            printOutput(graph, startVertex, endVertex, elapsedTime);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static Heap createHeapFrom(String heapTypeFlag, int size) {
        switch(heapTypeFlag) {
            case "f":case "fibonacci": {
                return new FibonacciHeapWrapper(size);
            }
            case "b":case "binomial": {
                return new BinaryHeapWrapper(size);
            }
            default: {
                throw new IllegalArgumentException("Invalid heap type flag: " + heapTypeFlag);
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

    private static void printOutput(Graph graph, int startVertex, int endVertex, long elapsedTime) {
        int distance = graph.getVertex(endVertex).getDistance();
        if(distance == Integer.MAX_VALUE) {
            System.out.println("Path between " + startVertex + " and " + endVertex + " doesn't exist.");
        }
        else {
            String path = "";
            int cur = graph.getVertex(endVertex).getIndex();
            path = cur + " " + path;
            while(cur != startVertex){
                cur = graph.getVertex(cur).getPredecessor();
                path = cur + " " + path;
            }

            System.out.println("Shortest distance between " + startVertex + " and " + endVertex + " is " + distance);
            System.out.println("Shortest path: " + path );
        }

        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }
}