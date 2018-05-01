package SDM.Spark;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;

public class CalculateClusters implements Runnable{
	JavaRDD<Vector> parsed_data;
	
	private boolean optimised=false;
	private int num_clusters=2;	// als default, auch wenn nicht optimised
		
	private int maxIterations=2;//20;
	private int runs=2;
	private int initializationSteps=2;//5;
	private double epsilon=1.0e-2;//-6;
	private double distance=-1.0;
	
	@Override
	public void run()	{
		System.out.println("Start calculation for " + num_clusters +" Clusters");
		distance=choosek(num_clusters);
	}
	
	public CalculateClusters(JavaRDD<Vector> parsedrdd,int k, int maxIterations)	{
		this.parsed_data=parsedrdd;
		this.num_clusters=k;
		this.maxIterations=maxIterations;
	}

	public double Distance()	{
		return distance;
	}
	public int numClusters()	{
		return num_clusters;
	}
	
	private double choosek(int k)	{	            											                     
		
		long Elements_rdd = parsed_data.rdd().count();
        
        double sum_distance = 0.0;
        double sum_cost = 0.0;
        
        //int maxIterations = 30;
            
        // Run 10 times to get average result
    	//int runs=10;
        for(int j = 1 ; j<=runs ; j++)	 {
        	
           KMeans kmeans = new KMeans();
           
           kmeans.setEpsilon(epsilon);
           kmeans.setInitializationSteps(initializationSteps);
           kmeans.setMaxIterations(maxIterations);
           kmeans.setRuns(runs);
           kmeans.setK(numClusters());
           
           
           //kmeans.setRuns(10); does not do anything since spark 2
        	
           // Trains a k-means model using specified parameters, k - Number of clusters to create.
           // maxIterations - Maximum number of iterations allowed.
           //KMeansModel clusters = kmeans.train(parsedrdd.rdd(), k, maxIterations);
           KMeansModel clusters = kmeans.run(parsed_data.rdd());
           
           // Implemented Distance function --> pass clusters and parsedrdd to method			   
           double euclidian_distance = Distance.euclidian(clusters,parsed_data);	                		                    	                    	                
            
           double average_distance = euclidian_distance / Elements_rdd;           
    
           sum_distance += average_distance;
           
           //Return the K-means cost (sum of squared distances of points to their nearest center) for this model on the given data.
           double centroid_distance = clusters.computeCost(parsed_data.rdd());
           
           sum_cost += centroid_distance;
           
           sum_distance/=runs;
           sum_cost/=runs;
           
           System.out.println("Clusters="+ k+ " run="+j + " sum_distance=" + sum_distance);
        }                	                             	                       
        
            
        //System.out.println("Average Euclidian Distance achieved with k = " + k + ", value is " + (sum_distance));
        //System.out.println("Distance from a data point to its nearest cluster’s centroid with k = " + k + ", value is " + (sum_cost ));
        return sum_distance;
    }
	
	
}
