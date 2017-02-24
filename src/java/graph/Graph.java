package graph;

import dataStructures.interfaces.Heap;

import java.util.*;

/**
 class to represent an undirected graph using adjacency lists
 */
public class Graph {
    private int maxDistance = 0; // used for Van Emde Boas tree, that needs to know the max distance
                            // between two vertices in advance

    private Vertex[] vertices; // the (array of) vertices

    public Graph(int n) {
        vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i + 1);
        }
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public static Graph generateRandomGraph(int numVertices, double probability, int maxDistance) {
        Graph graph = new Graph(numVertices);
        Random random = new Random();
        long size = 0;

        for(int i = 1; i <= numVertices; i++) {
            for(int j = i + 1; j <= numVertices; j += 1 + (Math.log(1 - random.nextDouble()) / Math.log(1 - probability))) {
                Vertex v = graph.getVertex(i);
                Vertex w = graph.getVertex(j);

                int distance = random.nextInt(maxDistance - 1) + 1;
                if(distance > maxDistance) {
                    maxDistance = distance;
                }

                // since the graph is undirected,
                // both vertices should add the other vertex to their adjacency list
                v.addToAdjList(j, distance);
                w.addToAdjList(i, distance);

                size++;
            }
        }
//
        System.out.println(size);
        return graph;
    }

    public int size() {
        return vertices.length;
    }

    public Vertex getVertex(int i) {
        return vertices[i - 1];
    }

    public void findShortestPath(int startVertex, Heap<Vertex> heap) {
        //initialization
        Vertex u = getVertex(startVertex);
        u.setDistance(0);
        heap.insert(u);

        while(!heap.isEmpty()){
            // find v not in S with d(v) minimum;
            Vertex v = heap.removeMin();
            v.setProcessed(true); // shortest path to 'v' is already known
            // perform decreaseKey for each vertex 'w' adjacent to 'v'
            for(AdjListNode node: v.getAdjList()){
                Vertex w = getVertex(node.getVertexNumber());
                if(!w.isProcessed()) {
                    int newDistance = v.getDistance() + node.getWeight();
                    if(newDistance < w.getDistance()){
                        w.setPredecessor(v.getIndex());
                        if(w.isInserted()) {
                            heap.decreaseKey(w, newDistance);
                        }else {
                            w.setDistance(newDistance);
                            w.setInserted(true);
                            heap.insert(w);
                        }
                    }
                }
            }
        }
    }
}
