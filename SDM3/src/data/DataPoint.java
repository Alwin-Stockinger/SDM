package data;

public class DataPoint {

	private int dim;
	private double[] vectors;
	private String cluster;
	private String groundTruth;
	
	public DataPoint(double[] vectors, String cluster) {
		
		setDim(10);	// the dimension is expected to always be 10
		setVectors(vectors);
		setCluster(cluster);
		setTruth(cluster);
	}
	
	public void setDim(int dim) {
		this.dim = dim;
	}
	
	public int getDim() {
		return dim;
	}
	
	public void setVectors(double[] vectors) {
		this.vectors = vectors;
	}
	
	public double[] getVectors() {
		return vectors;
	}
	
	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	
	public String getCluster() {
		return cluster;
	}
	
	public void setTruth(String groundTruth) {
		this.groundTruth = groundTruth;
	}
	
	public String getTruth() {
		return groundTruth;
	}
}