package dataStructures.vanEmdeBoas;

public class VEBNode
{
    public int universeSize;
    public int min;
    public int max;
    public VEBNode summary;
    public VEBNode[] cluster;

    public int minCount = 0;
    public int maxCount = 0;

    public int lowerSquareRoot;

    // the contstructor creates VEB nodes recursively until the whole tree is constructed
    public VEBNode(int universeSize)
    {
        this.universeSize = universeSize;
        // precompute the lowerSquareRoot since it is needed during the veb tree operations
        this.lowerSquareRoot = (int) Math.pow(2, Math.floor((Math.log(universeSize) / Math.log(2)) / 2.0));
        min = VEBTree.NULL;
        max = VEBTree.NULL;

		/* Allocate the summary and cluster children. */
        initializeChildren(universeSize);
    }

    private void initializeChildren(int universeSize)
    {
        if(universeSize <= VEBTree.BASE_SIZE)
        {
            summary = null;
            cluster = null;
        }
        else
        {
            int childUnivereSize = higherSquareRoot();

            summary = new VEBNode(childUnivereSize);
            cluster = new VEBNode[childUnivereSize];

            for(int i = 0; i < childUnivereSize; i++)
            {
                cluster[i] = new VEBNode(childUnivereSize);
            }
        }
    }

    public boolean isEmpty() {
        return min == VEBTree.NULL;
    }

    private int higherSquareRoot()
    {
        return (int)Math.pow(2, Math.ceil((Math.log(universeSize) / Math.log(2)) / 2));
    }
}