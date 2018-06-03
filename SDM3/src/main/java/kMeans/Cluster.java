package kMeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import data.DataPoint;


public class Cluster {
	
	private DataPoint centroid;
	
	private ArrayList<DataPoint> points;
	
	
	private boolean converged=false;
	
	
	
	
	public boolean isConverged() {
		return converged;
	}


	public void setConverged(boolean converged) {
		this.converged = converged;
	}

	
	public DataPoint getCentroid() {
		return centroid;
	}


	
	public void addPoints(Set<DataPoint> mainSet) {
		points.addAll(mainSet);
	}
	
	
	public void addPoint(DataPoint point) {
		points.add(point);
	}
	
	public void wipePoints() {
		if(!points.isEmpty())	points=new ArrayList<DataPoint>();
	}

	
	public Cluster(DataPoint point) {
		this.centroid=point;
		points=new ArrayList<>();
	}
	
	public ArrayList<DataPoint> getDataPoints() {
		return this.points;
	}
	
	
	
	
	
	
	public void calcCentroid() {	//berechnet den neuen Centroid des Clusters
		DataPoint centroid=sumPoints();
		if(points.size()>0) for(int i=0;i<centroid.getDim();++i) centroid.getVector()[i]/=points.size();
		this.centroid=new DataPoint(centroid.getVector(),null);
	}
	
	
	private DataPoint sumTwoPoints(DataPoint sumPoint, DataPoint point){	//Vektor addition
		
		for(int i=0;i<sumPoint.getDim();++i) {
			sumPoint.getVector()[i]+=point.getVector()[i];
		}
		return sumPoint;
	}
	
	private DataPoint sumPoints() {		//summiert alle Punkte des Clusters
		DataPoint sumPoint=zeroPoint(centroid.getDim());
		
		for(DataPoint point:points) {
			sumPoint=sumTwoPoints(sumPoint,point);
		}
		return sumPoint;
	}
	
	private DataPoint zeroPoint(int dim){	//gibt den Nullvektor zurück
		double[] point=new double[10];
		for(int i=0;i<dim;i++)	point[i]=0.;
		
		DataPoint dataPoint=new DataPoint(point, null);
		return dataPoint;
	}
	
	public int size() {
		return points.size();
	}


}
