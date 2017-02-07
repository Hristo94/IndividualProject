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
//        for(int i = 0; i < buckets.length; i++) {
//            System.out.println("bucket " + i + ": " + buckets[i].size());
//        }

        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        Vertex minVertex = null;
        if (!buckets[0].isEmpty()) {
            minVertex = buckets[0].iterator().next();
        } else {
            for (int i = 1; i < buckets.length; i++) {
                HashSet<Vertex> bucket = buckets[i];
                if (!bucket.isEmpty()) {
                    minVertex = Collections.min(bucket);
                    break;
                }
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
        if (xorred == 0) return 0;
        return getLargestBitSet(xorred) + 1;
    }

    private static int getLargestBitSet(int val) {
        if (val == 0) return -1;
        int leadingZeroes = Integer.numberOfLeadingZeros(val);
        return 31 - leadingZeroes;
    }

}