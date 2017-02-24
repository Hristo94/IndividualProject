package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.vanEmdeBoas.VEBTree;
import graph.Graph;

import java.io.IOException;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args) throws IOException {

        Graph graph = Utils.createGraphFrom("parsed1.txt");

        System.out.println();
        //BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph.size());
        //FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph.size());
       // RadixHeap radixHeap = new RadixHeap();
        //TwoLevelRadixHeap twoLevelRadixHeap = new TwoLevelRadixHeap(32);
       // TwoLevelRadixHeap2 twoLevelRadixHeap2 = new TwoLevelRadixHeap2(4096);

        VEBTree vebTree = VEBTree.createVEBTree(graph.getMaxDistance());
        long start = System.currentTimeMillis();
        graph.findShortestPath(5, vebTree);
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph, 5 , 55, end - start));
    }
}