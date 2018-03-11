package dataGenerator;

import java.util.Random;

public class GaussianVector {
	
	private Random randomGenerator;
	public GaussianVector(){
		randomGenerator = new Random();
	}
	
	public double[] gausPoint(double[] cluster) {	//return a random gaussian distributed point around the given point
		double[] out=new double[cluster.length];
		
		for(int i=0;i<cluster.length;i++) {//cluster länge=dim, dim muss nicht übnerall mitgezogen werden
			double rand=randomGenerator.nextGaussian()+cluster[i];
			out[i]=rand;
		}
		
		return out;
	}
	
	
	public double[] startPoint(int dim, double size) {	//dim is dimension of space where cluster centers can be in, size is the max values in + and - direction of the space
		double[] out=new double[dim];
		
		for(int i=0;i<dim;i++) {
			double rand=(randomGenerator.nextDouble()-0.5)*2*size;
			out[i]=rand;
		}
		
		return out;
	}
	
}
