package Hashfunktions;

import java.util.ArrayList;
import java.util.List;

import data.DataPoint;

public class LSH implements Hasher {

	private double[] p;
	private double bucketSize;
	private int bucketNumber;
	private Bucket[] buckets;
	private List<DataPoint> dataPoints; // is this necessary?
	private List<Double> hashValues;
	private double minimum;
	private double maximum;
	
	public LSH(double[] p, int bucketNumber) {
		
		setP(p);
		setBucketNumber(bucketNumber);
		dataPoints = new ArrayList<>();
		hashValues = new ArrayList<>();
	}
	
	public Bucket[] getBuckets() {
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
		
		getMinimumAndMaximum(dataPoints);
		constructBuckets();
		int index;
		for (int i = 0; i < dataPoints.size(); ++i) {

			index = (int) ((hashValues.get(i) - minimum) / getBucketSize());
			buckets[index].getDataPoints().add(this.dataPoints.get(i));
			buckets[index].getHashValues().add(hashValues.get(i));
		}
	}

	public void combineHashOR(List<DataPoint> dataPoints, LSH otherHasher) {

		// TODO defensive programming: number of buckets must be equal
		getMinimumAndMaximum(dataPoints);
		constructBuckets();
		int index;
		int otherHashIndex;
		for (int i = 0; i < dataPoints.size(); ++i) {

			index = (int) ((hashValues.get(i) - minimum) / getBucketSize());
			buckets[index].getDataPoints().add(this.dataPoints.get(i));
			buckets[index].getHashValues().add(hashValues.get(i));
			otherHashIndex = (int) ((otherHasher.hashValues.get(i) - otherHasher.minimum) / getBucketSize());
			if (index == otherHashIndex) {
				buckets[otherHashIndex].getDataPoints().add(this.dataPoints.get(i));
				buckets[otherHashIndex].getHashValues().add(hashValues.get(i));
			}
		}
	}
	
	private void constructBuckets() {
		
		// +1 to deal with possible round-up errors
		double range = maximum - minimum + 1;
		setBucketSize(range / bucketNumber);
		buckets = new Bucket[getBucketNumber()];
		for (int i = 0; i < getBucketNumber(); ++i) {
			buckets[i] = new Bucket();
		}
	}
	
	private void getMinimumAndMaximum(List<DataPoint> dataPoints) {
		
		double value = 0.0;
		for (int i = 0; i < p.length; ++i) {
			
			value += dataPoints.get(0).getVector()[i] * p[i];
		}
		minimum = maximum = value;
		for (DataPoint dataPoint : dataPoints) {
			value = 0.0;
			for (int i = 0; i < p.length; ++i) {

				value += dataPoint.getVector()[i] * p[i];
			}
			this.dataPoints.add(dataPoint);
			hashValues.add(value);
			if (value < minimum) {
				minimum = value;
			}
			if (value > maximum) {
				maximum = value;
			}
		}
	}
}
