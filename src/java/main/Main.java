package java.main;

import java.dataStructures.binaryHeap.BinaryHeapWrapper;
import java.dataStructures.fibonacciHeap.FibonacciHeapWrapper;
import java.dataStructures.radixHeap.RadixHeap;
import java.dataStructures.radixHeap.TwoLevelRadixHeap;
import java.dataStructures.radixHeap.TwoLevelRadixHeap2;
import java.dataStructures.vanEmdeBoas.VEBTree;
import java.graph.Graph;
import java.io.IOException;

public class Main {
    /**
        The main method initializes the User Interface,
        from which we control the execution of the program.
     */
    public static void main(String[] args) throws IOException {
//        Graph graph = Utils.createGraphFrom("parsed1.txt");
        //Utils.generateRandomGraph(10000000, 0.0000005, 500, "10mil-50mil-500.txt");
        Graph graph = Graph.generateRandomGraph(10000,100000000,1000000);
       BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph.size());
       // FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph.size());
         //RadixHeap radixHeap = new RadixHeap(graph.getMaxDistance());
//        TwoLevelRadixHeap twoLevelRadixHeap = new TwoLevelRadixHeap((int)Math.pow(2,14));
        //TwoLevelRadixHeap2 twoLevelRadixHeap2 = new TwoLevelRadixHeap2(graph.getMaxDistance(),(int)Math.pow(2,10));


      //  VEBTree vebTree = new VEBTree(graph.getMaxDistance());
        long start = System.currentTimeMillis();
        graph.findShortestPath(5,binaryHeapWrapper);
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph, 5 , 55, end - start));


//        System.out.println(vebTree.time / 1000000);
    }
}