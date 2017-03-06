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
    public VEBNode(int universeSize)
    {
        this.universeSize = universeSize;
        this.lowerSquareRoot = (int) Math.pow(2, Math.floor((Math.log10(universeSize) / Math.log10(2)) / 2.0));
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
    /*
     * Returns the value of the most significant bits of x.
     */
    private int higherSquareRoot()
    {
        return (int)Math.pow(2, Math.ceil((Math.log10(universeSize) / Math.log10(2)) / 2));
    }
}