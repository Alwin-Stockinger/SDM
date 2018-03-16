package main;

import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;

import dataGenerator.ClusterGenerator;
import visual.XYPlane;
import kmeans.KMeans;
import kmeans.Cluster;

public class Main {
	

	
	public static void main(String[] args) {
		/*int clusters=Integer.parseInt(args[0]);
		int points=Integer.parseInt(args[1]);
		int dim=Integer.parseInt(args[2]);
		*/
		int clusterAmount=8;
		int points=1000;
		int dim=2;
		double size=5;
		
		ClusterGenerator gen=new ClusterGenerator();
		KMeans k=new KMeans();
		ArrayList<Cluster> clusters=k.randomMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.pointMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.randomLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.pointLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		
		
		
		
	
		if(dim==2) {
			final XYPlane window=new XYPlane(clusters);
			window.pack();
			RefineryUtilities.centerFrameOnScreen(window);
			window.setVisible(true);
		}
		
	}
}
