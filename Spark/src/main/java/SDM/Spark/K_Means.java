package SDM.Spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;


public class K_Means {
	
	// Kmeans Algorithm of Spark
		public static void Kmeans(JavaRDD<Vector> parsedrdd) 
		{         
	           // Kmeans Algorithm of apache spark
	           KMeans kmeans = new KMeans();
	           
	           // A clustering model for K-means of apache spark
	           KMeansModel clusters = kmeans.run(parsedrdd.rdd());
	          
	           // Implemented Distance function --> pass clusters and parsedrdd to method			   
	           Double euclidian_distance = Distance.euclidian(clusters,parsedrdd);
	           
	           // Return the number of elements in the RDD
	           long Elements_rdd = parsedrdd.rdd().count();
	           
	           // using the overall average distance as our quality score for the clustering result
	           long average_distance = (long) (euclidian_distance / Elements_rdd);
	           
	           System.out.println("Average Distance: " + average_distance);                
	   } 

}
