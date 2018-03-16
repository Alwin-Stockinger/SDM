package dataGenerator;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;;

public class ClusterGenerator {
	
	GaussianVector genVec;
	public double size=20;	//sets die size of the space where cluster centers can be in +- direction, so the volume of the n dimensional space would be (size*2)^n
	
	public CopyOnWriteArrayList<ArrayList<Double>> generate(int clusters,int points, int dim, double size){
		this.size=size;
		genVec=new GaussianVector();
		
		ArrayList<ArrayList<Double>> clusterPoints=genClusterPoints(clusters,dim);
		return genData(clusterPoints,points,dim);
	}

	private CopyOnWriteArrayList<ArrayList<Double>> genData(ArrayList<ArrayList<Double>> clusterPoints, int points, int dim) {
		
		
		CopyOnWriteArrayList<ArrayList<Double>> data= new CopyOnWriteArrayList<ArrayList<Double>>();

		
		for(int i=0; i<clusterPoints.size();++i) {
			for(int j=0;j<points/clusterPoints.size();++j) {
				//data[i*points/clusterPoints.length+j]=genVec.gausPoint(clusterPoints[i]);
				data.add(genVec.gausPoint(clusterPoints.get(i)));
			}
		}
		
		
		
		return data;
	}

	private ArrayList<ArrayList<Double>> genClusterPoints(int clusters, int dim) {
		ArrayList<ArrayList<Double>> points=new ArrayList<ArrayList<Double>>();
		for(int i=0;i<clusters;i++)	points.add(genVec.startPoint(dim, size));
		
		return points;
	}
	
}
