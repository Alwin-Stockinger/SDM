package SDM.Spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;


public class K_Means {
	
		// Kmeans Algorithm of Spark
		public static void Kmeans(JavaRDD<Vector> parsedrdd) 
		{         
	           KMeans kmeans = new KMeans();
	           
	           // A clustering model for K-means of apache spark
	           KMeansModel clusters = kmeans.run(parsedrdd.rdd());
	          
	           // Implemented Distance function --> pass clusters and parsedrdd to method			   
	           Double euclidian_distance = Distance.euclidian(clusters,parsedrdd);
	           
	           // Return the number of elements in the RDD
	           long Elements_rdd = parsedrdd.rdd().count();
	           
	           // using the overall average distance as our quality score for the clustering result
	           long average_distance = (long) (euclidian_distance / Elements_rdd);
	           System.out.println("k: 2, maxIterations: 20, runs: 1, initializationSteps: 5, epsilon: 1e-4");
	           System.out.println("Average Distance: " + average_distance);                
	   }
		
		public static double choosek(JavaRDD<Vector> parsedrdd, int k,int maxIterations)
		{	            											                     
	            long Elements_rdd = parsedrdd.rdd().count();
	            
	            double sum_distance = 0.0;
	                
	            double sum_cost = 0.0;
	            
	            //int maxIterations = 30;
	                
	                // Run 10 times to get average result
	            	int runs=10;
	                for(int j = 0 ; j < runs; j++)
	                {
	                   KMeans kmeans = new KMeans();
	                   
	                   kmeans.setEpsilon(1.0e-6);
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
				current=choosek(parsedrdd,k,30);
				System.out.println("\n\n\n Current K is "+k+" with distance "+current);
				
				double up=choosek(parsedrdd,k+stair,30);
				double down=choosek(parsedrdd,k-stair,30);
				
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
			
		}
		
		
		
}
