/**
 * Represents a union-find data structure using quick find.
 * <p>
 * Initializing a data structure with <em>N</em> objects takes linear time.
 * Afterwards, <em>find</em>, <em>connected</em>, and <em>count</em> takes O(1)
 * time but <em>union</em> takes O(N) time.
 * <p>
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * 
 * @author jforbes
 *
 */

public class QuickFind implements IUnionFind {
	private int[] myID;
	private int myComponents;

	/**
	 * Default constructor
	 */
	public QuickFind() {
		myID = null;
		myComponents = 0;
	}

	/**
	 * Constructor that creates N isolated components
	 */
	public QuickFind(int N) {
		initialize(N);
	}

	// instantiate N isolated components 0 through N-1
	public void initialize(int n) {
		myComponents = n;
		myID = new int[n];
		for (int i = 0; i < n; i++) {
			myID[i] = i;
		}
	}

	// return number of connected components
	public int components() {
		return myComponents;
	}

	// return id of component corresponding to element x
	public int find(int x) {
		// TODO: add IndexOutOfBoundsException
		return myID[x];
//		int n = myID.length;
//        if (x < 0 || x >= n) 
//            throw new IndexOutOfBoundsException("index " + x + " is not between 0 and " + (n-1));
//		// this method finds the root of x 
//		while (x != myID[x])
//			x = myID[x];
//		return x;
	}

	// are elements p and q in the same component?
	public boolean connected(int p, int q) {
		return myID[p] == myID[q];
		
		// this method determines if roots are equivalent
//		return find(p) == find(q);
	}

	// merge components containing p and q
	public void union(int p, int q) {
		if (connected(p, q))
			return;
		int pid = myID[p];
		for (int i = 0; i < myID.length; i++)
			if (myID[i] == pid)
				myID[i] = myID[q];
		myComponents -= 1;
//		int pRoot = find(p);
//		int qRoot = find(q);
//		if (pRoot == qRoot)
//			return;
//		myID[pRoot] = qRoot;
//		myComponents--;
	}
}
