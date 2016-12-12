package dataStructures.fibonacciHeap2;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 11/12/2016.
 */
public class FibonacciHeapWrapper2 implements Heap<Vertex> {

    private FibonacciHeap2<Vertex> fibonacciHeap;
    private FibonacciHeapNode2<Vertex>[] fibonacciHeapNodes;

    public FibonacciHeapWrapper2(int n) {
        fibonacciHeapNodes = new FibonacciHeapNode2[n];
        fibonacciHeap = new FibonacciHeap2<>();
    }
    @Override
    public Vertex removeMin() {
        return fibonacciHeap.removeMin().getData();
    }

    @Override
    public boolean isEmpty() {
        return fibonacciHeap.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        FibonacciHeapNode2<Vertex> fibonacciHeapNode = new FibonacciHeapNode2<>(vertex,vertex.getDistance());
        fibonacciHeapNodes[vertex.getIndex() - 1] = fibonacciHeapNode;
        fibonacciHeap.insert(fibonacciHeapNode,vertex.getDistance());
    }

    @Override
    public void restoreHeapProperty(Vertex vertex) {
        FibonacciHeapNode2<Vertex> fibonacciHeapNode = fibonacciHeapNodes[vertex.getIndex() - 1];
        fibonacciHeap.decreaseKey(fibonacciHeapNode, vertex.getDistance());

    }
}
