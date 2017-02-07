package graph;

import dataStructures.interfaces.Heap;

import java.util.*;

/**
 class to represent an undirected graph using adjacency lists
 */
public class Graph {
    private Vertex[] vertices; // the (array of) vertices

    public Graph(int n) {
        vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            vertices[i] = new Vertex(i + 1);
        }
    }

    public static Graph generateRandomGraph(int numVertices, long numEdges, int maxDistance) {
        Graph graph = new Graph(numVertices);
        Random random = new Random();

        int numEdgesPerVertex = (int) (numEdges / numVertices);
        int numEdgesPerVertexRemainder = (int) numEdges % numVertices;

        int[] adjacentVertices = new int[numVertices];
        for(int i = 0; i < numVertices; i++) {
            adjacentVertices[i] = i + 1;
        }
        int totalEdges = 0;
        for (int i = 1; i <= numVertices; i++) {
            if(i% 1000000 == 0) {
                System.out.println(i);
            }

            Vertex v = graph.getVertex(i);
            int maxIndex = adjacentVertices.length - 1;

            int edges = 0;
            if(numEdgesPerVertexRemainder > 0) {
                edges--;
                numEdgesPerVertexRemainder--;
            }

            while(edges < numEdgesPerVertex) {
                int randIndex = random.nextInt(maxIndex + 1);

                int w = adjacentVertices[randIndex];

                if(v.getIndex() != w) {

                    v.addToAdjList(w, random.nextInt(maxDistance) + 1);

                    totalEdges++;
                    edges++;
                    maxIndex--;
                }

                int temp = adjacentVertices[maxIndex];
                adjacentVertices[maxIndex] = adjacentVertices[randIndex];
                adjacentVertices[randIndex] = temp;
            }

        }

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
