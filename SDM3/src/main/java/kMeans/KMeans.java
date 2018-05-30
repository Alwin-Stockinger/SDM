package kMeans;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import Hashfunktions.Bucket;
import Hashfunktions.HashGenerator;
import Hashfunktions.LSH;
import data.DataPoint;

public class KMeans {
	
	double maxDistance;
	private ArrayList<Cluster> clusters;
	private ArrayList<Bucket> buckets;
	
	private int ANDCount=0;
	private int OrCount=0;
	
	private ArrayList<LSH> lshVec;
	
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	void initWithPoints(ArrayList<DataPoint> points) {
		clusters=new ArrayList<Cluster>();
	
		for(DataPoint point: points) {
			clusters.add(new Cluster(point));
		}
	}
	
	void setAndCount(int count) {
		this.ANDCount=count;
	}
	void setOrCount(int count) {
		this.OrCount=count;
	}

	
	
	//KMean with LSH
	public ArrayList<Cluster> lshLloyed(ArrayList<DataPoint> startPoints,List<DataPoint> data, int ANDCount, int ORCount,int iterations) {
		initWithPoints(startPoints);
		
		
		//setMaxDistance(size);	//Größt mögliche Distanz zwischen zwei Punkten festlegen
		
		
		setAndCount(ANDCount);
		setOrCount(ORCount);
		
		initLSH(startPoints.get(0).getDim(),startPoints.size());
		hashToBuckets(data);
		
		int i;
		for(i=0;i<iterations;i++) {
			System.out.println("Current Iteration is "+i);
			assignPoints();
			calcCentroids();
		}
		System.out.println("Iterations done: "+i);
		return clusters;
	}
	
	private void initLSH(int dim,int bucketnumber) {
		this.lshVec=new ArrayList<>(ANDCount*OrCount);
		for(int i=0;i<ANDCount*OrCount;++i) {
			lshVec.add(new LSH(HashGenerator.generateHashVector(dim, 1),bucketnumber));
		}
	}
	
	private void hashToBuckets(List<DataPoint> data) {
		for(LSH lsh:lshVec) {
			lsh.hash(data);
		}
	}
	
	private void assignPoints() {	//no AND currently possible with this implementation
		for(LSH lsh:lshVec) {
			for(Cluster cluster:clusters) {
				cluster.wipePoints();
				cluster.addPoints(lsh.getPointsByCentroid(cluster.getCentroid()));
			}
		}
	}
	
	private void calcCentroids() {
		for(Cluster cluster:clusters) {
			cluster.calcCentroid();
		}
	}
	
	
}