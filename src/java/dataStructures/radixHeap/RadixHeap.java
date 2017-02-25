package dataStructures.radixHeap;

import dataStructures.interfaces.Heap;
import graph.Vertex;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Collections;
import java.util.HashSet;

public class RadixHeap implements Heap<Vertex> {
    private static final int MAX_BUCKET = 33;

    private ObjectOpenHashSet<Vertex>[] buckets = new ObjectOpenHashSet[MAX_BUCKET];
    private int[] upperBound = new int[MAX_BUCKET]; // array of upper bounds
    private int[] bucketSize = new int[MAX_BUCKET];


    private int lastDeleted = 0;
    private int size = 0;

    public long time = 0;

    public RadixHeap() {
        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new ObjectOpenHashSet<>();
        }

        for(int i = 0; i < upperBound.length; i++) {
            upperBound[i] = (int)Math.pow(2, i - 1) - 1;
        }

        bucketSize[1] = 1;
        for(int i = 2; i < bucketSize.length - 1; i++) {
            bucketSize[i] = (int)Math.pow(2, i - 2);
        }
        bucketSize[bucketSize.length -1] = Integer.MAX_VALUE;
    }

    public void insert(Vertex v) {
        int bucketIndex = getBucketIndex(v);
        buckets[bucketIndex].add(v);
        v.bucketIndex = bucketIndex;
        size++;
    }

    public void decreaseKey(Vertex v, int newDistance) {
        ObjectOpenHashSet<Vertex> bucket = buckets[v.bucketIndex];
        bucket.remove(v);

        v.setDistance(newDistance);
        int bucketIndex = getBucketIndex(v, v.bucketIndex);
        buckets[bucketIndex].add(v);
        v.bucketIndex = bucketIndex;
    }

    private void redistribute(Vertex minVertex) {
        int bucketIndex = minVertex.bucketIndex;
        lastDeleted = minVertex.getDistance();

        updateUpperBounds(minVertex.bucketIndex);
        ObjectOpenHashSet<Vertex> bucket = buckets[bucketIndex];

        for(Vertex vertex: bucket){
            if (!vertex.equals(minVertex)) {
                bucketIndex = getBucketIndex(vertex, vertex.bucketIndex);
                buckets[bucketIndex].add(vertex);
                vertex.bucketIndex = bucketIndex;
            }
        }
        bucket.clear();
    }

    public Vertex removeMin() {
        for (int i = 1; i < buckets.length; i++) {
            ObjectOpenHashSet<Vertex> bucket = buckets[i];
            if (!bucket.isEmpty()) {
                Vertex minVertex;
                if(i == 1) {
                    minVertex = bucket.iterator().next();
                    bucket.remove(minVertex);
                }
                else {
                    minVertex = Collections.min(bucket);
                    redistribute(minVertex);
                }

                size--;
                return minVertex;
            }
        }

        return null;
    }

    public boolean isEmpty() { return size() == 0; }

    public int size() { return size; }

    private void updateUpperBounds(int bucketIndex) {
        upperBound[0] = lastDeleted - 1;
        upperBound[1] = lastDeleted;

        for(int i = 2; i < bucketIndex; i++) {
            upperBound[i] = Math.min(upperBound[i-1] + bucketSize[i], upperBound[bucketIndex]);
        }
    }

    private int getBucketIndex(Vertex v) {
        for(int i = upperBound.length - 1; i >=0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getBucketIndex(Vertex v, int j) {
        for(int i = j - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }
}