package dataStructures.binaryHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 12/10/16.
 */
public class BinaryHeapWrapper implements Heap<Vertex> {

    BinaryHeap<Vertex> binaryHeap;
    BinaryHeapNode<Vertex>[] binaryHeapNodes;

    public BinaryHeapWrapper(int n) {
        binaryHeap = new BinaryHeap<>(n);
        binaryHeapNodes = new BinaryHeapNode[n];
    }

    @Override
    public Vertex removeMin() {
        Vertex data = binaryHeap.removeMin().getData();
        return data;
    }

    @Override
    public boolean isEmpty() {
        return binaryHeap.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        binaryHeapNodes[vertex.getIndex() - 1] = binaryHeap.insert(vertex);
    }

    @Override
    public void decreaseKey(Vertex vertex, int newDistance) {
        vertex.setDistance(newDistance);
        BinaryHeapNode<Vertex> binaryHeapNode = binaryHeapNodes[vertex.getIndex() - 1];
        binaryHeap.upHeap(binaryHeapNode);
    }
}
