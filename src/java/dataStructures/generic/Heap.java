package dataStructures.generic;


// Interface specifying the operations that the Dijkstra's algorithm requires
// Each particular implementation must implement this interface so that it can be passed
// to the Dijkstra's algorithm
public interface Heap<V> {

    V removeMin();

    boolean isEmpty();

    void insert(V vertex);

    void decreaseKey(V v, int newKey);
}
