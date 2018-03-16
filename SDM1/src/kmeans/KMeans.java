package kmeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import dataGenerator.GaussianVector;

public class KMeans {
	
	double maxDistance;
	
	
	private ArrayList<Cluster> clusters;
	

	
	
	
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	private void setMaxDistance(double size) {
		ArrayList<Double> minus=new ArrayList<Double>();
		ArrayList<Double> plus=new ArrayList<Double>();
		
		for(int i=0; i<clusters.get(0).getMeanPoint().size();++i) {
			minus.add(-size);
			plus.add(size);
		}
		maxDistance=distance(minus,plus);
	}
	
	public ArrayList<Cluster> randomLloyd(int clusterCount,CopyOnWriteArrayList<ArrayList<Double>> data, double size) {
		initRandomRandom(clusterCount,data.get(0).size(),size);
		setMaxDistance(size);
		
		int i=0;
		while(!converged()) {
			pointsToClusters(data);
			calcAllCentroids(data);
			++i;
		}
		System.out.println("Iterationen bis Konvergenz: "+i);
		return clusters;
	}
	
	
	
	public ArrayList<Cluster> pointLloyd(int clusterCount,CopyOnWriteArrayList<ArrayList<Double>> data, double size) {
		initRandomPoints(clusterCount,data);
		setMaxDistance(size);
		
		int i=0;
		while(!converged()) {
			pointsToClusters(data);
			calcAllCentroids(data);
			++i;
		}
		System.out.print("Iterationen bis Konvergenz: "+i);
		return clusters;
	}
	
	public ArrayList<Cluster> pointMQ(int clusterCount,CopyOnWriteArrayList<ArrayList<Double>> data, double size) {
		initRandomPoints(clusterCount,data);
		setMaxDistance(size);
		
		int i=0;
		boolean converged=false;
		while(!converged) {
			converged=true;
			Iterator<ArrayList<Double>> iter=data.iterator();
			while(iter.hasNext()) {
				if(pointToCluster(iter.next())) {
					calcAllCentroids(data); //TODO Optimize so that only the two corresponding clusters get updated
					converged=false;
				}
			}
			++i;
		}
		
		System.out.println("Iterationen bis Konvergenz: "+i);
		return clusters;
	}
	
	
	public ArrayList<Cluster> randomMQ(int clusterCount,CopyOnWriteArrayList<ArrayList<Double>> data, double size) {
		initRandomRandom(clusterCount,data.get(0).size(),size);
		setMaxDistance(size);
		
		int i=0;
		boolean converged=false;
		while(!converged) {
			converged=true;
			Iterator<ArrayList<Double>> iter=data.iterator();
			while(iter.hasNext()) {
				if(pointToCluster(iter.next())) {
					calcAllCentroids(data); //TODO Optimize so that only the two corresponding clusters get updated
					converged=false;
				}
			}
			++i;
		}
		
		System.out.println("Iterationen bis Konvergenz: "+i);
		return clusters;
	}
	
	
	
	private boolean pointToCluster(ArrayList<Double> point) {
		Cluster near=nearestCluster(point);
		if(near.getPoints().contains(point)) return false;
		else {
			Iterator<Cluster> iter=clusters.iterator();
			while(iter.hasNext()) iter.next().getPoints().remove(point);		//TODO Optimizable
			near.addPoint(point);
			return true;
		}
	}

	private boolean converged() {
		Iterator<Cluster> iter=clusters.iterator();
		while(iter.hasNext())	if(!iter.next().isConverged()) return false;
		
		return true;
	}
	
	
	
	private void initRandomPoints(int clusterCount,CopyOnWriteArrayList<ArrayList<Double>> data) {
		Random randomGenerator = new Random();
		ArrayList<Integer> used=new ArrayList<Integer>();
		
		clusters=new ArrayList<Cluster>();
		
		for(int i=0;i<clusterCount;++i) {
			int start=randomGenerator.nextInt(data.size());
			while(used.contains(start)) {
				start=randomGenerator.nextInt(data.size());
			}
			Cluster cluster=new Cluster(data.get(start));
			clusters.add(cluster);
		}

	}
	
	private void initRandomRandom(int clusterCount,int dim, double size) {
		GaussianVector generator=new GaussianVector();
		//TODO implement detection for two points on the same space
		clusters=new ArrayList<Cluster>();
		
		for(int i=0;i<clusterCount;++i) {
			Cluster cluster=new Cluster(generator.startPoint(dim, size));
			clusters.add(cluster);
		}

	}

	
	private void calcAllCentroids(CopyOnWriteArrayList<ArrayList<Double>> data) {
		Iterator<Cluster> iter=clusters.iterator();
		while(iter.hasNext()) iter.next().calcCentroid();
	}
	
	private void emptyClusters() {
		Iterator<Cluster> iter=clusters.iterator();
		while(iter.hasNext()) iter.next().wipePoints();
	}
	
	private void pointsToClusters(CopyOnWriteArrayList<ArrayList<Double>> data) {
		emptyClusters();
		Iterator<ArrayList<Double>> iter=data.iterator();
		while(iter.hasNext()) {
			ArrayList<Double> point=iter.next();
			nearestCluster(point).addPoint(point);
		}
	}
	
	
	private Cluster nearestCluster(ArrayList<Double> point) {
		int nearest=0;
		double minDistance=maxDistance;
		Iterator<Cluster> iter=getClusters().iterator();
		
		
		int i=0;
		while(iter.hasNext()) {
			double dist=distance(point,iter.next().getMeanPoint());
			if(dist<minDistance) {
				minDistance=dist;
				nearest=i;
			}
			++i;
		}
		
		return getClusters().get(nearest);
	}

	public double distance(ArrayList<Double> a,ArrayList<Double> b) {
		double sum=0;
		Iterator<Double> iterA=a.iterator();
		Iterator<Double> iterB=b.iterator();
		while(iterA.hasNext())	sum+=Math.pow(iterB.next()-iterA.next(), 2);
		return Math.sqrt(sum);
	}
}
