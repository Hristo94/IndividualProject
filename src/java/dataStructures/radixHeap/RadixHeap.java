package dataStructures.radixHeap;

import dataStructures.generic.DList;
import dataStructures.generic.Heap;
import graph.Vertex;


public class RadixHeap implements Heap<Vertex> {
    private int MAX_BUCKET;

    private DList[] buckets = new DList[MAX_BUCKET]; // array of doubly linked lists represents the buckets
    private int[] upperBound = new int[MAX_BUCKET]; // array of upper bounds
    private int[] bucketSize = new int[MAX_BUCKET]; // array of bucket sizes
    private int lastDeleted = 0;
    private int size = 0;

    public RadixHeap(int maxDistance) {

        // normally we need B = log2(C + 1) + 2 buckets starting from 1 to B
        // for convenience we create one additional bucket that will not be used
        // since array indices start from index 0
        MAX_BUCKET = (int)Math.ceil((Math.log(maxDistance + 1) / Math.log(2))) + 3;
        buckets = new DList[MAX_BUCKET];
        upperBound = new int[MAX_BUCKET]; // array of upper bounds
        bucketSize = new int[MAX_BUCKET];

        // initialise the buckets
        for(int i = 0; i < buckets.length; i++) {
            buckets[i] = new DList();
        }

        // initialise the upper bounds
        for(int i = 0; i < upperBound.length - 1; i++) {
            upperBound[i] = (int)Math.pow(2, i - 1) - 1;
        }
        upperBound[upperBound.length - 1] = Integer.MAX_VALUE;

        // initialise the bucket sizes
        bucketSize[1] = 1;
        for(int i = 2; i < bucketSize.length - 1; i++) {
            bucketSize[i] = (int)Math.pow(2, i - 2);
        }
        bucketSize[bucketSize.length -1] = Integer.MAX_VALUE;
    }

    // insert a vertex into its bucket
    public void insert(Vertex v) {
        int bucketIndex = getBucketIndex(v);
        buckets[bucketIndex].insert(v);
        v.bucketIndex = bucketIndex;
        size++;
    }

    // remove a vertex from its bucket, update the distance, and reinsert into its new bucket
    public void decreaseKey(Vertex v, int newDistance) {
        DList bucket = buckets[v.bucketIndex];
        bucket.remove(v);

        v.setDistance(newDistance);
        int bucketIndex = getBucketIndex(v, v.bucketIndex);
        buckets[bucketIndex].insert(v);
        v.bucketIndex = bucketIndex;
    }

    // move all vertices from the bucket of the lastDeleted
    // to lowerIndexed buckets. Only performed for buckets > 1
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

    // removes the minimum vertex from the heap
    public Vertex removeMin() {
        // find the first non-empty bucket
        for (int i = 1; i < buckets.length; i++) {
            DList bucket = buckets[i];
            if (!bucket.isEmpty()) {
                Vertex minVertex;
                if(i == 1) { // return arbitrary element if it is the first bucket
                    minVertex = bucket.poll();
                }
                else {
                    // scan to find the minimum element in the bucket
                    minVertex = bucket.findMin();
                    // redistribute the rest of the vertices to lower indexed buckets
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

    // after removal, the upper bounds need to be updated according to teh value of lastDeleted
    private void updateUpperBounds(int bucketIndex) {
        upperBound[0] = lastDeleted - 1;
        upperBound[1] = lastDeleted;

        for(int i = 2; i < bucketIndex; i++) {
            upperBound[i] = Math.min(upperBound[i-1] + bucketSize[i], upperBound[bucketIndex]);
        }
    }

    // used during insertion to locate the bucket whose range matches the distance of the vertex
    private int getBucketIndex(Vertex v) {
        for(int i = upperBound.length - 1; i >=0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    // used during decreaseKey and removeMin to reinsert a vertex into the bucket whose range matches the distance of the vertex
    private int getBucketIndex(Vertex v, int j) {
        for(int i = j - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }
}