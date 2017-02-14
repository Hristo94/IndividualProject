package dataStructures.radixHeap;

import com.googlecode.javaewah.datastructure.BitSet;
import dataStructures.interfaces.Heap;
import graph.Vertex;

import java.util.Collections;
import java.util.HashSet;

public class TwoLevelRadixHeap2 implements Heap<Vertex> {
    public static final int K = 1024;
    public static final int MAX_BUCKET = (int) (Math.log(Math.pow(2,32) + 1) / Math.log(K)) + 2;
    
    private HashSet<Vertex>[][] buckets = new HashSet[MAX_BUCKET][K];
    private int[] upperBound = new int[MAX_BUCKET]; // array of upper bounds
    private int[] bucketSize = new int[MAX_BUCKET]; // store the size for each bucket

    private BitSet bucketBitSet = new BitSet(MAX_BUCKET);
    private BitSet[] segmentBitSets = new BitSet[MAX_BUCKET];

    private int lastDeleted = 0;
    private int size = 0;

    public long time = 0;

    public TwoLevelRadixHeap2() {
        for(int i = 1; i < buckets.length; i++) {
            for(int j = 0; j < K; j++){
                buckets[i][j] = new HashSet<>();
            }
        }

        upperBound[0] = lastDeleted - 1;
        upperBound[upperBound.length - 1] = Integer.MAX_VALUE;

        for(int i = 1; i < upperBound.length - 1; i++) {
            int sum = 0;
            for(int j = 1; j <= i; j++) {
                sum += Math.pow(K, j);
            }
            upperBound[i] = sum - 1;
        }

        for(int i = 1; i < bucketSize.length - 1; i++) {
            bucketSize[i] = (int)Math.pow(K, i);
        }

        bucketSize[bucketSize.length - 1] = Integer.MAX_VALUE;


        // initialize the segment bit map
        for(int i = 0; i < segmentBitSets.length; i++) {
            segmentBitSets[i] = new BitSet(K);
        }
    }

    public void insert(Vertex v) {
        int bucketIndex = getBucketIndex(v);
        int segmentIndex = getSegmentIndex(v,bucketIndex);

        insert(v, bucketIndex, segmentIndex);

        size++;
    }

    private void insert(Vertex v, int bucketIndex, int segmentIndex) {
        buckets[bucketIndex][segmentIndex].add(v);

        v.bucketIndex = bucketIndex;
        v.segmentIndex = segmentIndex;

        bucketBitSet.set(bucketIndex);
        segmentBitSets[bucketIndex].set(segmentIndex);
    }

    public void decreaseKey(Vertex v, int newDistance) {
        HashSet<Vertex> bucket = buckets[v.bucketIndex][v.segmentIndex];

        bucket.remove(v);

        if(bucket.isEmpty()) {
            BitSet segmentBitSet = segmentBitSets[v.bucketIndex];
            segmentBitSet.clear(v.segmentIndex);

            if(segmentBitSet.empty()) {
                bucketBitSet.clear(v.bucketIndex);
            }
        }

        v.setDistance(newDistance);
        int bucketIndex = getBucketIndex(v);
        int segmentIndex = getSegmentIndex(v,bucketIndex);

        insert(v, bucketIndex, segmentIndex);
    }

    private void redistribute(Vertex minVertex) {
        int bucketIndex = minVertex.bucketIndex;
        int segmentIndex = minVertex.segmentIndex;

        lastDeleted = minVertex.getDistance();
        updateUpperBounds(minVertex.bucketIndex);

        HashSet<Vertex> bucket = buckets[bucketIndex][segmentIndex];

        for(Vertex vertex: bucket){
            if(!vertex.equals(minVertex)) {
                bucketIndex = getBucketIndex(vertex, vertex.bucketIndex);
                segmentIndex = getSegmentIndex(vertex, bucketIndex);

                insert(vertex, bucketIndex, segmentIndex);
            }
        }
        bucket.clear();
    }

    public Vertex removeMin() {
        int bucketIndex = bucketBitSet.nextSetBit(0);
        int segmentIndex = segmentBitSets[bucketIndex].nextSetBit(0);
        HashSet<Vertex> bucket = buckets[bucketIndex][segmentIndex];

        if (!bucket.isEmpty()) {
            Vertex minVertex;
            if(bucketIndex == 1) {
                minVertex = bucket.iterator().next();
                bucket.remove(minVertex);
            }
            else {
                minVertex = Collections.min(bucket);
                redistribute(minVertex);
            }

            size--;

            if(bucket.isEmpty()) {
                BitSet segmentBitSet = segmentBitSets[bucketIndex];
                segmentBitSet.clear(segmentIndex);

                if(segmentBitSet.empty()) {
                    bucketBitSet.clear(bucketIndex);
                }
            }

            return minVertex;
        }
        return null;
    }

    public boolean isEmpty() { return size() == 0; }

    public int size() { return size; }

    private void updateUpperBounds(int bucketIndex) {
        upperBound[0] = lastDeleted - 1;
        for(int i = 1; i < bucketIndex; i++) {
            upperBound[i] = Math.min(upperBound[i-1] + bucketSize[i], upperBound[bucketIndex]);
        }
    }

    private int getBucketIndex(Vertex v) {
        for(int i = upperBound.length - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getBucketIndex(Vertex v, int startingIndex) {
        for(int i = startingIndex - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    public int getSegmentIndex(Vertex v, int bucketIndex) {
        if(bucketIndex == MAX_BUCKET - 1) { // the last bucket has only one segment
            return 0;
        }
        int i = (upperBound[bucketIndex] - v.getDistance()) / (int) Math.pow(K, bucketIndex - 1);
        return K - i - 1 ;
    }
}