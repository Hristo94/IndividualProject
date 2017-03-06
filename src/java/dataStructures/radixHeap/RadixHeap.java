package dataStructures.radixHeap;

import dataStructures.DList;
import dataStructures.interfaces.Heap;
import graph.Vertex;


import java.util.Collections;

public class RadixHeap implements Heap<Vertex> {
    private int MAX_BUCKET;

    private DList[] buckets = new DList[MAX_BUCKET];
    private int[] upperBound = new int[MAX_BUCKET]; // array of upper bounds
    private int[] bucketSize = new int[MAX_BUCKET];
    private int lastDeleted = 0;
    private int size = 0;

    public RadixHeap(int maxDistance) {

        // normally we need B = log2(C + 1) + 2 buckets starting from 1 to B
        // for convenience we create one additional bucket that will not be used
        // since arrays start from index 0
        MAX_BUCKET = (int)Math.ceil((Math.log(maxDistance + 1) / Math.log(2))) + 3;
        buckets = new DList[MAX_BUCKET];
        upperBound = new int[MAX_BUCKET]; // array of upper bounds
        bucketSize = new int[MAX_BUCKET];

        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new DList();
        }

        for(int i = 0; i < upperBound.length - 1; i++) {
            upperBound[i] = (int)Math.pow(2, i - 1) - 1;
        }
        upperBound[upperBound.length - 1] = Integer.MAX_VALUE;

        bucketSize[1] = 1;
        for(int i = 2; i < bucketSize.length - 1; i++) {
            bucketSize[i] = (int)Math.pow(2, i - 2);
        }
        bucketSize[bucketSize.length -1] = Integer.MAX_VALUE;
    }

    public void insert(Vertex v) {
        int bucketIndex = getBucketIndex(v);
        buckets[bucketIndex].insert(v);
        v.bucketIndex = bucketIndex;
        size++;
    }

    public void decreaseKey(Vertex v, int newDistance) {
        DList bucket = buckets[v.bucketIndex];
        bucket.remove(v);

        v.setDistance(newDistance);
        int bucketIndex = getBucketIndex(v, v.bucketIndex);
        buckets[bucketIndex].insert(v);
        v.bucketIndex = bucketIndex;
    }

    private void redistribute(Vertex minVertex) {
        int bucketIndex = minVertex.bucketIndex;
        lastDeleted = minVertex.getDistance();

        updateUpperBounds(minVertex.bucketIndex);
        DList bucket = buckets[bucketIndex];
        while(!bucket.isEmpty()) {
            Vertex vertex = bucket.poll();
            if (!vertex.equals(minVertex)) {
                bucketIndex = getBucketIndex(vertex, vertex.bucketIndex);
                buckets[bucketIndex].insert(vertex);
                vertex.bucketIndex = bucketIndex;
            }
        }
    }

    public Vertex removeMin() {
        for (int i = 1; i < buckets.length; i++) {
            DList bucket = buckets[i];
            if (!bucket.isEmpty()) {
                Vertex minVertex;
                if(i == 1) {
                    minVertex = bucket.poll();
//                    bucket.remove(minVertex);
                }
                else {
                    minVertex = bucket.findMin();
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