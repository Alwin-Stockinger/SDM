package main;

import java.util.ArrayList;

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
