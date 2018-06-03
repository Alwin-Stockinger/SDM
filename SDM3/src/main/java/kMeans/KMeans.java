package kMeans;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import Hashfunktions.Bucket;
import Hashfunktions.HashGenerator;
import Hashfunktions.LSH;
import data.DataPoint;
import main.TimeMeasurement;

public class KMeans {
	
	double maxDistance=Math.sqrt(10*Math.pow(100,2));		//DIM=10,volume=100^10
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
		if(count<1) count=1;
		this.ANDCount=count;
	}
	void setOrCount(int count) {
		if(count<1) count=1;
		this.OrCount=count;
	}

	
	
	//KMean with LSH
	public ArrayList<Cluster> lshLloyed(ArrayList<DataPoint> startPoints,List<DataPoint> data, int ANDCount, int ORCount, int bucketNumber,int iterations) {
		initWithPoints(startPoints);
		
		
		
		
		
		setAndCount(ANDCount);
		setOrCount(ORCount);
		
		initLSH(startPoints.get(0).getDim(),bucketNumber);
		hashToBuckets(data);
		
		int i;
		for(i=0;i<iterations;i++) {
			TimeMeasurement time=new TimeMeasurement();
			time.Start();
			assignPoints(data);
			calcCentroids();
			time.Stop();
		}
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
	
	
	
	
	private void assignPoints(List<DataPoint> data) {	
		
		Set<DataPoint> unasignedPoints=new HashSet<DataPoint>(data);
		
		for(int k=0;k<clusters.size();k++) {
			Cluster cluster=clusters.get(k);
			cluster.wipePoints();
			
			Set<DataPoint> mainSet=new HashSet<DataPoint>();

			Set<DataPoint> orSet;
			
			//OR Combinator
			for(int i=0;i<OrCount;i++) {
				orSet=new HashSet<DataPoint>(lshVec.get(i*ANDCount).getPointsByCentroid(cluster.getCentroid()));
				
				//AND Combinator
				for(int j=1;j<ANDCount;j++) {
					orSet.retainAll(lshVec.get(i*ANDCount+j).getPointsByCentroid(cluster.getCentroid()));
				}
				mainSet.addAll(orSet);
				
			}
		
			
			//removes already assigned points
			for(int i=0;i<k;i++) {
				mainSet.removeAll(clusters.get(i).getDataPoints());
			}
			
			unasignedPoints.removeAll(mainSet);
			
			cluster.addPoints(mainSet);
		}
		
		
		assignNonBucketPoints(unasignedPoints);
	}
	
	private void assignNonBucketPoints(Set<DataPoint> data) {
		for(DataPoint point:data) {
			nearestCluter(point).addPoint(point);
		}
	}
	
	
	private Cluster nearestCluter(DataPoint point) {
		
		int closest=0;
		
		double minDistance=maxDistance;
		
		
		
		for(int i=0;i<clusters.size();i++) {
			double dist=distance(point,clusters.get(i).getCentroid());
			if(dist<minDistance) {
				minDistance=dist;
				closest=i;
			}
		}
		
		return clusters.get(closest);
	}
	
	public double distance(DataPoint a,DataPoint b) {	//berechnet die L2 Norm von 2 Punkten
		double sum=0;

		
		for(int i=0;i<a.getDim();i++) {
			sum+=Math.pow(a.getVector()[i]-b.getVector()[i], 2);
		}
		
		return Math.sqrt(sum);
	}
	
	
	
	

	private void calcCentroids() {
		for(Cluster cluster:clusters) {
			cluster.calcCentroid();
		}
	}
	
	
}