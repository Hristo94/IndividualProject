package graph;

import dataStructures.interfaces.Heap;

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

    public int size() {
        return vertices.length;
    }

    public Vertex getVertex(int i) {
        return vertices[i - 1];
    }

    public void findShortestPath(int startVertex, Heap<Vertex> heap) {
        Vertex u = getVertex(startVertex);
        u.setDistance(0);
        heap.insert(u);

        while(!heap.isEmpty()){
            // find v not in S with d(v) minimum;
            Vertex v = heap.removeMin();
            v.setProcessed(true); // shortest path to 'v' is already known

            // perform restoreHeapProperty for each vertex 'w' adjacent to 'v'
            for(AdjListNode node: v.getAdjList()){
                Vertex w = getVertex(node.getVertexNumber());
                if(!w.getProcessed()) {
                    int newDistance = v.getDistance() + node.getWeight();
                    if(newDistance < w.getDistance()){
                        w.setDistance(newDistance);
                        w.setPredecessor(v.getIndex());

                        heap.restoreHeapProperty(w);
                    }
                }
            }
        }
    }
}
