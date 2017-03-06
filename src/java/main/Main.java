package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.fibonacciHeap.FibonacciHeapWrapper;
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
//        Graph graph = Utils.createGraphFrom("parsed1.txt");
        //Utils.generateRandomGraph(10000000, 0.0000005, 500, "10mil-50mil-500.txt");
        int numVertices = 1000000;
        int numEdges = numVertices * 5;

        Graph graph = Graph.generateRandomGraph(numVertices,numEdges,1000000);
       BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph.size());
//       FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph.size());
         //RadixHeap radixHeap = new RadixHeap(graph.getMaxDistance());
//        TwoLevelRadixHeap twoLevelRadixHeap = new TwoLevelRadixHeap((int)Math.pow(2,14));
        TwoLevelRadixHeap2 twoLevelRadixHeap2 = new TwoLevelRadixHeap2(graph.getMaxDistance(),(int)Math.pow(2,10));


     //   VEBTree vebTree = new VEBTree(graph.getMaxDistance());
        long start = System.nanoTime();
        graph.findShortestPath(5,twoLevelRadixHeap2);
        long end = System.nanoTime();
        System.out.println(Utils.produceOutput(graph, 5 , 55, (end - start) / 1000000));


//        System.out.println(vebTree.time / 1000000);
    }
}