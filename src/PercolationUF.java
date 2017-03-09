import java.util.Arrays;

/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */

public class PercolationUF implements IPercolate {
	private final int OUT_BOUNDS = -1;
	public int[][] myGrid; // is this even necessary? yes to keep track of open
	private int myOpenSites;
	public IUnionFind qf;
	private final int VIRT_SOURCE;
	private final int VIRT_SINK;
	
	/**
	 * Constructs a Percolation object for a nxn grid that that creates
	 * a IUnionFind object to determine whether cells are full
	 */
	public PercolationUF(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		myOpenSites = 0;
		myGrid = new int[n][n];
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED);
		qf = new QuickUWPC(n*n + 2);
		VIRT_SOURCE = n*n;
		VIRT_SINK = n*n + 1;
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row < 0 || row >= myGrid.length || col < 0 ||
				col >= myGrid.length)
			return OUT_BOUNDS;
		// these indices directly correspond to the myID array in QuickFind.java a.k.a. our qf object 
		return row*myGrid.length + col; // unique index for every cell in the grid 
	}

	public void open(int i, int j) {
		// TODO complete open
		// should be making union calls here 
		// be careful with top-bottom union calls 
		if (myGrid[i][j] != BLOCKED) // already OPEN
			return;
		myGrid[i][j] = OPEN;
		myOpenSites++;
		// int index = getIndex(i,j); // our index
		// int id = qf.find(index); 		// this will change. tells us what set our cell is in currently  
		validate(i,j);
//		if (i==0){
//			qf.union(index, VIRT_SOURCE);
//		}	
		//for (int m=0; m < myGrid.length; m++)
		connect(i,j); // try to connect with all adjacent cells 
	}

	public boolean isOpen(int i, int j) {
		return myGrid[i][j] == OPEN;
	}

	public boolean isFull(int i, int j) {
		
		int index = getIndex(i,j);
		return qf.connected(index, VIRT_SOURCE);
		//return isOpen(i,j);
	}
	

	public int numberOfOpenSites() {
		return myOpenSites;
	}
	
	private void validate(int i, int j) {
	        int n = myGrid.length;
	        int idx = getIndex(i,j);
	        if (i < 0 || i >= n || j < 0 || j >= n) {
	            throw new IndexOutOfBoundsException("index " + idx + " is not between 0 and " + (n-1));
	        }
	}       

	public boolean percolates() {

		return qf.connected(VIRT_SOURCE, VIRT_SINK);
	}

	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		
		// check adjacent cells
		// if already connected, move on
		int n = myGrid.length;
		// stay in bounds! checking our cell if it's open, not full and in bounds 
		if (row < 0 || row >= n)
			return;
		if (col < 0 || col >= n)
			return;
		if (!isOpen(row,col))	// nothing to do if not open 
			return;
//		if (isFull(row,col))	// can't go if full 
//			return;
		
		
		int index = getIndex(row,col); // our index
		if (row==0)
			qf.union(index, VIRT_SOURCE);
		if (row==n-1)
			qf.union(index, VIRT_SINK);

		
		int down = getIndex(row+1,col);
		int up = getIndex(row-1,col);
		int right = getIndex(row,col+1);
		int left = getIndex(row,col-1);
		
		
		
		if (!((down < 0 || down >= n*n) || !isOpen(row+1,col) || qf.connected(index, down))){
			qf.union(down, index); // down
			//connect(row+1,col);
		}
		
		
		
		if (!((up < 0 || up >= n*n) || !isOpen(row-1,col) || qf.connected(index, up))){
			qf.union(up, index); // down
			//connect(row-1,col);
		}
		
		
		
		if (!((right < 0 || right >= n*n) || !isOpen(row,col+1) || qf.connected(index, right))){
			qf.union(right, index); // down
			//connect(row,col+1);
		}
		
		
		
		if (!((left < 0 || left >= n*n) || !isOpen(row,col-1) || qf.connected(index, left))){
			qf.union(left, index); // down
			//connect(row,col-1);
		}
		
		
		
//		if ((up >= 0 && up < n*n) && !isFull(row-1,col) && isOpen(row-1,col)){
//			qf.union(up, index); // up
//			connect(row-1,col);
//		}
//		if ((right >= 0 && right < n*n) && !isFull(row,col+1) && isOpen(row,col+1)){
//			qf.union(right, index); // right
//			connect(row,col+1);
//		}
//		if ((left >= 0 && left < n*n) && !isFull(row,col-1) && isOpen(row,col-1)){
//			qf.union(left, index); // left
//			connect(row,col-1);
//		}
//		
		
	}

}
