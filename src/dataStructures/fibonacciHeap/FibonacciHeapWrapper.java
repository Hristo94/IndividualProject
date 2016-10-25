package dataStructures.fibonacciHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 11/10/16.
 */
public class FibonacciHeapWrapper implements Heap<Vertex> {
    FibonacciHeapNode<Vertex>[] fibonacciHeapNodes;
    FibonacciHeap<Vertex> fibonacciHeap;

    public long total = 0;
    public FibonacciHeapWrapper(int n) {
        fibonacciHeapNodes = new FibonacciHeapNode[n];
        fibonacciHeap = new FibonacciHeap<>();
    }

    @Override
    public Vertex removeMin() {
        long start = System.currentTimeMillis();
        Vertex v =  fibonacciHeap.removeMin().getData();
        long end = System.currentTimeMillis();
        total += end - start;
        return v;
    }

    @Override
    public boolean isEmpty() {
        return fibonacciHeap.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        fibonacciHeapNodes[vertex.getIndex() - 1] = new FibonacciHeapNode<>(vertex);
        fibonacciHeap.insert(fibonacciHeapNodes[vertex.getIndex() - 1], vertex.getDistance());
    }

    @Override
    public void restoreHeapProperty(Vertex v) {
        FibonacciHeapNode fibonacciHeapNode = fibonacciHeapNodes[v.getIndex() - 1];
        if(fibonacciHeapNode == null) {
            insert(v);
        }
        else {
            fibonacciHeap.decreaseKey(fibonacciHeapNode, v.getDistance());
        }
    }
}
