package kmeans;

import java.util.Arrays;

public class KMeans {
	
	double[][] data;
	double[][] centroids;
	
	double maxDistance;
	
	
	public double[][][] kmain() {
		while(!calcAllCentroids());
		return orderData();
	}
	
	private double[][][] orderData(){
		//TODO Function has to order all Data points
		return null;
	}
	
	
	
	
	
	private void calcOneCentroid(int centNum) {
		
		int density=0;
		for(int i=0;i<data.length;++i) {
			if(centNum==nearestCent(data[i])) {
				density++;
				centroids[centNum]=sumArrays(centroids[centNum],data[i]);
			}
		}
		
		for(int j=0;j<centroids[0].length;j++) {
			centroids[centNum][j]/=density;
		}
	}
	
	private boolean calcAllCentroids(){
		double[][] newCentroids=new double[centroids.length][centroids[0].length];
		int[] density=new int[centroids.length];
		for(int i=0;i<data.length;++i) {
			int nearest=nearestCent(data[i]);
			density[nearest]++;
			newCentroids[nearest]=sumArrays(newCentroids[nearest],data[i]);
		}
		
		for(int i=0;i<centroids.length;++i) {
			for(int j=0;j<centroids[0].length;j++) {
				newCentroids[i][j]/=density[i];
			}
		}
		boolean same=newCentroids.equals(centroids);
		centroids=newCentroids;
		return same;
	}
	
	private double[] sumArrays(double[] a, double[] b) {
		for(int i=0; i<a.length;++i) {
			a[i]+=b[i];
		}
		return a;
	}
	
	
	private int nearestCent(double[] point) {
		int nearest=0;
		double minDistance=maxDistance;
		for(int i=0;i<centroids.length;++i) {
			double dist=distance(point,centroids[i]);
			if(dist<minDistance) {
				minDistance=dist;
				nearest=i;
			}
		}
		return nearest;
	}
	
	
	private double distance(double[] a,double[] b) {
		double sum=0;
		for(int i=0;i<a.length;++i) {
			sum=Math.pow(b[i]-a[i],2);
		}
		return Math.sqrt(sum);
	}
	
}
