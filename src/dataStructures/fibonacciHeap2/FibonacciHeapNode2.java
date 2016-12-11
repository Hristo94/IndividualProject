package dataStructures.fibonacciHeap2;

/**
 * Created by hristo on 11/12/2016.
 */
public class FibonacciHeapNode2<T> {

    T data;
    FibonacciHeapNode2<T> child;
    FibonacciHeapNode2<T> left;
    FibonacciHeapNode2<T> parent;
    FibonacciHeapNode2<T> right;

    boolean mark;
    double key;
    int degree;

    public FibonacciHeapNode2(T data, double key) {

        this.right = this;
        this.left = this;
        this.data = data;
        this.key = key;

    }

    public final double getKey() {
        return key;
    }

    public final T getData() {
        return data;
    }
}