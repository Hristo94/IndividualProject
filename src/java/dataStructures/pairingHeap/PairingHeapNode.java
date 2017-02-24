package dataStructures.pairingHeap;

/**
 * Created by hristo on 07/11/2016.
 */
public class PairingHeapNode<T extends Comparable<T>>
{
    public T element;
    public PairingHeapNode<T> leftChild;
    public PairingHeapNode<T> nextSibling;
    public PairingHeapNode<T> prev;

    public PairingHeapNode(T element )
    {
        this.element = element;
        leftChild   = null;
        nextSibling = null;
        prev        = null;
    }
}
