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
	
	double maxDistance=Math.sqrt(10.*Math.pow(100,2));		//DIM=10,volume=100^10
	private ArrayList<Cluster> clusters;
	private ArrayList<Bucket> buckets;
	private boolean isHashing;
	
	private int ANDCount=0;
	private int OrCount=0;
	
	private ArrayList<LSH> lshVec;

	public KMeans(boolean standard) {
		isHashing=!standard;
	}
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	//KMean with LSH
	public ArrayList<Cluster> lshLloyed(ArrayList<DataPoint> startPoints,List<DataPoint> data, int ANDCount, int ORCount, int bucketNumber,int iterations) {
		
		
		initWithPoints(startPoints);
		
		
		if(isHashing) {	
			setAndCount(ANDCount);
			setOrCount(ORCount);
			initLSH(startPoints.get(0).getDim(),bucketNumber);
			hashToBuckets(data);
		}
		Set<DataPoint> dataSet=new HashSet<DataPoint>(data);
		int i;
		for(i=0;i<iterations;i++) {
			removeClusterPoints();
			
			if(isHashing) assignPoints(dataSet);
			else assignNonBucketPoints(dataSet);
			
			calcCentroids();
		}
		return clusters;
	}
	public double distance(DataPoint a,DataPoint b) {	//berechnet die L2 Norm von 2 Punkten
		double sum=0;

		for(int i=0;i<a.getDim();i++) {
			sum+=Math.pow(a.getVector()[i]-b.getVector()[i], 2);
		}
		return Math.sqrt(sum);
	}

	
	private void initWithPoints(ArrayList<DataPoint> points) {
		clusters=new ArrayList<Cluster>();
	
		for(DataPoint point: points) {
			clusters.add(new Cluster(point));
		}
	}
	private void setAndCount(int count) {
		if(count<1) count=1;
		this.ANDCount=count;
	}
	private void setOrCount(int count) {
		if(count<1) count=1;
		this.OrCount=count;
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
	private void removeClusterPoints() {
		for(Cluster cluster:clusters) {
			cluster.wipePoints();
		}
	}
	private void assignPoints(Set<DataPoint> data) {	
		
		Set<DataPoint> unasignedPoints=data;

		for(int k=0;k<clusters.size();k++) {
			Cluster cluster=clusters.get(k);
			
						
			Set<DataPoint> mainSet=new HashSet<DataPoint>();

			Set<DataPoint> orSet = null;
			
			//OR Combinator
			for(int i=0;i<OrCount;i++) {
				orSet=new HashSet<DataPoint>(lshVec.get(i*ANDCount).getPointsByCentroid(cluster.getCentroid()));
				//System.out.println("s=" +orSet.size());
				
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
			//System.out.println("s=" +mainSet.size() +" " + unasignedPoints.size());
			
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
	private void calcCentroids() {
		for(Cluster cluster:clusters) {
			cluster.calcCentroid();
		}
	}
	
	
}