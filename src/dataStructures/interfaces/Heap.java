package dataStructures.interfaces;

/**
 * Created by hristo on 11/10/16.
 */
public interface Heap<V> {

    V removeMin();

    boolean isEmpty();

    void insert(V vertex);

    void restoreHeapProperty(V v);
}
