package test;

import static org.junit.Assert.*;

import Hashfunktions.Bucket;
import org.junit.Test;

import data.DataPoint;
import data.DataSet;
import Hashfunktions.LSH;

public class UnitTest {

	@Test
	public void test() {
		
		DataSet dataSet = new DataSet("test.csv");
		DataPoint testPoint = dataSet.getDataPoints().get(9);
		
		assertEquals(dataSet.getDataPoints().size(), 15);
		assertEquals(testPoint.getTruth(), testPoint.getCluster());
		assertEquals(testPoint.getTruth(), "class15.0");
		assertEquals(testPoint.getVector()[4], 38.53374833133432, 0.0001);
	}

	@Test
	public void testHashing() {
		
		DataSet dataSet = new DataSet("test.csv");
		double[] p = new double[10];
		for (int i = 0; i < 10; ++i) {
			p[i] = 1.0;
		}
		LSH hasher = new LSH(p, 15);
		hasher.hash(dataSet.getDataPoints());
		
		assertEquals(dataSet.getDataPoints().size(), 15);
	}
	
	@Test
	public void testHashing1() {
		
		DataSet dataSet = new DataSet("test.csv");
		double[] p = new double[10];
		for (int i = 1; i < 10; ++i) {
			p[i] = 0.0;
		}
		p[0] = 1.0;
		
		LSH hasher = new LSH(p, 15);
		hasher.hash(dataSet.getDataPoints());
		
		assertEquals(dataSet.getDataPoints().size(), 15);
	}

	@Test
	public void testSizeOfDataSet() {

		DataSet dataSet = new DataSet("LSH-nmi.csv");
		double[] p = new double[10];
		for (int i = 0; i < 10; ++i) {

			p[i] = 1.0;
		}
		LSH lsh = new LSH(p, 30);
		lsh.hash(dataSet.getDataPoints());

		int sum = 0;
		int sum1 = 0;
		Bucket[] buckets = lsh.getBuckets();
		for (int i = 0; i < buckets.length; ++i) {
			//System.out.println(i+1 + " : " + buckets[i]);
			sum += buckets[i].getHashValues().size();
			sum1 += buckets[i].getDataPoints().size();
		}

		assertEquals(sum, 291500);
		assertEquals(sum1, 291500);
	}

	@Test
	public void testOrAnd() {

		DataSet dataSet = new DataSet("test.csv");
		double[] p = new double[10];
		for (int i = 0; i < 10; ++i) {
			p[i] = 1.0;
		}
		LSH hasher = new LSH(p, 15);
		hasher.hash(dataSet.getDataPoints());

		LSH hasher1 = new LSH(p, 15);
		hasher1.combineHashOR(dataSet.getDataPoints(), hasher);

		assertEquals(dataSet.getDataPoints().size(), 15);
	}
}
