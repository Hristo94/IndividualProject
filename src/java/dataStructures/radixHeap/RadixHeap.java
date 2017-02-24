package dataStructures.radixHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;
import java.util.Collections;
import java.util.HashSet;

public class RadixHeap implements Heap<Vertex> {
    private static final int MAX_BUCKET = 33;

    private HashSet<Vertex>[] buckets = new HashSet[MAX_BUCKET];
    private int lastDeleted = 0;
    private int size = 0;

    public long time = 0;

    public RadixHeap() {
        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new HashSet<>();
        }
    }

    public void insert(Vertex v) {
        buckets[getBucketIndex(v)].add(v);
        size++;
    }

    public void decreaseKey(Vertex v, int newDistance) {
        HashSet<Vertex> bucket = buckets[getBucketIndex(v)];
        bucket.remove(v);

        v.setDistance(newDistance);
        buckets[getBucketIndex(v)].add(v);
    }

    private void redistribute(Vertex minVertex) {
        int bucketIndex = getBucketIndex(minVertex);
        lastDeleted = minVertex.getDistance();

        if (bucketIndex == 0) {
            HashSet<Vertex> bucket = buckets[0];
            bucket.remove(bucket.iterator().next());
        } else {
            HashSet<Vertex> bucket = buckets[bucketIndex];
            for(Vertex vertex: bucket) {
                if(!vertex.equals(minVertex)) {
                    buckets[getBucketIndex(vertex)].add(vertex);
                }
            }
            bucket.clear();
        }
    }

    public Vertex removeMin() {
        Vertex minVertex = null;
        for (int i = 0; i < buckets.length; i++) {
            HashSet<Vertex> bucket = buckets[i];
            if (!bucket.isEmpty()) {
               // System.out.println(bucket.size());
                minVertex = Collections.min(bucket);
                break;
            }
        }

        size--;
        redistribute(minVertex);
        return minVertex;
    }

    public boolean isEmpty() { return size() == 0; }

    public int size() { return size; }

    private int getBucketIndex(Vertex v) {
        int xorred = lastDeleted ^ v.getDistance();
        return log(xorred) + 1;
    }

    private int log(int x) {
        int res = -1;
        while (x > 0) { res++ ; x = x >> 1; }
        return res;
    }
}