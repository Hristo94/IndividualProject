package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.radixHeap.RadixHeap;
import graph.Graph;

import java.io.IOException;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args) throws IOException {
        // uncomment this line to turn on the UI
        //UserInterface.init();
        
        Graph graph2 = Utils.createGraphFrom("parsed.txt");
        //Graph graph2 = Graph.generateRandomGraph(10000000, 50000000,500000);
//        FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph2.size());
        BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph2.size());
        RadixHeap radixHeap = new RadixHeap();
//
        long start = System.currentTimeMillis();
        graph2.findShortestPath(5,radixHeap);
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph2, 5 , 55, end - start));
    }

}