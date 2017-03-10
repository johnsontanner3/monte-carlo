import java.awt.Point;

import java.util.ArrayList;
import java.util.Arrays;
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

	private int mySize;
	private int iterations;
	private int numCells;
	private List<Point> shuffled;
	private PercolationUF[] myPercs;
	private double myMean;
	private int[] myOpenings;
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T){
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException("Invalid input for either N or T");
		mySize = N;
		numCells = N*N;
		iterations = T;
		myPercs = new PercolationUF[T];
		for (int i=0; i<T; i++){
			myPercs[i] = new PercolationUF(N);
		}
		//myMean = mean();
		myOpenings = getRawOpenings(); // this is an array of raw openings 
	}
	
	private List<Point> getShuffledCells() {
		ArrayList<Point> list = new ArrayList<Point>();
		for(int i=0; i<mySize; i++)
			for (int j=0; j<mySize; j++){
				list.add(new Point(i,j));
			}
		Collections.shuffle(list, ourRandom);
		return list;
	}

	private int[] getRawOpenings(){
		int[] temp = new int[iterations];
		for (int i=0; i < iterations; i++){
			shuffled = getShuffledCells(); // re-shuffle
			temp[i] = totalOpened(myPercs[i]);
		}
		return temp;
	}
	
	private int totalOpened(PercolationUF perc){
		for (Point cell : shuffled){
			perc.open(cell.x, cell.y);
			if (perc.percolates())
				break;
		}
		return perc.numberOfOpenSites();
	}
	
	// sample mean of percolation threshold
	public double mean() {
		double total = 0.0;
		for (int i=0; i<iterations; i++)
			total += myOpenings[i];
		return (total/iterations) / numCells; // mySize*mySize;
	}
	
	
	// sample standard deviation of percolation threshold
	public double stddev() {
		double runningSum = 0.0;
		myMean = mean();
		for (int i=0; i<iterations; i++){
			double temp = myOpenings[i]; //totalOpened(p);
			temp = temp / numCells;
			runningSum += ((temp - myMean) * (temp - myMean));
		}
		return Math.sqrt((runningSum / (iterations-1)));
			
	}
	// low  endpoint of 95% confidence interval
	private double delta(){
		return (1.96*stddev())/Math.sqrt(iterations);
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
		PercolationStats test = new PercolationStats(20, 10);
		System.out.println("Mean: "+ test.mean());
		System.out.println("Standard Deviation: "+ test.stddev());
		System.out.println("Lower Bound: "+ test.confidenceLow());
		System.out.println("Upper Bound: "+ test.confidenceHigh());
		Arrays.sort(test.myOpenings);
		System.out.println(Arrays.toString(test.myOpenings));
		double end = System.currentTimeMillis();
		System.out.println("Statistical calculation runtime: " + (end - start));
	}
}
