package dataStructures.fibonacciHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 11/10/16.
 */
public class FibonacciHeapWrapper implements Heap<Vertex> {
    FibonacciHeapNode<Vertex>[] fibonacciHeapNodes;
    FibonacciHeap<Vertex> fibonacciHeap;

    public FibonacciHeapWrapper(int n) {
        fibonacciHeapNodes = new FibonacciHeapNode[n];
        fibonacciHeap = new FibonacciHeap<>();
    }

    @Override
    public Vertex removeMin() {
        Vertex v =  fibonacciHeap.removeMin().getData();
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
    public void decreaseKey(Vertex v, int newDistance) {
        v.setDistance(newDistance);
        FibonacciHeapNode fibonacciHeapNode = fibonacciHeapNodes[v.getIndex() - 1];
        fibonacciHeap.decreaseKey(fibonacciHeapNode, v.getDistance());
    }
}
