package main;

import data.DataSet;
import lsh.LSH;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Starting LSH algorithm");
		DataSet dataSet = new DataSet("LSH-nmi.csv");
		double[] p = new double[10];
		for (int i = 0; i < 10; ++i) {
			
			p[i] = 1.0;
		}
		LSH lsh = new LSH(p, 30);
		lsh.hash(dataSet.getDataPoints());
		
		int sum = 0;
		int[] buckets = lsh.getBuckets();
		for (int i = 0; i < buckets.length; ++i) {
			System.out.println(i+1 + " : " + buckets[i]);
			sum += buckets[i];
		}
		System.out.println("total: " + sum);
	}
}
