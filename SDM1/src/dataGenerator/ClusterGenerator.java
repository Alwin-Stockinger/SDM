package dataGenerator;

public class ClusterGenerator {
	
	GaussianVector genVec;
	public double size=20;	//sets die size of the space where cluster centers can be in +- direction, so the volume of the n dimensional space would be (size*2)^n
	
	public double[][] generate(int clusters,int points, int dim){
		double[][] data;
		double[][] clusterPoints;
		genVec=new GaussianVector();
		
		
		clusterPoints=genClusterPoints(clusters,dim);
		data=genData(clusterPoints,points,dim);

		
		return data;
	}

	private double[][] genData(double[][] clusterPoints, int points, int dim) {
		
		double[][] data=new double[points][dim];

		for(int i=0; i<clusterPoints.length;++i) {
			for(int j=0;j<points/clusterPoints.length;++j) {
				data[i*points/clusterPoints.length+j]=genVec.gausPoint(clusterPoints[i]);
			}
		}
		
		
		
		return data;
	}

	private double[][] genClusterPoints(int clusters, int dim) {
		double[][] points=new double[clusters][dim];
		
		for(int i=0;i<clusters;i++)	points[i]=genVec.startPoint(dim, size);
		
		return points;
	}
	
}
