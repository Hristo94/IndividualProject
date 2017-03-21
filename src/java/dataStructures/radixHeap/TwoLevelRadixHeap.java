package dataStructures.radixHeap;

import dataStructures.generic.DList;
import dataStructures.generic.Heap;
import graph.Vertex;

public class TwoLevelRadixHeap implements Heap<Vertex> {
    private int MAX_BUCKET;

    private DList[][] buckets;
    private int[] upperBound; // array of upper bounds
    private int[] bucketSize; // store the size for each bucket

    private int lastDeleted = 0;
    private int size = 0;
    private int K = 0; // number of segments

    public TwoLevelRadixHeap(int maxDistance, int K) {
        this.K = K; //

        // normally we need B = logK(C + 1) + 1 buckets starting from 1 to B
        // for convenience we create one additional bucket that will not be used
        // since array indices start from index 0
        MAX_BUCKET = (int)Math.ceil((Math.log(maxDistance + 1) / Math.log(K)) + 2);
        buckets = new DList[MAX_BUCKET][K];
        upperBound = new int[MAX_BUCKET]; // array of upper bounds
        bucketSize = new int[MAX_BUCKET]; // store the size for each bucket

        // initialise the buckets
        for(int i = 1; i < buckets.length; i++) {
            for(int j = 0; j < K; j++){
                buckets[i][j] = new DList();
            }
        }

        // initialise the upper bounds
        upperBound[0] = lastDeleted - 1;
        upperBound[upperBound.length - 1] = Integer.MAX_VALUE;
        for(int i = 1; i < upperBound.length - 1; i++) {
            int sum = 0;
            for(int j = 1; j <= i; j++) {
                sum += Math.pow(K, j);
            }
            upperBound[i] = sum - 1;
        }

        // initialise the bucket sizes
        for(int i = 1; i < bucketSize.length - 1; i++) {
            bucketSize[i] = (int)Math.pow(K, i);
        }

        bucketSize[bucketSize.length - 1] = Integer.MAX_VALUE;
    }

    public void insert(Vertex v) {
        int bucketIndex = getBucketIndex(v);
        int segmentIndex = getSegmentIndex(v,bucketIndex);

        buckets[bucketIndex][segmentIndex].insert(v);

        v.bucketIndex = bucketIndex;
        v.segmentIndex = segmentIndex;

        size++;
    }

    public void decreaseKey(Vertex v, int newDistance) {
        DList bucket = buckets[v.bucketIndex][v.segmentIndex];

        bucket.remove(v);

        v.setDistance(newDistance);
        int bucketIndex = getBucketIndex(v);
        int segmentIndex = getSegmentIndex(v,bucketIndex);

        buckets[bucketIndex][segmentIndex].insert(v);

        v.bucketIndex = bucketIndex;
        v.segmentIndex = segmentIndex;
    }

    // move all vertices from the bucket of the lastDeleted
    // to lowerIndexed buckets. Only performed for buckets > 1
    private void redistribute(Vertex minVertex) {
        int bucketIndex = minVertex.bucketIndex;
        int segmentIndex = minVertex.segmentIndex;

        lastDeleted = minVertex.getDistance();
        updateUpperBounds(minVertex.bucketIndex);

        DList bucket = buckets[bucketIndex][segmentIndex];

        while(!bucket.isEmpty()) {
            Vertex vertex = bucket.poll();
            if(!vertex.equals(minVertex)) {
                bucketIndex = getBucketIndex(vertex, vertex.bucketIndex);
                segmentIndex = getSegmentIndex(vertex, bucketIndex);

                buckets[bucketIndex][segmentIndex].insert(vertex);

                vertex.bucketIndex = bucketIndex;
                vertex.segmentIndex = segmentIndex;
            }
        }
    }

    public Vertex removeMin() {
        // find the first non-empty segment
        for (int i = 1; i < buckets.length; i++) {
            for(int j = 0; j < K; j++) {
                DList bucket = buckets[i][j];
                if (!bucket.isEmpty()) {
                    // find the min vertex
                    Vertex minVertex;
                    if(i == 1) {
                        minVertex = bucket.poll();
                    }
                    else {
                        minVertex = bucket.findMin();
                        redistribute(minVertex);
                    }

                    size--;
                    return minVertex;
                }
            }
        }
        return null;
    }

    public boolean isEmpty() { return size() == 0; }

    public int size() { return size; }

    // after removal, the upper bounds need to be updated according to teh value of lastDeleted
    private void updateUpperBounds(int bucketIndex) {
        upperBound[0] = lastDeleted - 1;
        for(int i = 1; i < bucketIndex; i++) {
            upperBound[i] = Math.min(upperBound[i-1] + bucketSize[i], upperBound[bucketIndex]);
        }
    }

    // used during insertion to locate the bucket whose range matches the distance of the vertex
    private int getBucketIndex(Vertex v) {
        for(int i = upperBound.length - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    // used during decreaseKey and removeMin to reinsert a vertex into the bucket whose range matches the distance of the vertex
    private int getBucketIndex(Vertex v, int startingIndex) {
        for(int i = startingIndex - 1; i >= 0 ; i--) {
            if(upperBound[i] < v.getDistance()) {
                return i + 1;
            }
        }
        return 0;
    }

    // used during insertion to locate the segment within the given bucket to store the vertex
    public int getSegmentIndex(Vertex v, int bucketIndex) {
        if(bucketIndex == MAX_BUCKET - 1) { // the lastRemoved bucket has only one segment
            return 0;
        }
        int i = (upperBound[bucketIndex] - v.getDistance()) / (int) Math.pow(K, bucketIndex - 1);
        return K - i - 1 ;
    }
}