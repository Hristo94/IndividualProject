package graph;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 class to represent a vertex in a graph
 */
public class Vertex implements Comparable<Vertex>{

    private Queue<AdjListNode> adjList;
    private int index;
    private int predecessor;
    private int distance;
    private boolean processed;

    public Vertex(int n) {
        /**
         use concurrent data structure for the adjacency list
         in order to process the input file with multiple threads
         */
        adjList = new ConcurrentLinkedQueue();
        index = n;
        distance = Integer.MAX_VALUE;
        predecessor = n;
        processed = false;
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

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public int compareTo(Vertex v) {
        return this.getDistance() - v.getDistance();
    }
}