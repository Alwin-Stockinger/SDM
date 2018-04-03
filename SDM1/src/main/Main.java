package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jfree.ui.RefineryUtilities;

import dataGenerator.ClusterGenerator;
import visual.GnuPlot;
import visual.XYPlane;
import kmeans.KMeans;
import kmeans.Cluster;

public class Main {
	

	
	public static void main(String[] args) {
		/*int clusterAmount=10;
		int N=500;
		int dim=2;
		double size=10;
		String kVariant = "compare";
		boolean kgiven=false;
		boolean vis=false;*/
		
		// 0-values werden nicht abgefangen. aber egal denke ich.
		int clusterAmount	=Math.abs(Integer.parseInt		(System.getProperty("cluster","10")));
		int N				=Math.abs(Integer.parseInt		(System.getProperty("n","500")));
		int dim				=Math.abs(Integer.parseInt		(System.getProperty("dim","2")));
		double size			=Math.abs(Double.parseDouble	(System.getProperty("size","10")));
		String kVariant 	=	System.getProperty("variant","");
		//boolean kgiven	=Boolean.parseBoolean	(System.getProperty("n",""));
		boolean vis			=Boolean.parseBoolean			(System.getProperty("vis","false"));		
		boolean png			=Boolean.parseBoolean			(System.getProperty("png","false"));		

		
		ClusterGenerator gen=new ClusterGenerator();
		KMeans k=new KMeans();
		
		System.out.println("Starting k-means program \n\n");
		
		System.out.println("Usage: add the following options to the command - line");
		System.out.println("	-Dcluster=IntegerValue:	Number of clusters");
		System.out.println("	-Dn=IntegerValue:	Number of data-points");
		System.out.println("	-Ddim=IntegerValue:	Dimension");
		System.out.println("	-Dsize=DoubleValue:	Size of the domain");
		System.out.println("	-Dvariant=String:	Name of the task (necessary)");
		System.out.println("	-Dvis=Boolean:		Results are visualized");
		System.out.println("	-Dpng=Boolean:		Make a GnuPlot \n\n\n");

		
/*		if(args.length>2) {
			//clusterAmount=Integer.parseInt(args[0]);
			//N=Integer.parseInt(args[1]);
			//dim=Integer.parseInt(args[2]);
			clusterAmount = parseDecimalValue(args[0], clusterAmount);
			N = parseDecimalValue(args[1], N);
			dim = parseDecimalValue(args[2], dim);
			
			if(args.length>3) {
				kgiven=true;
				kVariant=args[3];
	
				if(args.length>4) {
					if(args[4].equals("true")) {
						if(dim==2) vis=true;
						else System.out.println("Visualization is only for 2 Dimensions viable, you picked "+dim+" dimensions.");
					}
				}
				
			}
			else System.out.println("Not enough start parameters, 4. Argument should be the Algorithm");
		}
	*/	
		           
		//if(kVariant!="") {//analyse argument and start corresponding algorithm
			if(kVariant.equals("partitionLloyed")) {
				CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
				System.out.println("Random partition Lloyd: k="+clusterAmount+", dimension = " + dim + ", N = " + N);
				ArrayList<Cluster> clusters = k.randomLloyd(clusterAmount, points, size, vis);
				return;
			}
			else if(kVariant.equals("partitionMQ")) {
				CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
				System.out.println("Random partition Mac Queen: k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
				ArrayList<Cluster> clusters3 = k.randomMQ(clusterAmount, points, size,vis);
			}
			else if(kVariant.equals("pointLloyed")) {
				CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
				System.out.println("Random points Lloyd:  k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
				ArrayList<Cluster> clusters2 = k.pointLloyd(clusterAmount, points, size,vis);
			}
			else if(kVariant.equals("pointMQ")) {
				CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
				System.out.println("Random points Mac Queen:  k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
				ArrayList<Cluster> clusters4 = k.pointMQ(clusterAmount, points, size,vis);
			}
			else if(kVariant.equals("compare")) {
				System.out.println("Comparing Algorithms: k="+clusterAmount+", dimension = " + dim + ", N = " + N);
				compare(k, gen, N, clusterAmount, size, dim);
			}
			else if(kVariant.equals("testSeries")) {
				testSeries(k,gen);
			}
			
			else {
				System.out.println("Invalid Kmeans Algorithm: "+kVariant+"\n"+" Available Arguments are: partitionLloyed, partitionMQ, pointLloyed, pointMQ, compare, testSeries");
			}
		//}
		
		//Generator für die die Daten, die Daten werden in der Form CopyOnWriteArrayList gegeben, da diese beim Iterieren, solange man nicht darauf schreibt, schneller und gut parallelisierbar sein soll
		
		if(png) {
			//Die 4 unterschiedlichen KMeans Algorithmen( Die Funktionen nehmen alle als Argument die cluster Menge, die generierten Punkt und die Größe des Teilbereichs des R^n auf den sich die Punkte befinden an)::
			//ArrayList<Cluster> clusters=k.randomMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
			//ArrayList<Cluster> clusters=k.pointMQ(clusterAmount,gen.generate(clusterAmount, points, dim,size),size);
			ArrayList<Cluster> clusters=k.randomLloyd(clusterAmount,gen.generate(clusterAmount, N, dim,size),size, false);
			//ArrayList<Cluster> clusters=k.pointLloyd(clusterAmount,gen.generate(clusterAmount, points, dim,size),size,false);
			
			visualizeResults(clusters, dim,kVariant);
		}
		//testSeries(k, gen);
		System.out.println("End of Program");
	}
	
	public static int parseDecimalValue(String argument, int defaultValue) {
		
		int value;
		try {
			value = Integer.parseInt(argument);
		} catch (NumberFormatException e) {
			System.out.println("Invalid Number Format using the default value: " + defaultValue);
			value = defaultValue;
		}
		if(value < 0) {
			System.out.println("Numbers below 0 are not allowed, using the positive value: " + (-value));
			return -value;
		} else if(value == 0) {
			System.out.println("0 is not allowed, using the default: " + defaultValue);
			return defaultValue;
		}
		return value;
	}
	
	public static void visualizeResults(ArrayList<Cluster> clusters, int dim, int iteration, String algorithm) {
		if(dim==2) {
			final XYPlane window=new XYPlane(clusters, iteration, algorithm);
			window.pack();
			RefineryUtilities.centerFrameOnScreen(window);
			window.setVisible(true);
		}
	}
	public static void visualizeResults(ArrayList<Cluster> clusters, int dim,String image_titel) {
		try {
			GnuPlot s=new GnuPlot(clusters,dim,image_titel);
		} catch (IOException e) {
			e.printStackTrace();
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
			compare(k, gen, N, clusterAmount, size, dim);
		}
	}
	
	public static void compare(KMeans k, ClusterGenerator gen, int N, int clusterAmount, double size, int dim) {
		System.out.println("\n");
		CopyOnWriteArrayList<ArrayList<Double>> points = gen.generate(clusterAmount, N, dim, size);
		
		
		System.out.println("Random partition Lloyd: k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
		ArrayList<Cluster> clusters = k.randomLloyd(clusterAmount, points, size, false);
	
		
		System.out.println("Random points Lloyd: k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
		ArrayList<Cluster> clusters2 = k.pointLloyd(clusterAmount, points, size,false);
	
		System.out.println("Random partition Mac Queen:  k="+clusterAmount+",  dimension = " + dim + ", N = " + N);
		ArrayList<Cluster> clusters3 = k.randomMQ(clusterAmount, points, size,false);
	
		
		System.out.println("Random points Mac Queen:  k="+clusterAmount+", dimension = " + dim + ", N = " + N);
		ArrayList<Cluster> clusters4 = k.pointMQ(clusterAmount, points, size,false);
		
		System.out.println("\n");
	}
}

	


