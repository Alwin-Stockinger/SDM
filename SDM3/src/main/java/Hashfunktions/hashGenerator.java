package Hashfunktions;

import java.util.Random;

public class hashGenerator {
	
	public static double[] generateHashVector(int dim, int scale){
		double[] hashVector=new double[dim];
		
		Random randomgenerator=new Random();
		
		for(int i=0;i<dim;i++) {
			hashVector[i]=(randomgenerator.nextDouble()*2-1)*scale;
		}
		return hashVector;
	}
	
	public static double[][] generateHashMatrix(int hashAmount, int dim, int scale){
		double[][] hashMatrix=new double[dim][hashAmount];
		
		Random randomgenerator = new Random();
		
		for(int i=0;i<hashAmount;i++) {
			for(int j=0;j<dim;j++) {
				hashMatrix[j][i]=(randomgenerator.nextDouble()*2-1)*scale;
			}
		}
		return hashMatrix;
	}
	
}
