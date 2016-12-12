package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap2.FibonacciHeapWrapper2;
import graph.Graph;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args)  {
        //UserInterface.init();


        Graph graph = Graph.generateRandomGraph(Integer.parseInt(args[0]),Long.parseLong(args[1]),5000000);
        System.out.println("Graph generated");
        long start = System.currentTimeMillis();
        if(args[2].equals("b")) {
            graph.findShortestPath(5, new BinaryHeapWrapper(graph.size()));
        }
        else {
            graph.findShortestPath(5, new FibonacciHeapWrapper2(graph.size()));
        }
        //graph.findShortestPath(5, new BinaryHeapWrapper(graph.size()));
        //graph.findShortestPath(5, new FibonacciHeapWrapper2(graph.size()));
        long end = System.currentTimeMillis();
        //String fileName = "binary-100,000,000v-1,000,000,000e.txt";
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[3]), StandardCharsets.UTF_8));
            String output = Utils.produceOutput(graph,5,55, end - start) + '\n';
            writer.write(output);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Graph graph = Utils.createGraphFrom("1000-900000.txt");
//            Heap heap = Utils.createHeapFrom(Constants.BINARY_HEAP, graph.size());
//            long start = System.currentTimeMillis();
//            graph.findShortestPath(5, heap);
//            long end = System.currentTimeMillis();
//            System.out.println(Utils.produceOutput(graph, 5, 55, end - start));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}