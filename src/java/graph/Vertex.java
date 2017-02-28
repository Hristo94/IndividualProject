package graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 class to represent a vertex in a graph
 */
public class Vertex implements Comparable<Vertex>{

    private Queue<AdjListNode> adjList;
    private int index;
    private int predecessor;
    private int distance;
    private boolean processed;
    private boolean inserted;


    // needed for radix heap
    public int bucketIndex;
    public int segmentIndex;

    // neded for radix heap and veb tree
    public  Vertex prev;
    public Vertex next;


    public Vertex(int n) {
        /**
         use concurrent data structure for the adjacency list
         in order to process the input file with multiple threads
         */
        adjList = new LinkedList<>();
        index = n;
        distance = Integer.MAX_VALUE;
        predecessor = n;
        processed = false;
        inserted = false;
    }

    public Queue<AdjListNode> getAdjList(){
        return adjList;
    }

    public int getIndex(){
        return index;
    }

    public int getPredecessor(){
        return predecessor;
    }

    public void setPredecessor(int n){
        predecessor = n;
    }

    public void addToAdjList(int n, int weight){
        adjList.add(new AdjListNode(n,weight));
    }

    public int getDistance(){
        return distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public Vertex getPrev() {
        return prev;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
    }

    public Vertex getNext() {
        return next;
    }

    public void setNext(Vertex next) {
        this.next = next;
    }

    @Override
    public int compareTo(Vertex v) {
        int compare = this.distance - v.distance;
        if(compare == 0) {
            compare = this.index - v.index;
        }
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        return index == ((Vertex) o).index;
    }

}