package java.dataStructures.binaryHeap;

/**
 * Created by hristo on 12/10/16.
 */
public class BinaryHeapNode<T extends Comparable<T>> implements Comparable<BinaryHeapNode<T>>{

    private int arrayIndex;
    private T data;

    public BinaryHeapNode(int arrayIndex, T data) {
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
    public int compareTo(BinaryHeapNode<T> heapNode) {
        return data.compareTo(heapNode.getData());
    }
}
