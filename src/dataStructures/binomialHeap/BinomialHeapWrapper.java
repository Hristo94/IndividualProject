package dataStructures.binomialHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 12/10/16.
 */
public class BinomialHeapWrapper implements Heap<Vertex> {

    BinomialHeap<Vertex> binomialHeap;
    BinomialHeapNode<Vertex>[] binomialHeapNodes;

    public BinomialHeapWrapper(int n) {
        binomialHeap = new BinomialHeap<>(n);
        binomialHeapNodes = new BinomialHeapNode[n];
    }

    @Override
    public Vertex removeMin() {
        return binomialHeap.removeMin().getData();
    }

    @Override
    public boolean isEmpty() {
        return binomialHeap.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        binomialHeapNodes[vertex.getIndex() - 1] = binomialHeap.insert(vertex);
    }

    @Override
    public void restoreHeapProperty(Vertex vertex) {
        BinomialHeapNode<Vertex> binomialHeapNode = binomialHeapNodes[vertex.getIndex() - 1];
        if(binomialHeapNode == null) {
            insert(vertex);
        }
        else {
            binomialHeap.upHeap(binomialHeapNode);
        }
    }
}
