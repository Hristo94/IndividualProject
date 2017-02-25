package dataStructures.vanEmdeBoas;

import dataStructures.interfaces.Heap;
import graph.Vertex;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.HashSet;

public class VEBTree implements Heap<Vertex>
{
    protected static int BASE_SIZE = 2; /* Base vEB Node size */
    protected static int NULL = -1; /* Initial min and max values */

    private ObjectOpenHashSet<Vertex>[] values;

    private int lastRemoved = NULL;
    private VEBNode root;
    private int size = 0;

    private int maxDistance;

    public static VEBTree createVEBTree(int maxDistance)
    {
        // calculates the universe size from the max distance by finding the nearest power of 2, bigger than the maxDistance
        int universeSize = (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(maxDistance - 1));
        if(isPowerOf2(universeSize))
        {
            return new VEBTree(maxDistance, universeSize);
        }
        else
        {
            System.out.println("ERROR: Must create a tree with size a power of 2!");
            return null;
        }
    }

    private VEBTree(int maxDistance, int universeSize)
    {
        this.maxDistance = maxDistance;

        values = new ObjectOpenHashSet[this.maxDistance + 1];

        for(int i = 0; i < values.length; i++) {
            values[i] = new ObjectOpenHashSet<>();
        }

        root = new VEBNode(universeSize);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void decreaseKey(Vertex v, int newDistance) {
        delete(v);

        v.setDistance(newDistance);

        insert(v);
    }

    public void insert(Vertex v)
    {
        int x = v.getDistance() % (maxDistance + 1);
        insertR(root, x, 1);

        values[x].add(v);
        size++;
    }

    public Vertex removeMin() {
        int x = successor(root, lastRemoved - 1);
        if (x == NULL) {
            x = successor(root, NULL);
        }

        lastRemoved = x;

        Vertex minVertex = values[x].iterator().next();

        delete(minVertex);

        return minVertex;
    }

    private void delete(Vertex v)
    {
        int x = v.getDistance() % (maxDistance + 1);
        values[x].remove(v);
        size --;

        deleteR(root, x, 1);
    }

    private void insertEmpty(VEBNode node, int x, int n) {
        node.min = x;
        node.max = x;
        node.minCount = node.maxCount = n;
    }

    private void insertR(VEBNode node, int x, int n)
    {
        if(node.isEmpty())
        {
            insertEmpty(node, x, n);
            return;
        }

        if(node.max == x) {
            node.maxCount += n;
        }

        if(node.min == x) {
            node.minCount += n;
            return;
        }

        if(x < node.min)
        {
            int tempValue = x;
            x = node.min;
            node.min = tempValue;

            int tempMinCount = node.minCount;
            node.minCount = n;
            n = tempMinCount;
        }

        if(x > node.min && node.universeSize > BASE_SIZE)
        {
            int c = x / node.lowerSquareRoot;
            int i = x % node.lowerSquareRoot;

            if(node.cluster[c].isEmpty())
            {
                insertR(node.summary, c, 1);
                insertEmpty(node.cluster[c], i , n);
            }
            else
            {
                insertR(node.cluster[c], i, n);
            }
        }
        if(x > node.max)
        {
            node.max = x;
            node.maxCount = n;
        }
    }

    private int successor(VEBNode node, int x) {

        if(node.universeSize == BASE_SIZE) {
            if(x == 0 && node.max == 1) {
                return 1;
            }
            return NULL;
        }

        if(node.min != NULL && x < node.min) {
            return node.min;
        }

        int c = x / node.lowerSquareRoot;
        int i = x % node.lowerSquareRoot;

        int max_low = node.cluster[c].max;
        if(i < max_low) {
            int offset = successor(node.cluster[c], i);
            return index(node, c, offset);
        }

        int succ_cluster = successor(node.summary, c);

        if(succ_cluster == NULL) {
            return NULL;
        }

        int offset = node.cluster[succ_cluster].min;
        return index(node,succ_cluster,offset);
    }

    private void deleteR(VEBNode node, int x, int n)
    {
        if(node.min == node.max)
        {
            if(node.min == NULL || node.minCount == n) {
                node.min = node.max = NULL;
                node.minCount = 0;
            }
            else {
                node.minCount -= n;
            }
            node.maxCount = node.minCount;
            return;
        }

        if(BASE_SIZE == node.universeSize)
        {
            if(0 == x )
            {
                node.minCount -= n;
                if(node.minCount == 0) {
                    node.min = 1;
                    node.minCount = node.maxCount;
                }
            }
            else
            {
                node.maxCount -= n;
                if(node.maxCount == 0) {
                    node.max = 0;
                    node.maxCount = node.minCount;
                }
            }
            // node.max = node.min; might need it
            return;
        }


        int next_n = n;
        if(x == node.min) {
            if (node.minCount > n) {
                node.minCount -= n;
                return;
            }

            int summaryMin = node.summary.min;
            x = index(node, summaryMin, node.cluster[summaryMin].min);
            node.min = x;
            node.minCount = node.cluster[summaryMin].minCount;
            next_n = node.cluster[summaryMin].minCount;
        }

        int c = x / node.lowerSquareRoot;
        int i = x % node.lowerSquareRoot;

        deleteR(node.cluster[c], i, next_n);

        if(NULL == node.cluster[c].min)
        {
            deleteR(node.summary, c, 1);
            if(x == node.max)
            {
                if(node.max == node.min) {
                    node.maxCount = node.minCount;
                    return;
                }
                node.maxCount -= n;
                if(node.maxCount == 0) {
                    int summaryMax = node.summary.max;
                    if(NULL == summaryMax)
                    {
                        node.max = node.min;
                        node.maxCount = node.minCount;
                    }
                    else
                    {
                        node.max = index(node, summaryMax, node.cluster[summaryMax].max);
                        node.maxCount = node.cluster[summaryMax].maxCount;
                    }
                }
            }
        }
        else if(x == node.max)
        {
            if(node.max == node.min) {
                node.maxCount = node.minCount;
                return;
            }
            node.maxCount -= n;
            if(node.maxCount == 0) {
                node.max = index(node, c, node.cluster[c].max);
                node.maxCount = node.cluster[c].maxCount;
            }
        }
    }
    

    /*
     * Returns the index in the tree of the given value.
     */
    private int index(VEBNode node, int x, int y)
    {
        return (x * node.lowerSquareRoot + y);
    }


    /*
     * Returns true if x is a power of 2, false otherwise.
     */
    private static boolean isPowerOf2(int x)
    {
        if(0 == x)
        {
            return false;
        }

        while(x % 2 == 0)
        {
            x = x / 2;
        }

        if(x > 1)
        {
            return false;
        }

        return true;
    }
}