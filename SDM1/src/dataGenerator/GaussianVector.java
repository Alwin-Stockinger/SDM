package dataGenerator;

import java.util.Random;
import java.util.ArrayList;

public class GaussianVector {
	
	private Random randomGenerator;
	public GaussianVector(){
		randomGenerator = new Random();
	}
	
	public ArrayList<Double> gausPoint(ArrayList<Double> arrayList) {	//return a random gaussian distributed point around the given point
		
		ArrayList<Double> out=new ArrayList<Double>(arrayList.size());
		
		
		for(int i=0;i<arrayList.size();i++) {//cluster länge=dim, dim muss nicht überall mitgezogen werden
			double rand=randomGenerator.nextGaussian()+arrayList.get(i);
			out.add(rand);
		}
		
		return out;
	}
	
	
	public ArrayList<Double> startPoint(int dim, double size) {	//dim is dimension of space where cluster centers can be in, size is the max values in + and - direction of the space
	
		ArrayList<Double> out=new ArrayList<Double>();
		for(int i=0;i<dim;i++) {
			double rand=(randomGenerator.nextDouble()-0.5)*2*size;
			out.add(rand);
		}
		
		return out;
	}
	
}
