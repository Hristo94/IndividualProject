package dataStructures.binomialHeap;

import java.util.Random;

/**
 * Created by hristo on 12/10/16.
 */
public class BinomialHeap<T extends Comparable<T>> {

    private BinomialHeapNode<T> heap[]; // contains the objects in the heap
    private int last; // index of last element in heap
    private int capacity; // max number of elements in heap

    public BinomialHeap(int n) {
        capacity = n;
        heap = new BinomialHeapNode[capacity + 1];
        last = 0;
    }

    public boolean isEmpty() {
        return last == 0;
    }

    public BinomialHeapNode<T> insert(T t) {
        BinomialHeapNode<T> binomialHeapNode = new BinomialHeapNode<>(last + 1, t);
        heap[last + 1] = binomialHeapNode;
        last++;

        upHeap();
        return binomialHeapNode;
    }

    public void upHeap(BinomialHeapNode<T> binomialHeapNode) {
        upHeap(binomialHeapNode.getArrayIndex());
    }

    private void upHeap(int index) {
        int parentIndex = getParentIndex(index);

        while(parentIndex != -1 && heap[parentIndex].compareTo(heap[index]) > 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    private void upHeap() {
        upHeap(last);
    }

    public BinomialHeapNode<T> removeMin() {

        BinomialHeapNode<T> min = heap[1];
        heap[1] = heap[last];
        last --;

        downHeap();
        return min;
    }


    private void downHeap() {
        int index = 1;
        int smaller = smallerChild(index);

        while(smaller != -1 && heap[index].compareTo(heap[smaller]) > 0) {
            swap(index, smaller);
            index = smaller;
            smaller = smallerChild(index);
        }
    }


    private void swap(int index, int parent) {
        BinomialHeapNode<T> temp = heap[index];
        heap[index] = heap[parent];
        heap[index].setArrayIndex(parent);
        heap[parent] = temp;
        heap[parent].setArrayIndex(temp.getArrayIndex());
    }

    private int smallerChild (int i) {
        if(! hasLeft(i)){
            return -1;
        }
        if(hasRight(i) && leftChild(i).compareTo(rightChild(i)) > 0){
            return i*2 +1;
        }
        return i*2;

    }
    private boolean hasLeft(int i) {
        return (i * 2) <= last;
    }

    private boolean hasRight(int i) {
        return ((i * 2) + 1) <= last;
    }

    private int getParentIndex(int i) {
        if(i <= 1){
            return -1;
        }
        return i / 2;
    }

    private BinomialHeapNode<T> leftChild(int i) {
        return heap[i * 2];
    }

    private BinomialHeapNode<T> rightChild(int i) {
        return heap[i * 2 + 1];
    }
}