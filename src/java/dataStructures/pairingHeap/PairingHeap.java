package dataStructures.pairingHeap;

import java.util.EmptyStackException;

public class PairingHeap<T extends Comparable<T>>
{
    private PairingHeapNode<T> root;
    // The tree array for combineSiblings
    private PairingHeapNode<T>[ ] treeArray = new PairingHeapNode[ 5 ];

    public PairingHeap( ) {
        root = null;
    }

    public PairingHeapNode<T> insert(T element ) {
        PairingHeapNode node = new PairingHeapNode<>(element);

        if( root == null )
            root = node;
        else
            root = compareAndLink( root, node );
        return node;
    }


    public T findMin() {
        if(isEmpty()) {
            throw new EmptyStackException();
        }
        return root.element;
    }


    public T deleteMin( ) {
        if(isEmpty()) {
            throw new EmptyStackException();
        }

        T minElement = findMin();
        if(root.leftChild == null) {
            root = null;
        }
        else {
            root = combineSiblings(root.leftChild);
        }
        return minElement;
    }


    public void decreaseKey(PairingHeapNode p, T newElement ) {
        if(p == null) {
            throw new IllegalArgumentException("null passed to decreaseKey");
        }

        if( p.element.compareTo(newElement) < 0 ) {
            throw new IllegalArgumentException("new element is bigger than old element");
        }

        p.element = newElement;
        if(p != root) {
            if(p.nextSibling != null) {
                p.nextSibling.prev = p.prev;
            }
            if(p.prev.leftChild == p) {
                p.prev.leftChild = p.nextSibling;
            }
            else {
                p.prev.nextSibling = p.nextSibling;
            }
            p.nextSibling = null;
            root = compareAndLink( root, p );
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    private PairingHeapNode<T> compareAndLink(PairingHeapNode<T> first, PairingHeapNode<T> second )
    {
        if( second == null )
            return first;

        if( second.element.compareTo( first.element ) < 0 )
        {
            // Attach first as leftmost child of second
            second.prev = first.prev;
            first.prev = second;
            first.nextSibling = second.leftChild;
            if(first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.leftChild = first;
            return second;
        }
        else
        {
            // Attach second as leftmost child of first
            second.prev = first;
            first.nextSibling = second.nextSibling;
            if(first.nextSibling != null) {
                first.nextSibling.prev = first;
            }
            second.nextSibling = first.leftChild;
            if(second.nextSibling != null) {
                second.nextSibling.prev = second;
            }
            first.leftChild = second;
            return first;
        }
    }

    private PairingHeapNode<T>[] doubleIfFull(PairingHeapNode<T>[] array, int index )
    {
        if(index == array.length) {
            PairingHeapNode[] oldArray = array;

            array = new PairingHeapNode[ index * 2 ];
            for(int i = 0; i < index; i++) {
                array[i] = oldArray[i];
            }
        }
        return array;
    }

    private PairingHeapNode<T> combineSiblings(PairingHeapNode<T> firstSibling )
    {
        if(firstSibling.nextSibling == null) {
            return firstSibling;
        }
        // Store the subtrees in an array
        int numSiblings = 0;
        for( ; firstSibling != null; numSiblings++ ) {
            treeArray = doubleIfFull( treeArray, numSiblings );
            treeArray[ numSiblings ] = firstSibling;
            firstSibling.prev.nextSibling = null;  // break links
            firstSibling = firstSibling.nextSibling;
        }
        treeArray = doubleIfFull( treeArray, numSiblings );
        treeArray[ numSiblings ] = null;

        // Combine subtrees two at a time, going left to right
        int i = 0;
        for( ; i + 1 < numSiblings; i += 2 )
            treeArray[ i ] = compareAndLink( treeArray[ i ], treeArray[ i + 1 ] );

        int j = i - 2;

        // j has the result of lastRemoved compareAndLink.
        // If an odd number of trees, get the lastRemoved one.
        if( j == numSiblings - 3 )
            treeArray[ j ] = compareAndLink( treeArray[ j ], treeArray[ j + 2 ] );

        // Now go right to left, merging lastRemoved tree with
        // next to lastRemoved. The result becomes the new lastRemoved.
        for( ; j >= 2; j -= 2 )
            treeArray[ j - 2 ] = compareAndLink( treeArray[ j - 2 ], treeArray[ j ] );

        return treeArray[ 0 ];
    }

}