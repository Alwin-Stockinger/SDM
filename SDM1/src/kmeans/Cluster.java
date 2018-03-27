package kmeans;

import java.util.ArrayList;
import java.util.Iterator;


public class Cluster {
	
	private ArrayList<Double> centroid;
	
	private ArrayList<ArrayList<Double>> points;
	
	
	private boolean converged=false;
	
	
	
	public boolean isConverged() {
		return converged;
	}


	public void setConverged(boolean converged) {
		this.converged = converged;
	}


	public ArrayList<ArrayList<Double>> getPoints() {
		return points;
	}
	public ArrayList<Double> getPoint(int pos) {
		return points.get(pos);
	}
	
	public void setPoints(ArrayList<ArrayList<Double>> points) {
		this.points = points;
	}

	public ArrayList<Double> getMeanPoint() {
		return centroid;
	}

	public void setMeanPoint(ArrayList<Double> centroid) {
		this.centroid = centroid;
	}
	

	
	public void addPoint(ArrayList<Double> point) {
		points.add(point);
	}
	
	public void wipePoints() {
		if(!points.isEmpty())	points.removeAll(points);
	}

	
	public Cluster(ArrayList<Double> start) {
		this.centroid=start;
		this.points=new ArrayList<ArrayList<Double>>();
	}
	
	public Cluster(int dim) {
		this.centroid=zeroPoint(dim);
		this.points=new ArrayList<ArrayList<Double>>();
	}
	
	
	
	
	
	public void calcCentroid() {	//berechnet den neuen Centroid des Clusters
		ArrayList<Double> centroid=sumPoints();
		if(points.size()>0) for(int i=0;i<centroid.size();++i) centroid.set(i, centroid.get(i)/points.size());
		setConverged(this.centroid.equals(centroid));	// wenn der alte Centroid am selben Punkt ist wie der neue, dann ist der Cluster konvergiert
		this.centroid=centroid;
		
	}
	
	
	private ArrayList<Double> sumTwoPoints(ArrayList<Double> a, ArrayList<Double> b){	//Vektor addition
		ArrayList<Double> sum=new ArrayList<Double>();
		for(int i=0;i<a.size();++i) {
			sum.add(b.get(i)+a.get(i));
		}

		return sum;
	}
	
	private ArrayList<Double> sumPoints() {		//summiert alle Punkte des Clusters
		ArrayList<Double> sum=zeroPoint(centroid.size());
		Iterator<ArrayList<Double>> iter=points.iterator();
		while(iter.hasNext())	sum=sumTwoPoints(sum,iter.next());
		return sum;
	}
	
	private ArrayList<Double> zeroPoint(int dim){	//gibt den Nullvektor zurück
		ArrayList<Double> point=new ArrayList<Double>();
		for(int i=0;i<dim;i++)	point.add(0.0);
		return point;
	}


	public int size() {
		return points.size();
	}
}
