package dataStructures.fibonacciHeap;
public class FibonacciHeap<T>
{
    // used for the formula determining the upper bound of a degree array
    private static final double ONEOVERLOGPHI = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
    private FibonacciHeapNode<T> minNode;
    private int numNodes;

    public FibonacciHeap() {
        minNode = null;
        numNodes = 0;
    }


    public boolean isEmpty()
    {
        return minNode == null;
    }

    public void insert(FibonacciHeapNode<T> node, int key)
    {
        node.setKey(key);

        // add the node to the root list
        if (minNode != null) {
            node.setLeft(minNode);
            node.setRight(minNode.getRight());
            minNode.setRight(node);
            node.getRight().setLeft(node);

            if (key < minNode.getKey()) {
                minNode = node;
            }
        } else {
            minNode = node;
        }

        numNodes++;
    }

    public FibonacciHeapNode<T> removeMin()
    {
        FibonacciHeapNode<T> min = minNode; // save minNode in a variable to be returned

        if (min != null) {
            int numKids = min.getDegree();
            FibonacciHeapNode<T> x = min.getChild();
            FibonacciHeapNode<T> tempRight;

            // add each of min's children to the root list
            while (numKids > 0) {
                tempRight = x.getRight();

                // remove x from child list
                x.getLeft().setRight(x.getRight());
                x.getRight().setLeft(x.getLeft());

                // add x to root list of heap
                x.setLeft(minNode);
                x.setRight(minNode.getRight());
                minNode.setRight(x);
                x.getRight().setLeft(x);

                // set parent[x] to null
                x.setParent(null);
                x = tempRight;
                numKids--;
            }

            // remove min from root list of heap
            min.getLeft().setRight(min.getRight());
            min.getRight().setLeft(min.getLeft());

            // perform consolidate to reduce the number of trees in the heap
            if (min == min.getRight()) {
                minNode = null;
            } else {
                minNode = min.getRight();
                consolidate();
            }

            // decrement size of heap
            numNodes--;
        }

        return min;
    }

    public void decreaseKey(FibonacciHeapNode<T> x, int newKey)
    {
        // update the key
        x.setKey(newKey);

        FibonacciHeapNode<T> y = x.getParent();

        // if the heap property is violated after the update
        // cut x from its parent and add it to the root list
        // this might involve structural changes in y as well
        // since x might be the second child that y loses.
        // thats why cascading-cut is performed
        if ((y != null) && (x.getKey() < y.getKey())) {
            cut(x, y);
            cascadingCut(y);
        }

        // update min in the root list if necessary
        if (x.getKey() < minNode.getKey()) {
            minNode = x;
        }
    }

    protected void cascadingCut(FibonacciHeapNode<T> y)
    {
        FibonacciHeapNode<T> z = y.getParent();

        // continue cutting the node from its parent, until an unmarked node is reached or a root
        if (z != null) {
            // if y is unmarked, set it marked
            if (!y.isMarked()) {
                y.setMarked(true);
            } else {
                // it's marked, cut it from parent
                cut(y, z);

                // cut its parent as well
                cascadingCut(z);
            }
        }
    }

    protected void consolidate()
    {
        // calculate the size of the degree array using the upper bound formula
        int arraySize = ((int) Math.floor(Math.log(numNodes) * ONEOVERLOGPHI)) + 1;

        FibonacciHeapNode<T>[] array = new FibonacciHeapNode[arraySize];

        // Find the number of root nodes.
        int numRoots = 0;
        FibonacciHeapNode<T> x = minNode;

        if (x != null) {
            numRoots++;
            x = x.getRight();

            while (x != minNode) {
                numRoots++;
                x = x.getRight();
            }
        }

        // For each node in root list
        while (numRoots > 0) {
            int d = x.getDegree();
            FibonacciHeapNode<T> next = x.getRight();

            // find another node with the same degree
            for (;;) {
                FibonacciHeapNode<T> y = array[d];
                if (y == null) {
                    // stop if there is no such a node
                    break;
                }

                // it is required that x is a root after the consolidate
                // so swap x and y if x's key is bigger
                if (x.getKey() > y.getKey()) {
                    FibonacciHeapNode<T> temp = y;
                    y = x;
                    x = temp;
                }

                // make y a child of x
                link(y, x);

                // Find nodes with the same degree as the new degree of x
                array[d] = null;
                d++;
            }

            // Indicate that x is the unique element with that degree
            array[d] = x;

            // continue with next nodes in the root list
            x = next;
            numRoots--;
        }


        // recreate the root list from the degree array
        minNode = null;
        for (int i = 0; i < arraySize; i++) {
            FibonacciHeapNode<T> y = array[i];
            // skip if there is no node with this degree
            if (y == null) {
                continue;
            }

            // add to the root list
            if (minNode != null) {
                y.getLeft().setRight(y.getRight());
                y.getRight().setLeft(y.getLeft());

                y.setLeft(minNode);
                y.setRight(minNode.getRight());
                minNode.setRight(y);
                y.getRight().setLeft(y);

                // Check if this is a new min.
                if (y.getKey() < minNode.getKey()) {
                    minNode = y;
                }
            } else {
                minNode = y;
            }
        }
    }

    protected void cut(FibonacciHeapNode<T> x, FibonacciHeapNode<T> y)
    {
        // remove x from the children of y
        x.getLeft().setRight(x.getRight());
        x.getRight().setLeft(x.getLeft());
        y.setDegree(y.getDegree() - 1);

        // reset y.child if necessary
        if (y.getChild() == x) {
            y.setChild(x.getRight());
        }

        if (y.getDegree() == 0) {
            y.setChild(null);
        }

        // add x to root list
        x.setLeft(minNode);
        x.setRight(minNode.getRight());
        minNode.setRight(x);
        x.getRight().setLeft(x);

        // reset the x's variables
        x.setParent(null);
        x.setMarked(false);
    }

    protected void link(FibonacciHeapNode<T> y, FibonacciHeapNode<T> x)
    {
        // remove y from root list
        y.getLeft().setRight(y.getRight());
        y.getRight().setLeft(y.getLeft());

        // make y a child of x
        y.setParent(x);

        if (x.getChild() == null) {
            x.setChild(y);
            y.setRight(y);
            y.setLeft(y);
        } else {
            y.setLeft(x.getChild());
            y.setRight(x.getChild().getRight());
            x.getChild().setRight(y);
            y.getRight().setLeft(y);
        }

        // increase the degree of x
        x.setDegree(x.getDegree() + 1);
        // y becomes unmarked
        y.setMarked(false);
    }
}

