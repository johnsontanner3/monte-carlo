import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Compute statistics on Percolation after performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	// choose the thing you wanna test 
	private PercolationDFS perc;
	private int mySize;
	private int t;
	private List<Point> shuffled;
	private List<Integer> myRuns;

	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T){
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException();
		perc = new PercolationDFS(N);
		mySize = N;
		t = T;
		shuffled = getShuffledCells();
		myRuns = new ArrayList<Integer>();
		int count = 0;
		while (count < t){
			myRuns.add(totalOpened());
			count++;
		}
	}
	
	public List<Point> getShuffledCells() {
		ArrayList<Point> list = new ArrayList<Point>();
		for(int i=0; i<mySize; i++)
			for (int j=0; j<mySize; j++){
				list.add(new Point(i,j));
			}
		Collections.shuffle(list, ourRandom);
		return list;
	}

	public int totalOpened(){
		perc = new PercolationDFS(mySize);
		shuffled = getShuffledCells();
		for (Point cell : shuffled){
			perc.open(cell.x, cell.y);
			if (perc.percolates()){
				break;
			}
		}
		return perc.numberOfOpenSites();
	}
	// sample mean of percolation threshold
	public double mean() {
		double total = 0.0;
		for (int item : myRuns)
			total += item;
		return (total/t) / (mySize*mySize); // mySize*mySize;
	}
	// sample standard deviation of percolation threshold
	public double stddev() {
		double myMean = mean();
		double runningSum = 0.0;
		for (int val: myRuns){
			double temp = val / (mySize*mySize);
			runningSum += (temp - myMean) * (temp - myMean);
		}
		return Math.sqrt((runningSum / (t-1)));
			
	}
	// low  endpoint of 95% confidence interval
	public double delta(){
		return (1.96*stddev())/Math.sqrt(t);
	}
	
	public double confidenceLow(){
		return mean() - delta();
		
	}
	// high endpoint of 95% confidence interval
	public double confidenceHigh(){
		return mean() + delta();
	}
	// print out values for testing &  analysis
	public static void main(String[] args) {
		double start = System.currentTimeMillis();
		PercolationStats test = new PercolationStats(95, 30);
		System.out.println("Mean: "+ test.mean());
		System.out.println("Standard Deviation: "+ test.stddev());
		System.out.println("Lower Bound: "+ test.confidenceLow());
		System.out.println("Upper Bound: "+ test.confidenceHigh());
		double end = System.currentTimeMillis();
		System.out.println("Statistical calculation runtime: " + (end - start));
	}
}
