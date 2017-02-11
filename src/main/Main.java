package main;

import dataStructures.binaryHeap.BinaryHeapWrapper;
import dataStructures.radixHeap.RadixHeap2;
import dataStructures.radixHeap.TwoLevelRadixHeap;
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
//
        Graph graph2 = Utils.createGraphFrom("parsed2.txt");
        //Graph graph2 = Graph.generateRandomGraph(100000, 500000,500000);
////////////        FibonacciHeapWrapper fibonacciHeapWrapper = new FibonacciHeapWrapper(graph2.size());
        BinaryHeapWrapper binaryHeapWrapper = new BinaryHeapWrapper(graph2.size());
        //RadixHeap2 radixHeap = new RadixHeap2();
////////////////
////////
        //TwoLevelRadixHeap radixHeap = new TwoLevelRadixHeap();
        RadixHeap2 radixHeap = new RadixHeap2();
        long start = System.currentTimeMillis();
        graph2.findShortestPath(5,radixHeap);
        long end = System.currentTimeMillis();
        System.out.println(Utils.produceOutput(graph2, 5 , 55, end - start));
        System.out.println(radixHeap.time / 1000000);
        //System.out.println(log2_floor((long)Math.pow(2,31) + 1) + 2) ;
      //  RadixHeap2 radixHeap2= new RadixHeap2();
//        int x = 5540;
//        System.out.println(Integer.toString(5540, 4));
//            int x = 5541; // 1112213
//            int y = 5540; // 1112220
//
//            int xorred = x ^ y;
//        int res = log2_floor(xorred);
//        System.out.println(res);
////
//        System.out.println(TwoLevelRadixHeap.MAX_BUCKET);
//
//
//        int result = 0;
//        int x = 16;
//        while ((x = x >> 2) > 0) result++;
//        System.out.println(result);
       // System.out.println(4294967296L ^ 2147483648L);
    }

    public static int log2_floor (long x)
    {
        int res = -1;
        while (x > 0) { res++ ; x = x >> 1; }
        return res;
    }
}