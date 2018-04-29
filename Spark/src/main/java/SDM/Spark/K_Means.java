package SDM.Spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;


public class K_Means {
	private static boolean optimised=false;
	private static int num_clusters=2;	// als default, auch wenn nicht optimised
		
	private static int maxIterations=30;
	private static int runs=10;
	private static int initializationSteps=5;
	private static double epsilon=1.0e-6;
	
	//--------------------------------------------------------------------------------------------------
	
	public static int getNum_clusters() {
		return num_clusters;
	}
	/*public static void setNum_clusters(int num_clusters) {
		K_Means.num_clusters = num_clusters;
	}*/

	public static int getMaxIterations() {
		return maxIterations;
	}
	public static void setMaxIterations(int maxIterations) {
		K_Means.maxIterations = maxIterations;
	}

	public static int getRuns() {
		return runs;
	}
	public static void setRuns(int runs) {
		K_Means.runs = runs;
	}

	public static int getInitializationSteps() {
		return initializationSteps;
	}
	public static void setInitializationSteps(int initializationSteps) {
		K_Means.initializationSteps = initializationSteps;
	}

	public static double getEpsilon() {
		return epsilon;
	}
	public static void setEpsilon(double epsilon) {
		K_Means.epsilon = epsilon;
	}

	//--------------------------------------------------------------------------------------------------

	public static String getParameters()	{
		return " maxIterations: "+K_Means.getMaxIterations()
				+", runs: "+K_Means.getRuns()
				+", initializationSteps: "+K_Means.getInitializationSteps()
				+", epsilon: "+K_Means.getEpsilon();   	
	}
	
	public static void Kmeans(JavaRDD<Vector> parsedData)	{   	// Kmeans Algorithm of Spark      
           KMeans kmeans = new KMeans();
           
           // A clustering model for K-means of apache spark
           //KMeansModel clusters = kmeans.run(parsedData.rdd());
           KMeansModel clusters = KMeans.train(parsedData.rdd(), num_clusters, maxIterations);
           
           // Implemented Distance function --> pass clusters and parsedrdd to method			   
           Double euclidian_distance = Distance.euclidian(clusters,parsedData);
           
           // Return the number of elements in the RDD
           long Elements_rdd = parsedData.rdd().count();
           
           // using the overall average distance as our quality score for the clustering result
           //long average_distance = (long) (euclidian_distance / Elements_rdd);
           System.out.println("k: "+num_clusters+getParameters());
           System.out.println("Average Distance: " + (long) (euclidian_distance / Elements_rdd));                
	   }

	public static boolean IsOptimised()	{
		return optimised;
	}
	
	public static double choosek(JavaRDD<Vector> parsedrdd, int k, int maxIterations)	{	            											                     
		
		long Elements_rdd = parsedrdd.rdd().count();
        
        double sum_distance = 0.0;
        double sum_cost = 0.0;
        
        //int maxIterations = 30;
            
        // Run 10 times to get average result
    	//int runs=10;
        for(int j = 0 ; j < runs; j++)	 {
           KMeans kmeans = new KMeans();
           
           kmeans.setEpsilon(epsilon);
           //kmeans.setRuns(10); does not do anything since spark 2
        	
           // Trains a k-means model using specified parameters, k - Number of clusters to create.
           // maxIterations - Maximum number of iterations allowed.
           KMeansModel clusters = kmeans.train(parsedrdd.rdd(), k, maxIterations);

           // Implemented Distance function --> pass clusters and parsedrdd to method			   
           double euclidian_distance = Distance.euclidian(clusters,parsedrdd);	                		                    	                    	                
            
           double average_distance = euclidian_distance / Elements_rdd;           
    
           sum_distance += average_distance;
           
           //Return the K-means cost (sum of squared distances of points to their nearest center) for this model on the given data.
           double centroid_distance = clusters.computeCost(parsedrdd.rdd());
           
           sum_cost += centroid_distance;
        }                	                             	                       
        
        sum_distance/=runs;
        sum_cost/=runs;
            
        System.out.println("Average Euclidian Distance achieved with k = " + k + ", value is " + (sum_distance));
        System.out.println("Distance from a data point to its nearest cluster’s centroid with k = " + k + ", value is " + (sum_cost ));
        return sum_distance;
    }

	public static void findk(JavaRDD<Vector> parsedrdd,int k, int stair) {
		double current=0;
		boolean best=false;
		while(!best) {
			current=choosek(parsedrdd,k,maxIterations);
			System.out.println("\n\n\n Current K is "+k+" with distance "+current);
			
			double up=choosek(parsedrdd,k+stair,maxIterations);
			double down=choosek(parsedrdd,k-stair,maxIterations);
			
			if(current<up&&current<down) {
				if(stair==1) best=true;
				stair/=2;
				
				//to get rid of lucky inits
			}
			else if(up>down) {
				current=down;
				k-=stair;
			}
			else {
				current=up;
				k+=stair;
			}
		}
		System.out.println("\nBest k is "+k+", with Average Euclidian Distance "+current+"\n");
		num_clusters=k;
		optimised=true;
	}
		
		
		
}
