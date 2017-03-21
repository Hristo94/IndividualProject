package graph;

import dataStructures.generic.Heap;

import java.util.*;

// Represents an undirected graph using adjacency lists
public class Graph {
    private int maxDistance = 0; // used for Radix Heaps and VEB tree
    private Vertex[] vertices; // the vertices are stored in an array, each vertex has a unique id
                                // which determines its index position

    public Graph(int n) {
        vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            // the vertex ids start from 1 but are stored from position 0.
            vertices[i] = new Vertex(i + 1);
        }
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    // Generates a random graph for a number of vertices, probability for an edge to be a part of the graph
    // and a maximum weight of each edge. The algorithm is adopted from the paper
    // "Efficient generation of large random networks, V. Batagelj and B. Ulrik - 2005".
    public static Graph generateRandomGraph(int numVertices, double probability, int maxDistance) {
        Graph graph = new Graph(numVertices);
        Random random = new Random();
        graph.setMaxDistance(maxDistance);

        int v = 1;
        int w = -1;
        double r;

        while(v < numVertices) {
            r = random.nextDouble();
            w += 1 + (Math.log(1 - r)/ Math.log(1 - probability));
            while(w >= v && v < numVertices) {
                w = w - v;
                v = v + 1;
            }

            if(v < numVertices) {
                int distance = random.nextInt(maxDistance - 1) + 1;
                graph.getVertex(v + 1).addToAdjList(w + 1,distance);
                graph.getVertex(w + 1).addToAdjList(v + 1,distance);
            }
        }
        return graph;
    }

    // generates a random graph by specifying number of vertices, number of edges and the maximum weight of an edge
    // from the vertices and edges, the probability of an edge to be a part of the graph is calculated
    // and the delegate method that performs the generation is invoked
    public static Graph generateRandomGraph(int numVertices, int numEdges, int maxDistance) {
        long possibleEdges = ((long)numVertices * (long)(numVertices - 1)) ;
        if(possibleEdges < numEdges) {
            System.out.println("The number of edges is bigger than the number of possible edges");
            return null;
        }

        double probability = (1.0 * numEdges / (possibleEdges))  ;
        return generateRandomGraph(numVertices, probability, maxDistance);
    }

    public int size() {
        return vertices.length;
    }

    public Vertex getVertex(int i) {
        return vertices[i - 1];
    }

    // Performs the Dijkstra's algorithm for the single-source shortest path problem
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

            Queue<AdjListNode> adjList = v.getAdjList();
            for(AdjListNode node: adjList){  // perform relaxation
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
