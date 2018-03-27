package main;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jfree.ui.RefineryUtilities;

import dataGenerator.ClusterGenerator;
import visual.XYPlane;
import kmeans.KMeans;
import kmeans.Cluster;

public class Main {
	

	
	public static void main(String[] args) {
		/* Für das Fertige Program werden dann die startwerte hier eingelesen
		int clusters=Integer.parseInt(args[0]);
		int points=Integer.parseInt(args[1]);
		int dim=Integer.parseInt(args[2]);
		*/
		int clusterAmount=5;
		int points=1000;
		int dim=2;
		double size=10;
		
		//Generator für die die Daten, die Daten werden in der Form CopyOnWriteArrayList gegeben, da diese beim Tterieren, solange man nicht darauf schreibt, schneller und gut parallelisierbar sein soll
		ClusterGenerator gen=new ClusterGenerator();
		
		KMeans k=new KMeans();
		//Die 4 unterschiedlichen KMeans Algorithmen( Die Funktionen nehmen alle als Argument die cluster Menge, die generierten Punkt und die Größe des Teilbereichs des R^n auf den sich die Punkte befinden an)::
		//ArrayList<Cluster> clusters=k.randomMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.pointMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		//ArrayList<Cluster> clusters=k.randomLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size, true);
		//ArrayList<Cluster> clusters=k.pointLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
		
		//visualizeResults(clusters, dim);
		testSeries(k, gen);
	}
	
	public static void visualizeResults(ArrayList<Cluster> clusters, int dim) {
		
		if(dim==2) {
			final XYPlane window=new XYPlane(clusters);
			window.pack();
			RefineryUtilities.centerFrameOnScreen(window);
			window.setVisible(true);
		}
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
			ArrayList<Cluster> clusters = k.randomLloyd(clusterAmount, points, size, true);
		
			CopyOnWriteArrayList<ArrayList<Double>> points2 = gen.generate(clusterAmount, N, dim, size);
			System.out.println("Random points Lloyd: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters2 = k.pointLloyd(clusterAmount, points, size,true);
		
			CopyOnWriteArrayList<ArrayList<Double>> points3 = gen.generate(clusterAmount, N, dim, size);
			System.out.println("Random partition Mac Queen: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters3 = k.randomMQ(clusterAmount, points, size,true);
		
			CopyOnWriteArrayList<ArrayList<Double>> points4 = gen.generate(clusterAmount, N, dim, size);
			System.out.println("Random points Mac Queen: dimension = " + dim + ", N = " + N);
			ArrayList<Cluster> clusters4 = k.pointMQ(clusterAmount, points, size,true);
			System.out.println("\n");
		}
	}
}
