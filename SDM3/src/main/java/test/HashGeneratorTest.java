package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Hashfunktions.HashGenerator;

public class HashGeneratorTest {
	

	
	@Test
	public void scaleValueTest(){
		int scale=1;
		double[] vec=HashGenerator.generateHashVector(10, scale);
		
		for(int i=0;i<10;i++) {
			assertTrue(vec[i]<scale);
			assertTrue(vec[i]>-scale);
		}
		
	}
}
