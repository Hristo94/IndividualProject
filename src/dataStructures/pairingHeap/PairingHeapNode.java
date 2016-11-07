package dataStructures.pairingHeap;

/**
 * Created by hristo on 07/11/2016.
 */
public class PairingHeapNode<T extends Comparable<T>>
{
    // Friendly data; accessible by other package routines
    public T element;
    public PairingHeapNode<T> leftChild;
    public PairingHeapNode<T> nextSibling;
    public PairingHeapNode<T> prev;
    /**
     * Construct the PairingHeapNode.
     * @param theElement the value stored in the node.
     */
    public PairingHeapNode(T theElement )
    {
        element     = theElement;
        leftChild   = null;
        nextSibling = null;
        prev        = null;
    }

    /**
     * Returns the value stored at this position.
     * @return the value stored at this position.
     */
    public T getValue( )
    {
        return element;
    }

}
