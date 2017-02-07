package dataStructures.vanEmdeBoas;

import dataStructures.interfaces.Heap;
import graph.Vertex;

/**
 * Created by hristo on 24/01/2017.
 */
public class VEBTreeWrapper implements Heap<Vertex> {
    @Override
    public Vertex removeMin() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void insert(Vertex vertex) {

    }

    @Override
    public void decreaseKey(Vertex vertex, int newDistance) {

    }
}
