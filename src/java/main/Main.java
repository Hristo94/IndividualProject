package main;

import dataStructures.radixHeap.RadixHeap;
import dataStructures.radixHeap.TwoLevelRadixHeap;
import dataStructures.radixHeap.TwoLevelRadixHeap2;
import dataStructures.vanEmdeBoas.VEBTree;
import graph.Graph;

import java.io.IOException;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args) throws IOException {
        Graph graph = Utils.createGraphFrom("graph2.txt");

        System.out.println();
        //BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph.size());
        //FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph.size());
        //RadixHeap radixHeap = new RadixHeap();
        //TwoLevelRadixHeap twoLevelRadixHeap = new TwoLevelRadixHeap(32);
        //TwoLevelRadixHeap2 twoLevelRadixHeap2 = new TwoLevelRadixHeap2((int)Math.pow(2,8));

        VEBTree vebTree = VEBTree.createVEBTree(graph.getMaxDistance());
        long start = System.currentTimeMillis();
        graph.findShortestPath(5, vebTree);
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph, 5 , 55, end - start));

    }
}