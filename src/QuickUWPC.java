/*
 * This code was largely adapted from the course online textbook, Algorithms, 4th Edition. 
 */

public class QuickUWPC implements IUnionFind {

	private int[] parent;
	private int[] size;
	private int count;
	
	public QuickUWPC(int N){
		initialize(N);
	}
	
	
	@Override
	public void initialize(int n) {
        count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
        	parent[i] = i;
            size[i] = 1;	
        }
  
	}
	private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n-1));  
        }
    }

	@Override
	public int components() {
		return count;
	}

	@Override
	public int find(int x) {
		validate(x);
        int root = x;
        while (root != parent[root])
            root = parent[root];
        while (x != root) {
            int newp = parent[x];
            parent[x] = root;
            x = newp;
        }
        return root;
	}

	@Override
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	@Override
	public void union(int p, int q) {
		int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
	}

}
