import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */


public class PercolationDFS implements IPercolate {
	// possible instance variable for storing grid state
	public int[][] myGrid;
	private int myOpenSites;
//	public static int OPEN;
//	public static int FULL;
//	public static int BLOCKED; 
	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 * we'll make a random grid and ask if it percolates. 
	 * To do this, we need to explore our grid. 
	 * Fill in all the open spots. 
	 * How do we mark this in an int[][] grid?
	 * call 0 blocked and 1 open. Do we update 1 with 2 if FULL (i.e., visited)?
	 */
	public PercolationDFS(int n) {
		// TODO complete constructor and add necessary instance variables
		// I suppose we'll use random to generate random grids 
		myGrid = new int[n][n];
		myOpenSites = 0;
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED); // if we do this, then we don't need myFull 
	}

	public void open(int i, int j) {
		// use depth first search and update myGrid 
		// how does dfs work?
		// call dfs here
		// we are given a cell. just call dfs on it. 
		// we're using this to traverse the top row. 
		// for each cell in top row, explore with dfs
		if (myGrid[i][j] != BLOCKED) // should this be ==?
			return;
		
		myOpenSites++;
		myGrid[i][j] = OPEN;
		// for each item in top, first FLUSH (take everything that's full and make it open again)
		validate(i,j);
		// and then call dfs for each cell in the top row 
		//if (i==0) {
		for (int k = 0; k < myGrid.length; k++)
			for (int l = 0; l < myGrid.length; l++)
				if (myGrid[k][l] == FULL)
					myGrid[k][l] = OPEN;
		for (int m = 0; m < myGrid.length; m++)
			dfs(0,m);
		//}
	}
	
	private void validate(int i, int j) {
        int n = myGrid.length;
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw new IndexOutOfBoundsException("index  is not between 0 and " + (n-1));
        }
}   

	public boolean isOpen(int i, int j) {
		// can ya do this?
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			throw new IndexOutOfBoundsException("Index " + i + "," + j + " is bad!");
		return myGrid[i][j] == OPEN;
	}

	public boolean isFull(int i, int j) {
		// or should we directly check the int value in our grid? 
		// same question for isOpen 
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			throw new IndexOutOfBoundsException("Index " + i + "," + j + " is bad!");
		return myGrid[i][j] == FULL;
	}

	public int numberOfOpenSites() {
		return myOpenSites;
	}

	public boolean percolates() {
		// TODO: run DFS to find all full sites
		for (int j=0; j<myGrid.length; j++){
			if (isFull(myGrid.length-1, j)) return true;
		}
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	private void dfs(int row, int col) {
		//  Flush the grid by marking all full cells as open before calling this method
		//  this is the recursive method 
		// avoid isFull cells and out of bounds cells and blocked cells 
		// first check myGrid to see if out of bounds or blocked
		// then you can overlay your check with myFull to see if full or val is 1. 
		// myGrid keeps us in bounds. myFull is what we get to play with. 
		int n = myGrid.length;
		// stay in bounds! checking our cell if it's open, not full and in bounds 
		if (row < 0 || row >= n)
			return;
		if (col < 0 || col >= n)
			return;
		if (!isOpen(row,col))	// nothing to do if not open 
			return;
		if (isFull(row,col))	// can't go if full 
			return;
		
		myGrid[row][col] = FULL;
		//recurse for up, down, right, left
		dfs(row+1,col); // down
		dfs(row-1,col); // up
		dfs(row,col+1); // right
		dfs(row,col-1); // left
		
	}
}
