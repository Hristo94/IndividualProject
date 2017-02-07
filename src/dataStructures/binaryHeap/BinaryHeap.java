package dataStructures.binaryHeap;

/**
 * Created by hristo on 12/10/16.
 */
public class BinaryHeap<T extends Comparable<T>> {

    private BinaryHeapNode<T> heap[]; // contains the objects in the heap
    private int last; // index of last element in heap
    private int capacity; // max number of elements in heap

    public BinaryHeap(int n) {
        capacity = n;
        heap = new BinaryHeapNode[capacity + 1];
        last = 0;
    }

    public int size() {
        return last;
    }
    public boolean isEmpty() {
        return last == 0;
    }

    public BinaryHeapNode<T> insert(T t) {
        BinaryHeapNode<T> binaryHeapNode = new BinaryHeapNode<>(last + 1, t);
        heap[last + 1] = binaryHeapNode;
        last++;

        upHeap();
        return binaryHeapNode;
    }

    public void upHeap(BinaryHeapNode<T> binaryHeapNode) {
        upHeap(binaryHeapNode.getArrayIndex());
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

    public BinaryHeapNode<T> removeMin() {

        BinaryHeapNode<T> min = heap[1];
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
        BinaryHeapNode<T> temp = heap[index];
        heap[index] = heap[parent];
        heap[index].setArrayIndex(index);
        heap[parent] = temp;
        heap[parent].setArrayIndex(parent);
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

    private BinaryHeapNode<T> leftChild(int i) {
        return heap[i * 2];
    }

    private BinaryHeapNode<T> rightChild(int i) {
        return heap[i * 2 + 1];
    }
}