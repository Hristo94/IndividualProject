package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import graph.Graph;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args)  {
        //UserInterface.init();


        Graph graph = Graph.generateRandomGraph(5000000,150000000,5000000);
        System.out.println("Graph generated");
        long start = System.currentTimeMillis();
        graph.findShortestPath(5, new BinaryHeapWrapper(graph.size()));
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph, 5, 55, end - start));

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