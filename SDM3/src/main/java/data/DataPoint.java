package data;

public class DataPoint {

	private int dim;
	private double[] vector;
	private double cluster;
	private String groundTruth;
	
	public DataPoint(double[] vectors, String cluster) {
		
		setDim(10);	// the dimension is expected to always be 10
		setVector(vectors);
		setCluster(-1);
		setTruth(cluster);
	}
	
	public void setDim(int dim) {
		this.dim = dim;
	}
	
	public int getDim() {
		return dim;
	}
	
	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	public double[] getVector() {
		return vector;
	}
	
	public void setCluster(double cluster) {
		this.cluster = cluster;
	}
	
	public double getCluster() {
		return cluster;
	}
	public double getTruthClusterNr() {
		return Double.parseDouble(groundTruth.replace("class", ""));
	}
	
	public void setTruth(String groundTruth) {
		this.groundTruth = groundTruth;
	}
	
	public String getTruth() {
		return groundTruth;
	}
}