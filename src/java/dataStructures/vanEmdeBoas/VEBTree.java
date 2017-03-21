package dataStructures.vanEmdeBoas;

import dataStructures.generic.DList;
import dataStructures.generic.Heap;
import graph.Vertex;

public class VEBTree implements Heap<Vertex>
{
    protected static int BASE_SIZE = 2; /* Base vEB Node size */
    protected static int NULL = -1; /* Initial min and max values */

    private DList[] values;

    private int lastRemoved = NULL;
    private VEBNode root;
    private int size = 0;

    private int maxDistance;

    public VEBTree(int maxDistance)
    {
        int universeSize = (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(maxDistance));
        this.maxDistance = maxDistance;

        // the actual vertices are stored in this bucket array of linked lists
        // vertices with the same distance map to the same bucket and are connected in a doubly linked list
        values = new DList[this.maxDistance + 1];

        for(int i = 0; i < values.length; i++) {
            values[i] = new DList();
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
        // the insertions are performed modulo maxDistance + 1
        // to allow the universe size to be equal to C. Otherwise it should be n * C
        int x = v.getDistance() % (maxDistance + 1);

        // insert the distance in the veb tree
        insertR(root, x, 1);

        // insert the vertex in the bucket array
        values[x].insert(v);

        size++;
    }

    public Vertex removeMin() {
        // the min vertex is the successor of the last deleted, not root.min
        // since we perform insertions in a circular fashion( modulo maxDistance + 1)
        int x = successor(root, lastRemoved - 1);
        if (x == NULL) {
            x = successor(root, NULL);
        }

        // update last deleted
        lastRemoved = x;

        // acquire a vertex of minimum distance from the linked list
        Vertex minVertex = values[x].poll();

        // delete the distance from the veb tree
        deleteR(root, x, 1);
        size --;

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

        // if x is the new min, swap with the previous min
        if(x < node.min)
        {
            int tempValue = x;
            x = node.min;
            node.min = tempValue;

            int tempMinCount = node.minCount;
            node.minCount = n;
            n = tempMinCount;
        }

        // since we don't want to lose the value of the previous min
        // it has to be stored in one of x's clusters as element low(x) = x % node.lowerSquareRoot
        if(x > node.min && node.universeSize > BASE_SIZE)
        {
            int c = x / node.lowerSquareRoot; // determines which cluster
            int i = x % node.lowerSquareRoot; // determines where within the cluster

            if(node.cluster[c].isEmpty())
            {
                // if the cluster was previously empty, the summary has to be updated that it is no longer empty
                insertR(node.summary, c, 1);
                insertEmpty(node.cluster[c], i , n);
            }
            else
            {
                insertR(node.cluster[c], i, n);
            }
        }
        // if x is the new max, update it
        if(x > node.max)
        {
            node.max = x;
            node.maxCount = n;
        }
    }

    private int successor(VEBNode node, int x) {

        // if the base universe, return the next available value
        if(node.universeSize == BASE_SIZE) {
            if(x == 0 && node.max == 1) {
                return 1;
            }
            return NULL;
        }

        if(node.min != NULL && x < node.min) {
            return node.min;
        }

        // if its not in the root node it must be in one of its clusters
        int c = x / node.lowerSquareRoot; // which cluster
        int i = x % node.lowerSquareRoot; // where within the cluster

        int max_low = node.cluster[c].max; // the max element within x's cluster
        if(i < max_low) { // if x's cluster contains element bigger than x, than the successor lies there
            int offset = successor(node.cluster[c], i);
            return index(node, c, offset);
        }

        // otherwise find successive clusters
        int succ_cluster = successor(node.summary, c);

        // if there are no more non-empty clusters, there is no successor
        if(succ_cluster == NULL) {
            return NULL;
        }

        // find the successor within the successive cluster
        int offset = node.cluster[succ_cluster].min;
        return index(node,succ_cluster,offset);
    }

    private void deleteR(VEBNode node, int x, int n)
    {
        // if only one element in the tree,
        // check if its not duplicated, if it is just decrement the counter
        // if there is no duplicate, just set everything to NULL to indiciate the tree is empty
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

        // if base case, possible values are only 0 and 1
        if(BASE_SIZE == node.universeSize)
        {
            // check if it is 1 or 0
            // check if it is not duplicated
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

            return;
        }


        int next_n = n;
        // if x is the minimum element, the min has to be set to a new value once x is removed
        if(x == node.min) {
            // if x is duplicated, simply decrement the counter and return
            if (node.minCount > n) {
                node.minCount -= n;
                return;
            }

            // use the summary to find the first non empty cluster
            int summaryMin = node.summary.min;
            // find the smallest value in the first non empty cluster, this will be the new min
            x = index(node, summaryMin, node.cluster[summaryMin].min);
            node.min = x;
            node.minCount = node.cluster[summaryMin].minCount;
            next_n = node.cluster[summaryMin].minCount;
        }

        // since we updated min to a new value, it has to be removed from its cluster since min occurs only once in the tree
        int c = x / node.lowerSquareRoot; // locate the cluster
        int i = x % node.lowerSquareRoot; // locate where in the cluster

        deleteR(node.cluster[c], i, next_n); // remove it

        if(NULL == node.cluster[c].min) // if the cluster became empty
        {
            deleteR(node.summary, c, 1); // update the summary to indicate that the cluster is now empty
            if(x == node.max) // max has to be updated to a new value if we are deleting max
            {
                // if max is duplicated just decrement
                if(node.max == node.min) {
                    node.maxCount = node.minCount;
                    return;
                }
                node.maxCount -= n;
                // otherwise find the next max using the summary, to acquire the cluster that contains the new max
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
        //update max if we are deleting it
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

    private int index(VEBNode node, int x, int y)
    {
        return (x * node.lowerSquareRoot + y);
    }
}