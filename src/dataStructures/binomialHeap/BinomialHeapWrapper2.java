package dataStructures.binomialHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;

import java.util.PriorityQueue;

/**
 * Created by hristo on 11/10/16.
 */
public class BinomialHeapWrapper2 implements Heap<Vertex> {
    PriorityQueue<Vertex> priorityQueue;
    public int total = 0;
    public BinomialHeapWrapper2() {
        priorityQueue = new PriorityQueue<>();
    }

    @Override
    public Vertex removeMin() {
        return priorityQueue.poll();
    }

    @Override
    public boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    @Override
    public void insert(Vertex vertex) {
        priorityQueue.add(vertex);
    }

    @Override
    public void restoreHeapProperty(Vertex v) {
        long start = System.currentTimeMillis();
        priorityQueue.remove(v);
        long end = System.currentTimeMillis();
        total += end - start;
        priorityQueue.add(v);
    }
}
