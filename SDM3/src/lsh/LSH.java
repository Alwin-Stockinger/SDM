package lsh;

import java.util.ArrayList;
import java.util.List;

import data.DataPoint;

public class LSH implements Hasher {

	private double[] p;
	private double bucketSize;
	private int bucketNumber;
	private int[] buckets;
	private List<Double> hashedValues;
	private double minimum;
	private double maximum;
	
	public LSH(double[] p, int bucketNumber) {
		
		setP(p);
		setBucketNumber(bucketNumber);
		hashedValues = new ArrayList<>();
	}
	
	public int[] getBuckets() {
		return buckets;
	}
	
	public void setP(double[] p) {
		this.p = p;
	}
	
	public double[] getP() {
		return p;
	}
	
	public void setBucketNumber(int bucketNumber) {
		this.bucketNumber = bucketNumber;
	}
	
	public int getBucketNumber() {
		return bucketNumber;
	}
	
	public void setBucketSize(double bucketSize) {
		this.bucketSize = bucketSize;
	}
	
	public double getBucketSize() {
		return bucketSize;
	}
	
	@Override
	public void hash(List<DataPoint> dataPoints) {
		
		getInitialMinimumAndMaximum(dataPoints);
		for (DataPoint dataPoint : dataPoints) {
			double value = 0.0;
			for (int i = 0; i < p.length; ++i) {
				
				value += dataPoint.getVector()[i] * p[i];
			}
			hashedValues.add(value);
			if (value < minimum) {
				minimum = value;
			}
			if (value > maximum) {
				maximum = value;
			}
		}
		constructBuckets();
	}
	
	private void constructBuckets() {
		
		// +1 to deal with possible round-up errors
		double range = maximum - minimum + 1;
		setBucketSize(range / bucketNumber);
		buckets = new int[getBucketNumber()];
		fillBuckets();
	}
	
	private void fillBuckets() {
		
		for (Double hashedValue : hashedValues) {
			
			buckets[(int) ((hashedValue - minimum) / bucketSize)]++;
		}
	}
	
	private void getInitialMinimumAndMaximum(List<DataPoint> dataPoints) {
		
		double value = 0.0;
		for (int i = 0; i < p.length; ++i) {
			
			value += dataPoints.get(0).getVector()[i] * p[i];
		}
		minimum = maximum = value;
	}
}
