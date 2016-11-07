package dataStructures.pairingHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 07/11/2016.
 */
public class PairingHeapWrapper implements Heap<Vertex> {
    PairingHeap<Vertex> pairingHeap;
    PairingHeapNode<Vertex> [] pairingHeapNodes;

    public PairingHeapWrapper(int n) {
        pairingHeap = new PairingHeap();
        pairingHeapNodes = new PairingHeapNode[n];
    }

    @Override
    public Vertex removeMin() {
        return pairingHeap.deleteMin();
    }

    @Override
    public boolean isEmpty() {
        return pairingHeap.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        pairingHeapNodes[vertex.getIndex() - 1] = pairingHeap.insert(vertex);
    }

    @Override
    public void restoreHeapProperty(Vertex vertex) {
        if(pairingHeapNodes[vertex.getIndex() - 1] == null) {
            insert(vertex);
        }
        else {
            pairingHeap.decreaseKey(pairingHeapNodes[vertex.getIndex() - 1], vertex);
        }
    }
}
