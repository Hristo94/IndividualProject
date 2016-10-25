package dataStructures.binomialHeap;

/**
 * Created by hristo on 12/10/16.
 */
public class BinomialHeapNode<T extends Comparable<T>> implements Comparable<BinomialHeapNode<T>>{

    private int arrayIndex;
    private T data;

    public BinomialHeapNode(int arrayIndex, T data) {
        this.arrayIndex = arrayIndex;
        this.data = data;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public T getData() {
        return data;
    }

    @Override
    public int compareTo(BinomialHeapNode<T> heapNode) {
        return data.compareTo(heapNode.getData());
    }
}
