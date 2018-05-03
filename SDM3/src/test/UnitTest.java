package test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.DataPoint;
import data.DataSet;

public class UnitTest {

	@Test
	public void test() {
		
		DataSet dataSet = new DataSet("test.csv");
		DataPoint testPoint = dataSet.getDataPoints().get(9);
		
		assertEquals(dataSet.getDataPoints().size(), 15);
		assertEquals(testPoint.getTruth(), testPoint.getCluster());
		assertEquals(testPoint.getTruth(), "class15.0");
		assertEquals(testPoint.getVectors()[4], 38.53374833133432, 0.0001);
	}

}
