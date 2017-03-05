package java.dataStructures.fibonacciHeap;
public class FibonacciHeap<T>
{
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

    public void decreaseKey(FibonacciHeapNode<T> x, int newKey)
    {
        if (newKey > x.getKey()) {
            throw new IllegalArgumentException(
                "decreaseKey() got larger key value. Current key: " + x.getKey() + " new key: " + newKey);
        }

        x.setKey(newKey);

        FibonacciHeapNode<T> y = x.getParent();

        if ((y != null) && (x.getKey() < y.getKey())) {
            cut(x, y);
            cascadingCut(y);
        }

        if (x.getKey() < minNode.getKey()) {
            minNode = x;
        }
    }

    public void insert(FibonacciHeapNode<T> node, int key)
    {
        node.setKey(key);

        // concatenate node into min list
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
        FibonacciHeapNode<T> z = minNode;

        if (z != null) {
            int numKids = z.getDegree();
            FibonacciHeapNode<T> x = z.getChild();
            FibonacciHeapNode<T> tempRight;

            // for each child of z do...
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

            // remove z from root list of heap
            z.getLeft().setRight(z.getRight());
            z.getRight().setLeft(z.getLeft());

            if (z == z.getRight()) {
                minNode = null;
            } else {
                minNode = z.getRight();
                consolidate();
            }

            // decrement size of heap
            numNodes--;
        }

        return z;
    }

    protected void cascadingCut(FibonacciHeapNode<T> y)
    {
        FibonacciHeapNode<T> z = y.getParent();

        // if there's a parent...
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

        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.getDegree();
            FibonacciHeapNode<T> next = x.getRight();

            // ..and see if there's another of the same degree.
            for (;;) {
                FibonacciHeapNode<T> y = array[d];
                if (y == null) {
                    // Nope.
                    break;
                }

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (x.getKey() > y.getKey()) {
                    FibonacciHeapNode<T> temp = y;
                    y = x;
                    x = temp;
                }

                // FibonacciHeapNode2<T> y disappears from root list.
                link(y, x);

                // We've handled this degree, go to next one.
                array[d] = null;
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array[d] = x;

            // Move forward through list.
            x = next;
            numRoots--;
        }

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        minNode = null;

        for (int i = 0; i < arraySize; i++) {
            FibonacciHeapNode<T> y = array[i];
            if (y == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (minNode != null) {
                // First remove node from root list.
                y.getLeft().setRight(y.getRight());
                y.getRight().setLeft(y.getLeft());

                // Now add to root list, again.
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
        // remove x from childlist of y and decrement degree[y]
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

        // add x to root list of heap
        x.setLeft(minNode);
        x.setRight(minNode.getRight());
        minNode.setRight(x);
        x.getRight().setLeft(x);

        // set parent[x] to nil
        x.setParent(null);

        // set isMarked[x] to false
        x.setMarked(false);
    }

    protected void link(FibonacciHeapNode<T> y, FibonacciHeapNode<T> x)
    {
        // remove y from root list of heap
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

        // increase degree[x]
        x.setDegree(x.getDegree() + 1);

        // set isMarked[y] false
        y.setMarked(false);
    }
}

