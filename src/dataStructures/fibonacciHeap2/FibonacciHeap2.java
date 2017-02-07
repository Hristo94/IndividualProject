package dataStructures.fibonacciHeap2;

public class FibonacciHeap2<T> {

    private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
    private FibonacciHeapNode2<T> minNode;
    private int nNodes;

    public FibonacciHeap2() {}

    public boolean isEmpty() {
        return minNode == null;
    }

    public void decreaseKey(FibonacciHeapNode2<T> x, double k) {

        if (k > x.key) {
            throw new IllegalArgumentException(
                    "decreaseKey() got larger key value");
        }

        x.key = k;

        FibonacciHeapNode2<T> y = x.parent;

        if ((y != null) && (x.key < y.key)) {
            cut(x, y);
            cascadingCut(y);
        }

        if (x.key < minNode.key) {
            minNode = x;
        }

    }

    public void delete(FibonacciHeapNode2<T> x) {

        decreaseKey(x, Double.NEGATIVE_INFINITY);
        removeMin();

    }


    public void insert(FibonacciHeapNode2<T> node, double key) {
        node.key = key;

        if (minNode != null) {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            if (key < minNode.key) {
                minNode = node;
            }
        } else {
            minNode = node;
        }

        nNodes++;
    }

    public FibonacciHeapNode2<T> removeMin() {
        FibonacciHeapNode2<T> z = minNode;

        if (z != null) {
            int numKids = z.degree;
            FibonacciHeapNode2<T> x = z.child;
            FibonacciHeapNode2<T> tempRight;

            // for each child of z do...
            while (numKids > 0) {
                tempRight = x.right;

                // remove x from child list
                x.left.right = x.right;
                x.right.left = x.left;

                // add x to root list of heap
                x.left = minNode;
                x.right = minNode.right;
                minNode.right = x;
                x.right.left = x;

                // set parent[x] to null
                x.parent = null;
                x = tempRight;
                numKids--;
            }

            // remove z from root list of heap
            z.left.right = z.right;
            z.right.left = z.left;

            if (z == z.right) {
                minNode = null;
            } else {
                minNode = z.right;
                consolidate();
            }

            // decrement size of heap
            nNodes--;
        }

        return z;
    }

    protected void cascadingCut(FibonacciHeapNode2<T> y) {
        FibonacciHeapNode2<T> z = y.parent;

        // if there's a parent...
        if (z != null) {
            // if y is unmarked, set it marked
            if (!y.mark) {
                y.mark = true;
            } else {
                // it's marked, cut it from parent
                cut(y, z);
                // cut its parent as well
                cascadingCut(z);
            }
        }
    }

    protected void consolidate() {

        int arraySize = ((int) Math.floor(Math.log(nNodes)*oneOverLogPhi))+1;

        FibonacciHeapNode2<T>[] array =
                new FibonacciHeapNode2[arraySize];

        // Find the number of root nodes.
        int numRoots = 0;
        FibonacciHeapNode2<T> x = minNode;

        if (x != null) {
            numRoots++;
            x = x.right;

            while (x != minNode) {
                numRoots++;
                x = x.right;
            }
        }

        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.degree;
            FibonacciHeapNode2<T> next = x.right;

            // ..and see if there's another of the same degree.
            for (;;) {
                FibonacciHeapNode2<T> y = array[d];
                if (y == null) {
                    // Nope.
                    break;
                }

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (x.key > y.key) {
                    FibonacciHeapNode2<T> temp = y;
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
            FibonacciHeapNode2<T> y = array[i];
            if (y == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (minNode != null) {
                // First remove node from root list.
                y.left.right = y.right;
                y.right.left = y.left;

                // Now add to root list, again.
                y.left = minNode;
                y.right = minNode.right;
                minNode.right = y;
                y.right.left = y;

                // Check if this is a new min.
                if (y.key < minNode.key) {
                    minNode = y;
                }
            } else {
                minNode = y;
            }
        }

    }

    protected void cut(FibonacciHeapNode2<T> x, FibonacciHeapNode2<T> y) {

        // remove x from childlist of y and decrement degree[y]
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        // reset y.child if necessary
        if (y.child == x) {
            y.child = x.right;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        // add x to root list of heap
        x.left = minNode;
        x.right = minNode.right;
        minNode.right = x;
        x.right.left = x;

        // set parent[x] to nil
        x.parent = null;

        // set mark[x] to false
        x.mark = false;

    }

    protected void link(FibonacciHeapNode2<T> y, FibonacciHeapNode2<T> x) {

        // remove y from root list of heap
        y.left.right = y.right;
        y.right.left = y.left;

        // make y a child of x
        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        // increase degree[x]
        x.degree++;

        // set mark[y] false
        y.mark = false;

    }

}