package Hashfunktions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.DataPoint;

public class LSH implements Hasher {

	private double[] p;
	private double bucketSize;
	private int bucketNumber;
	private Bucket[] buckets;
	private Map<DataPoint, Double> hashValues;
	private double minimum;
	private double maximum;
	
	public LSH(double[] p, int bucketNumber) {
		
		setP(p);
		setBucketNumber(bucketNumber);
		hashValues = new HashMap<>();
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

			index = (int) ((hashValues.get(dataPoints.get(i)) - minimum) / getBucketSize());
			buckets[index].getHashValuesMap().put(dataPoints.get(i), calcHash(dataPoints.get(i)));
			//buckets[index].getDataPoints().add(this.dataPoints.get(i));
			//buckets[index].getHashValues().add(hashValues.get(i));
			//System.out.println("index:   " + index);
		}
		// debug
		/*for (int i = 0; i < buckets.length; ++i) {
			System.out.println("Bucket " + i + " :: " + buckets[i].getHashValues().size());
		}*/
	}
	
	public List<DataPoint> getPointsByCentroid(DataPoint point){
		int index=(int) ((calcHash(point)-minimum)/getBucketSize());
		
		
		
		return buckets[index].getDataPoints();	
	}

	public void combineHashOR(List<DataPoint> dataPoints, LSH otherHasher) {

		// TODO defensive programming: number of buckets must be equal
		getMinimumAndMaximum(dataPoints);
		constructBuckets();
		int index;
		int otherHashIndex;
		for (int i = 0; i < dataPoints.size(); ++i) {

			index = (int) ((hashValues.get(dataPoints.get(i)) - minimum) / getBucketSize());
			buckets[index].getHashValuesMap().put(dataPoints.get(i), calcHash(dataPoints.get(i)));
			//buckets[index].getDataPoints().add(this.dataPoints.get(i));
			//buckets[index].getHashValues().add(hashValues.get(i));
			otherHashIndex = (int) ((otherHasher.hashValues.get(dataPoints.get(i)) - otherHasher.minimum) / otherHasher.getBucketSize());
			//System.out.println("otherIndex:  " + otherHashIndex + " index: " + index);
			if (index != otherHashIndex) {
				//buckets[otherHashIndex].getDataPoints().add(this.dataPoints.get(i));
				//buckets[otherHashIndex].getHashValues().add(hashValues.get(i));
				buckets[otherHashIndex].getHashValuesMap().put(dataPoints.get(i), otherHasher.calcHash(dataPoints.get(i)));
			}
		}
		// debug
		/*for (int i = 0; i < otherHasher.buckets.length; ++i) {
			System.out.println("Bucket " + i + " :: " + otherHasher.buckets[i].getHashValues().size());
		}*/
	}

	public void combineHashAND(List<DataPoint> dataPoints, LSH otherHasher) {

		// TODO defensive programming: number of buckets must be equal
		getMinimumAndMaximum(dataPoints);
		constructBuckets();
		int index;
		int otherHashIndex;
		for (int i = 0; i < dataPoints.size(); ++i) {

			index = (int) ((hashValues.get(dataPoints.get(i)) - minimum) / getBucketSize());
			otherHashIndex = (int) ((otherHasher.hashValues.get(dataPoints.get(i)) - otherHasher.minimum) / otherHasher.getBucketSize());
			if (index == otherHashIndex) {
				buckets[index].getHashValuesMap().put(dataPoints.get(i), calcHash(dataPoints.get(i)));
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
		
		double value = calcHash(dataPoints.get(0));

		minimum = maximum = value;
		for (DataPoint dataPoint : dataPoints) {
			value = calcHash(dataPoint);
			hashValues.put(dataPoint, value);
			//this.dataPoints.add(dataPoint);
			//hashValues.add(value);
			if (value < minimum) {
				minimum = value;
			}
			if (value > maximum) {
				maximum = value;
			}
		}
	}
	
	private double calcHash(DataPoint dataPoint){

		double value=0.;
		for (int i = 0; i < p.length; ++i) {
			value += dataPoint.getVector()[i] * p[i];
		}
		return value;
	}
	
}



