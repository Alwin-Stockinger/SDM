package test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.DataPoint;
import data.DataSet;
import lsh.LSH;

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
}
