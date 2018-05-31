package main;

import java.util.ArrayList;
import java.util.List;

import data.DataPoint;
import data.DataSet;
import kMeans.Cluster;
import kMeans.KMeans;

public class KMain {

	public static void main(String[] args) {
		
		
		
		
		double[] c1= {34, 83, 51, 21, 93, 8, 10, 17, 18, 94};
		double[] c2= {0, 21, 22, 50, 5, 75, 12, 24, 94, 9};
		double[] c3= {48, 19, 17, 85, 42, 83, 25, 72, 62, 20};
		double[] c4= {44, 58, 58, 60, 52, 25, 43, 51, 73, 42};
		double[] c5= {39, 33, 20, 55, 53, 46, 49, 56, 20, 30};
		double[] c6= {6, 77, 6, 64, 46, 46, 37, 64, 4, 98};
		double[] c7= {71, 35, 6, 0, 48, 18, 64, 94, 52, 48};
		double[] c8= {91, 62, 75, 6, 90, 51, 1, 55, 71, 67};
		double[] c9= {51, 78, 10, 4, 88, 74, 61, 85, 51, 34};
		double[] c10= {34, 37, 57, 31, 49, 61, 61, 37, 58, 64};
		double[] c11= {16, 59, 78, 65, 45, 17, 83, 79, 11, 41};
		double[] c12= {73, 25, 34, 60, 47, 31, 16, 36, 22, 75};
		double[] c13= {20, 14, 12, 42, 38, 10, 47, 30, 28, 6};
		double[] c14= {84, 84, 51, 5, 48, 42, 25, 45, 17, 48};
		double[] c15= {68, 61, 60, 67, 43, 70, 62, 50, 49, 49};
		
		ArrayList<DataPoint> startPoint=new ArrayList<>();
		
		startPoint.add(new DataPoint(c1,null));
		startPoint.add(new DataPoint(c2,null));
		startPoint.add(new DataPoint(c3,null));
		startPoint.add(new DataPoint(c4,null));
		startPoint.add(new DataPoint(c5,null));
		startPoint.add(new DataPoint(c6,null));
		startPoint.add(new DataPoint(c7,null));
		startPoint.add(new DataPoint(c8,null));
		startPoint.add(new DataPoint(c9,null));
		startPoint.add(new DataPoint(c10,null));
		startPoint.add(new DataPoint(c11,null));
		startPoint.add(new DataPoint(c12,null));
		startPoint.add(new DataPoint(c13,null));
		startPoint.add(new DataPoint(c14,null));
		startPoint.add(new DataPoint(c15,null));
		
		
		//startPoint.addAll({p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15});
		
		KMeans kmeans=new KMeans();
		
		DataSet dataSet=new DataSet("LSH-nmi.csv");
		List<DataPoint> data=dataSet.getDataPoints();
		
		
		ArrayList<Cluster> clusters=kmeans.lshLloyed(startPoint, data, 8, 8, 10);

		for(Cluster cluster:clusters) {
			System.out.print("Cluster ");
			for(int i=0;i<cluster.getCentroid().getDim();i++) {
				System.out.print(cluster.getCentroid().getVector()[i]+",");
			}
			
			
			System.out.println();
		}
		
		
	}

}
