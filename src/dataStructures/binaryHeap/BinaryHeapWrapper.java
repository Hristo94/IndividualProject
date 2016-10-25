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
        return binaryHeap.removeMin().getData();
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
    public void restoreHeapProperty(Vertex vertex) {
        BinaryHeapNode<Vertex> binaryHeapNode = binaryHeapNodes[vertex.getIndex() - 1];
        if(binaryHeapNode == null) {
            insert(vertex);
        }
        else {
            binaryHeap.upHeap(binaryHeapNode);
        }
    }
}
