package kmeans;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;




class KMeansTest {
	KMeans k=new KMeans();
	
	ArrayList<Double> a;
	ArrayList<Double> b;
	
	
	
	@Before
	void setUp() throws Exception {
		a=new ArrayList<Double>();
		b=new ArrayList<Double>();
	}

	@After
	void tearDown() throws Exception {
	}

	@Test
	final void testDistance1() {
		a.add(1.);
		b.add(4.);
		
		double dist=k.distance(a, b);
		
		assertEquals(3.,dist);
	}
	
	@Test
	final void testDistance2() {
		a.add(4.);
		a.add(1.);
		b.add(0.);
		b.add(6.);
		
		double dist=k.distance(a, b);
		
		assertEquals(5.,dist);
	}
	
	

}
