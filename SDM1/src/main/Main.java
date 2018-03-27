package main;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

import org.jfree.ui.RefineryUtilities;

import dataGenerator.ClusterGenerator;
import dataGenerator.Point;
import visual.GnuPlot;
import visual.XYPlane;
import kmeans.KMeans;
import kmeans.Cluster;

public class Main {
	

	
	public static void main(String[] args) {
		int clusterAmount=5;
		int points=1000;
		int dim=2;
		double size=10;
		String kVariant;
		boolean kgiven=false;
		
/*		ArrayList<Double> p1 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		Point P1=new Point(p1);
		
		Point P2=new Point(3);
		P2.randomise(P1,1);
		System.out.println(P2);
*/		

		
		if(args.length>=2) {
			clusterAmount=Integer.parseInt(args[0]);
			points=Integer.parseInt(args[1]);
			dim=Integer.parseInt(args[2]);
			if(args.length>=3) {
				kgiven=true;
				kVariant=args[3];
			}	
		}
		           
		if(kgiven) {
			//analyse argument and start corresponding algorithm
		}
		//Generator für die die Daten, die Daten werden in der Form CopyOnWriteArrayList gegeben, da diese beim Tterieren, solange man nicht darauf schreibt, schneller und gut parallelisierbar sein soll
		ClusterGenerator gen=new ClusterGenerator();
		
		KMeans k=new KMeans();
		//Die 4 unterschiedlichen KMeans Algorithmen( Die Funktionen nehmen alle als Argument die cluster Menge, die generierten Punkt und die Größe des Teilbereichs des R^n auf den sich die Punkte befinden an)::
		//ArrayList<Cluster> clusters=k.randomMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.pointMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		ArrayList<Cluster> clusters=k.randomLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size, false);
		//ArrayList<Cluster> clusters=k.pointLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		
		
		
		visualizeResults(clusters, dim);
		//testSeries(k, gen);*/
	}
	
	public static void visualizeResults(ArrayList<Cluster> clusters, int dim) {
		
		/*if(dim==2) {
			final XYPlane window=new XYPlane(clusters);
			window.pack();
			RefineryUtilities.centerFrameOnScreen(window);
			window.setVisible(true);
		}*/
		try {
			GnuPlot s=new GnuPlot(clusters,dim);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		//s.gnuplot();
		
	}
	
	public static void testSeries(KMeans k, ClusterGenerator gen) {
		
		int N = 1000;
		System.out.println("Starting the Test - Series");
		//ArrayList<Cluster> clusters=k.randomMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		
		for(int i = 0; i < 10; ++i) {
			
			runTestsForDifferentDims(k, gen, N);
			N += 500;
		}
		System.out.println("End the Test - Series");
	}
	
	public static void runTestsForDifferentDims(KMeans k, ClusterGenerator gen, int N) {
		
		int clusterAmount=5;
		//int N=1000;
		//int dim=2;
		double size=10;
		
		for(int dim = 1; dim <= 5; ++dim) {
			
			System.out.println("\n");
			CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
			
			
			System.out.println("Random partition Lloyd: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters = k.randomLloyd(clusterAmount, points, size, false);
		
			
			System.out.println("Random points Lloyd: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters2 = k.pointLloyd(clusterAmount, points, size,false);
		
			System.out.println("Random partition Mac Queen: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters3 = k.randomMQ(clusterAmount, points, size,false);
		
			
			System.out.println("Random points Mac Queen: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters4 = k.pointMQ(clusterAmount, points, size,false);
			System.out.println("\n");
		}
	}
}
